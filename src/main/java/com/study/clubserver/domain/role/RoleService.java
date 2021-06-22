package com.study.clubserver.domain.role;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  public Role addRole(RoleType roleType) {
    Optional<Role> optionalRole = roleRepository.findByRoleType(roleType);
    if (optionalRole.isEmpty()) {
      return roleRepository.save(new Role(roleType));
    } else {
      return optionalRole.get();
    }
  }

}
