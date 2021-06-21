package com.study.clubserver.domain.role;

public enum RoleType {
  USER("회원", RoleName.USER), ADMIN("관리자", RoleName.ADMIN),
  MEMBER("모임원", RoleName.MEMBER), MANAGER("모임장", RoleName.MANAGER);

  private String label;
  private String roleName;

  RoleType(String label, String roleName) {
    this.label = label;
    this.roleName = roleName;
  }

  public String getLabel() {
    return label;
  }

  public String getRoleName() {
    return roleName;
  }

  static class RoleName {
    public static final String MEMBER = "ROLE_MEMBER";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
  }

}
