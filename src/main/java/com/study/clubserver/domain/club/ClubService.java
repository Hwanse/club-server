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

    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(savedClub, account));

    roleRepository
      .findByRoleType(RoleType.MANAGER)
      .ifPresent(role -> clubAccountRoleRepository.save(new ClubAccountRole(role, clubAccount)));

    List<ClubInterest> clubInterests = interestRepository
      .findInterestsByIdList(request.getInterestList()).stream()
      .map(interest -> new ClubInterest(savedClub, interest))
      .collect(Collectors.toList());
    clubInterestRepository.saveAll(clubInterests);

    List<ClubZone> clubZones = zoneRepository
      .findZonesByIdList(request.getZoneList()).stream()
      .map(zone -> new ClubZone(savedClub, zone))
      .collect(Collectors.toList());
    clubZoneRepository.saveAll(clubZones);

    savedClub.incrementMemberCount();

    return savedClub;
  }

  @Transactional(readOnly = true)
  public ClubMembersDetailsDto getClubDetails(Long clubId, Account account) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    List<ClubAccount> accountListOfClub = getClubAccountListOfClub(club);

    if (!isJoinedInClub(account, accountListOfClub)) {
      throw new IllegalArgumentException("????????? ???????????? ?????? ?????? ???????????????");
    }

    // ?????? ?????? ????????? ?????? ??????
    ClubAccountStatusDto clubAccountStatusDto = clubAccountService
      .getClubAccountDetailWithRole(club, account);

    // ????????? ????????? ?????????
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
  public void accountJoinToClub(Account account, Long clubId) {
    Club club = clubRepository.findById(clubId).orElseThrow(IllegalArgumentException::new);
    List<ClubAccount> accountListOfClub = getClubAccountListOfClub(club);

    if (accountListOfClub.size() >= club.getLimitMemberCount()) {
      throw new RuntimeException("????????? ??? ???????????????.");
    }

    if (isJoinedInClub(account, accountListOfClub)) {
      throw new RuntimeException("?????? ??????????????????.");
    }

    ClubAccount clubAccount = clubAccountRepository.save(new ClubAccount(club, account));
    roleRepository
      .findByRoleType(RoleType.MEMBER)
      .ifPresent(role -> clubAccountRoleRepository.save(new ClubAccountRole(role, clubAccount)));

    club.incrementMemberCount();

  }

  private List<ClubAccount> getClubAccountListOfClub(Club club) {
    return clubAccountRepository.findClubAccountsOfClub(club);
  }

  private boolean isJoinedInClub(Account account, List<ClubAccount> accountListOfClub) {
    return accountListOfClub.stream().anyMatch(clubAccount -> clubAccount.getAccount() == account);
  }

}
