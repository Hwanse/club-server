package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.api.dto.club.ClubAccountStatusDto;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.club.ClubDto;
import com.study.clubserver.api.dto.club.ClubMembersDetailsDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.club.clubAccount.ClubAccount;
import com.study.clubserver.domain.club.clubAccount.ClubAccountRepository;
import com.study.clubserver.domain.club.clubAccount.ClubAccountService;
import com.study.clubserver.domain.club.clubAccountRole.ClubAccountRole;
import com.study.clubserver.domain.club.clubAccountRole.ClubAccountRoleRepository;
import com.study.clubserver.domain.club.clubInterest.ClubInterest;
import com.study.clubserver.domain.club.clubInterest.ClubInterestRepository;
import com.study.clubserver.domain.interest.InterestRepository;
import com.study.clubserver.domain.role.RoleRepository;
import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.domain.club.clubZone.ClubZone;
import com.study.clubserver.domain.club.clubZone.ClubZoneRepository;
import com.study.clubserver.domain.zone.ZoneRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubService {

  private final ClubRepository clubRepository;
  private final ClubAccountService clubAccountService;
  private final ClubAccountRepository clubAccountRepository;
  private final ClubAccountRoleRepository clubAccountRoleRepository;
  private final RoleRepository roleRepository;

  private final InterestRepository interestRepository;
  private final ClubInterestRepository clubInterestRepository;

  private final ZoneRepository zoneRepository;
  private final ClubZoneRepository clubZoneRepository;

  @Transactional
  public Club createClub(Account account, ClubCreateRequest request) {
    Club savedClub = clubRepository.save(new Club(request));

    // 클럽 - 유저 관계 추가
    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(savedClub, account));
    // 클럽유저 - 클럽내 유저 권한 관계 추가
    roleRepository.findByRoleType(RoleType.MANAGER)
                  .ifPresent(
                    role -> clubAccountRoleRepository.save(new ClubAccountRole(role, clubAccount))
                  );
    // 클럽 - 주요 관심사 관계 설정
    interestRepository.findInterestsByIdList(request.getInterestList())
                      .forEach(
                        interest -> clubInterestRepository.save(new ClubInterest(savedClub, interest))
                      );

    // 클럽 - 주요 지역 관계 설정
    zoneRepository.findZonesByIdList(request.getZoneList())
                  .forEach(
                    zone -> clubZoneRepository.save(new ClubZone(savedClub, zone))
                  );

    savedClub.incrementMemberCount();

    return savedClub;
  }

  @Transactional(readOnly = true)
  public ClubMembersDetailsDto getClubDetails(Long clubId, Account account) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    List<ClubAccount> accountListOfClub = getClubAccountListOfClub(club);
    if (!isJoinedInClub(account, accountListOfClub)) {
      throw new IllegalArgumentException("클럽에 가입되어 있지 않은 회원입니다");
    }

    // 현재 인증 유저의 상세 정보
    ClubAccountStatusDto clubAccountStatusDto = clubAccountService
      .getClubAccountDetailWithRole(club, account);

    // 클럽의 구성원 리스트
    List<ClubAccountInfoDto> clubAccountsOfClub = accountListOfClub
      .stream()
      .map(ClubAccountInfoDto::new)
      .collect(Collectors.toList());

    return new ClubMembersDetailsDto(new ClubDto(club),
                                     clubAccountStatusDto,
                                     clubAccountsOfClub);
  }

  @Transactional(readOnly = true)
  public Page<ClubDto> getClubPage(Pageable pageable) {
    Page<Club> clubPage = clubRepository.findClubsWithPaging(pageable);
    List<ClubDto> clubDtoList = clubPage.stream().map(ClubDto::new).collect(Collectors.toList());
    return new PageImpl<>(clubDtoList, clubPage.getPageable(), clubPage.getTotalElements());
  }

  @Transactional
  public ClubAccount accountJoinToClub(Account account, Long clubId) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    List<ClubAccount> accountListOfClub = getClubAccountListOfClub(club);
    if (accountListOfClub.size() >= club.getLimitMemberCount()) {
      throw new RuntimeException("인원이 찬 클럽입니다.");
    }

    if (isJoinedInClub(account, accountListOfClub)) {
      throw new RuntimeException("이미 가입했습니다.");
    }

    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(club, account));
    roleRepository.findByRoleType(RoleType.MEMBER)
                  .ifPresent(
                    role -> clubAccountRoleRepository.save(new ClubAccountRole(role, clubAccount))
                  );
    club.incrementMemberCount();

    return clubAccount;
  }

  private List<ClubAccount> getClubAccountListOfClub(Club club) {
    return clubAccountRepository.findClubAccountsOfClub(club);
  }

  private boolean isJoinedInClub(Account account, List<ClubAccount> accountListOfClub) {
    return accountListOfClub.stream().anyMatch(clubAccount -> clubAccount.getAccount() == account);
  }

}
