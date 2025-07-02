package com.core.electionsystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.exception.NonExistentUserException;
import com.core.electionsystem.model.CustomizedUserDetails;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;

@Service
public class CustomizedUserDetailsService implements UserDetailsService {

  private final SupervisorRepository supervisorRepository;
  private final ElectorRepository electorRepository;

  @Autowired
  public CustomizedUserDetailsService(SupervisorRepository supervisorRepository, ElectorRepository electorRepository) {
    this.supervisorRepository = supervisorRepository;
    this.electorRepository = electorRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    Optional<Supervisor> locatedSupervisorByEmail = supervisorRepository.findSupervisorByEmail(email);
    if (locatedSupervisorByEmail.isPresent()) {
      return new CustomizedUserDetails(locatedSupervisorByEmail.get(), Role.SUPERVISOR);
    }
    Optional<Elector> locatedElectorByEmail = electorRepository.findElectorByEmail(email);
    if (locatedElectorByEmail.isPresent()) {
      return new CustomizedUserDetails(locatedElectorByEmail.get(), Role.ELECTOR);
    }
    throw new NonExistentUserException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email);
  }
}
