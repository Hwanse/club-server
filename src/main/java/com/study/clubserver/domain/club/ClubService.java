package com.study.clubserver.domain.club;

import com.study.clubserver.api.dto.club.ClubAccountInfoDto;
import com.study.clubserver.api.dto.club.ClubAccountStatusDto;
import com.study.clubserver.api.dto.club.ClubCreateRequest;
import com.study.clubserver.api.dto.club.ClubDto;
import com.study.clubserver.api.dto.club.ClubMembersDetailsDto;
import com.study.clubserver.domain.account.Account;
import com.study.clubserver.domain.role.RoleRepository;
import com.study.clubserver.domain.role.RoleType;
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
  private final ClubAccontRoleRepository clubAccontRoleRepository;
  private final RoleRepository roleRepository;

  @Transactional
  public Club createClub(Account account, ClubCreateRequest request) {
    Club club = new Club(request);
    Club savedClub = clubRepository.save(club);

    // clubAccount
    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(savedClub, account));
    // clubAccountRole
    roleRepository.findByRoleType(RoleType.MANAGER)
                  .ifPresent(
                    role -> clubAccontRoleRepository.save(new ClubAccountRole(role, clubAccount))
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

  private boolean isContainsMember(Club club, Account account) {
    return clubAccountRepository.existsClubAccountByClubAndAccount(club, account);
  }

}
