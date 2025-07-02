package com.core.electionsystem.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.exception.InvalidArgumentsForElectorException;
import com.core.electionsystem.exception.InvalidArgumentsForSupervisorException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;

public class CustomizedUserDetails implements UserDetails {

  private transient Supervisor supervisor;
  private transient Elector elector;
  private final Role role;

  public CustomizedUserDetails(Supervisor supervisor, Role role) {
    if ((role != Role.SUPERVISOR) || (supervisor == null)) {
      throw new InvalidArgumentsForSupervisorException(ElectionSystemUtility.MESSAGE_FOR_INVALID_SUPERVISOR_ARGUMENTS_EXCEPTION);
    }
    this.supervisor = supervisor;
    this.role = role;
  }

  public CustomizedUserDetails(Elector elector, Role role) {
    if ((role != Role.ELECTOR) || (elector == null)) {
      throw new InvalidArgumentsForElectorException(ElectionSystemUtility.MESSAGE_FOR_INVALID_ELECTOR_ARGUMENTS_EXCEPTION);
    }
    this.elector = elector;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    if (Role.SUPERVISOR.equals(role)) {
      return supervisor.getPasswordHash();
    } else if (Role.ELECTOR.equals(role)) {
      return elector.getElectorCredentials().getPasswordHash();
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  @Override
  public String getUsername() {
    if (Role.SUPERVISOR.equals(role)) {
      return supervisor.getSupervisorEmail();
    } else if (Role.ELECTOR.equals(role)) {
      return elector.getElectorCredentials().getEmail();
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public Supervisor getSupervisor() {
    return this.supervisor;
  }

  public Elector getElector() {
    return this.elector;
  }

  public Role getRole() {
    return this.role;
  }
}
