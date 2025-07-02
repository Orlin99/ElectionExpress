package com.core.electionsystem.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.exception.NonExistentJsonWebTokenException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.utility.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecuredLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;
  private final SupervisorBearerTokenRepository supervisorTokenRepository;
  private final ElectorBearerTokenRepository electorTokenRepository;

  @Autowired
  public SecuredLogoutHandler(JwtService jwtService, SupervisorBearerTokenRepository supervisorTokenRepository,
      ElectorBearerTokenRepository electorTokenRepository) {
    this.jwtService = jwtService;
    this.supervisorTokenRepository = supervisorTokenRepository;
    this.electorTokenRepository = electorTokenRepository;
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authorizationHeader = request.getHeader(SecurityUtility.AUTHORIZATION_HEADER_NAME);
    if ((authorizationHeader == null) || !authorizationHeader.startsWith(SecurityUtility.AUTHENTICATION_HEADER_PREFIX)) {
      return;
    }
    final String jsonWebToken = authorizationHeader.substring(SecurityUtility.AUTHENTICATION_HEADER_PREFIX_LENGTH);
    final String userRole = jwtService.extractRole(jsonWebToken);
    if (Role.SUPERVISOR.getValue().equals(userRole)) {
      SupervisorBearerToken supervisorBearerToken = supervisorTokenRepository.findByTokenItself(jsonWebToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Supervisor supervisor = supervisorBearerToken.getSupervisor();
      jwtService.revokeAllSupervisorTokens(supervisorTokenRepository, supervisor);
      final Long supervisorId = supervisor.getId();
      supervisorTokenRepository.deleteAllInvalidTokensBySupervisor(supervisorId);
    } else if (Role.ELECTOR.getValue().equals(userRole)) {
      ElectorBearerToken electorBearerToken = electorTokenRepository.findByTokenItself(jsonWebToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Elector elector = electorBearerToken.getElector();
      jwtService.revokeAllElectorTokens(electorTokenRepository, elector);
      final String electorId = elector.getElectorId();
      electorTokenRepository.deleteAllInvalidTokensByElector(electorId);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }
}
