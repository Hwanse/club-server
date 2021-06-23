package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.api.dto.club.ClubAccountStatusDto;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.club.ClubDto;
import com.study.clubserver.api.dto.club.ClubMembersDetailsDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.interest.ClubInterest;
import com.study.clubserver.domain.interest.ClubInterestRepository;
import com.study.clubserver.domain.interest.InterestRepository;
import com.study.clubserver.domain.role.RoleRepository;
import com.study.clubserver.domain.role.RoleType;
import com.study.clubserver.domain.zone.ClubZone;
import com.study.clubserver.domain.zone.ClubZoneRepository;
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
@Transactional(readOnly = true)
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

  public ClubMembersDetailsDto getClubDetails(Long clubId, Account account) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    if (! isContainsMember(club, account)) {
      throw new IllegalArgumentException();
    }

    // 현재 인증 유저의 상세 정보
    ClubAccountStatusDto clubAccountStatusDto = clubAccountService
      .getClubAccountDetailWithRole(club, account);

    // 클럽의 구성원 리스트
    List<ClubAccountInfoDto> clubAccountsOfClub = clubAccountRepository
      .findClubAccountsOfClub(club)
      .stream()
      .map(ClubAccountInfoDto::new)
      .collect(Collectors.toList());

    return new ClubMembersDetailsDto(new ClubDto(club),
                                     clubAccountStatusDto,
                                     clubAccountsOfClub);
  }

  public Page<ClubDto> getClubPage(Pageable pageable) {
    Page<Club> clubPage = clubRepository.findClubsWithPaging(pageable);
    List<ClubDto> clubDtos = clubPage.stream().map(ClubDto::new).collect(Collectors.toList());
    return new PageImpl<>(clubDtos, clubPage.getPageable(), clubPage.getTotalElements());
  }

  public ClubAccount accountJoinToClub(Account account, Long clubId) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    if (isContainsMember(club, account)) {
      throw new RuntimeException("이미 가입했습니다..");
    }

    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(club, account));
    roleRepository.findByRoleType(RoleType.MEMBER)
                  .ifPresent(
                    role -> clubAccountRoleRepository.save(new ClubAccountRole(role, clubAccount))
                  );

    return clubAccount;
  }

  private boolean isContainsMember(Club club, Account account) {
    return clubAccountRepository.existsClubAccountByClubAndAccount(club, account);
  }

}
