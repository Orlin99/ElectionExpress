package com.core.electionsystem.configuration.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.core.electionsystem.configuration.security.utility.BufferedResponseWrapper;
import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.exception.NonExistentElectorException;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.service.SecondAuthenticationFactorService;
import com.core.electionsystem.supervisor.exception.NonExistentSupervisorException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecondAuthenticationFactorFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final SecondAuthenticationFactorService secondAuthenticationFactorService;
  private final SupervisorRepository supervisorRepository;
  private final SupervisorBearerTokenRepository supervisorBearerTokenRepository;
  private final ElectorRepository electorRepository;
  private final ElectorBearerTokenRepository electorBearerTokenRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public SecondAuthenticationFactorFilter(JwtService jwtService, SecondAuthenticationFactorService secondAuthenticationFactorService,
      SupervisorRepository supervisorRepository, SupervisorBearerTokenRepository supervisorBearerTokenRepository, ElectorRepository electorRepository,
      ElectorBearerTokenRepository electorBearerTokenRepository, ObjectMapper objectMapper) {
    this.jwtService = jwtService;
    this.secondAuthenticationFactorService = secondAuthenticationFactorService;
    this.supervisorRepository = supervisorRepository;
    this.supervisorBearerTokenRepository = supervisorBearerTokenRepository;
    this.electorRepository = electorRepository;
    this.electorBearerTokenRepository = electorBearerTokenRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
    return request.getRequestURI().startsWith(SecurityUtility.SHOULD_NOT_FILTER_REQUESTS_WITH_THIS_PREFIX);
  }

  @Override
  @SuppressWarnings("java:S3776")
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!SecurityUtility.isAuthenticated(authentication)) {
      filterChain.doFilter(request, response);
      return;
    }
    final String currentUserEmail = authentication.getName();
    final Role role = SecurityUtility.getRoleOfCurrentUser(authentication, jwtService);
    switch (role) {
      case SUPERVISOR:
        Supervisor supervisor = supervisorRepository.findSupervisorByEmail(currentUserEmail).orElseThrow(
            () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + currentUserEmail));
        if (secondAuthenticationFactorService.isSecondAuthenticationFactorRequiredForSupervisor(supervisor)) {
          if (SecurityUtility.isVerified2faClaim(request, jwtService)) {
            filterChain.doFilter(request, response);
            return;
          }
          final String inputTotpCode = request.getHeader(SecurityUtility.TIME_BASED_ONE_TIME_PASSWORD_HEADER_NAME);
          if (!SecurityUtility.isSupervisorInputTotpCodeAuthentic(inputTotpCode, secondAuthenticationFactorService, supervisor)) {
            response.setContentType(SecurityUtility.CONTENT_TYPE_APPLICATION_JSON);
            response.setCharacterEncoding(SecurityUtility.CHARACTER_ENCODING_UTF_8);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(SecurityUtility.MESSAGE_FOR_INVALID_OR_WRONG_2FA_TOTP_CODE);
            return;
          }
          SupervisorUtility.revokeAndDeleteAllSupervisorTokens(jwtService, supervisorBearerTokenRepository, supervisor);
          String newSupervisorToken = SecurityUtility.generateBuildAndStoreSupervisorToken(jwtService, currentUserEmail, role, supervisor,
              supervisorBearerTokenRepository);
          String wrappedJsonObjectResponse = objectMapper
              .writeValueAsString(Map.of(SecurityUtility.RESPONSE_DATA_TOKEN_KEY_ATTRIBUTE, newSupervisorToken));
          response.setContentType(SecurityUtility.CONTENT_TYPE_APPLICATION_JSON);
          response.setCharacterEncoding(SecurityUtility.CHARACTER_ENCODING_UTF_8);
          response.setStatus(HttpStatus.OK.value());
          response.getWriter().write(wrappedJsonObjectResponse);
          response.getWriter().flush();
          BufferedResponseWrapper bufferedResponseWrapper = new BufferedResponseWrapper(response);
          filterChain.doFilter(request, bufferedResponseWrapper);
          return;
        }
        filterChain.doFilter(request, response);
        break;
      case ELECTOR:
        Elector elector = electorRepository.findElectorByEmail(currentUserEmail).orElseThrow(
            () -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + currentUserEmail));
        if (secondAuthenticationFactorService.isSecondAuthenticationFactorRequiredForElector(elector)) {
          if (SecurityUtility.isVerified2faClaim(request, jwtService)) {
            filterChain.doFilter(request, response);
            return;
          }
          final String inputTotpCode = request.getHeader(SecurityUtility.TIME_BASED_ONE_TIME_PASSWORD_HEADER_NAME);
          if (!SecurityUtility.isElectorInputTotpCodeAuthentic(inputTotpCode, secondAuthenticationFactorService, elector)) {
            response.setContentType(SecurityUtility.CONTENT_TYPE_APPLICATION_JSON);
            response.setCharacterEncoding(SecurityUtility.CHARACTER_ENCODING_UTF_8);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(SecurityUtility.MESSAGE_FOR_INVALID_OR_WRONG_2FA_TOTP_CODE);
            return;
          }
          ElectorUtility.revokeAndDeleteAllElectorTokens(jwtService, electorBearerTokenRepository, elector);
          String newElectorToken = SecurityUtility.generateBuildAndStoreElectorToken(jwtService, currentUserEmail, role, elector,
              electorBearerTokenRepository);
          String wrappedJsonObjectResponse = objectMapper
              .writeValueAsString(Map.of(SecurityUtility.RESPONSE_DATA_TOKEN_KEY_ATTRIBUTE, newElectorToken));
          response.setContentType(SecurityUtility.CONTENT_TYPE_APPLICATION_JSON);
          response.setCharacterEncoding(SecurityUtility.CHARACTER_ENCODING_UTF_8);
          response.setStatus(HttpStatus.OK.value());
          response.getWriter().write(wrappedJsonObjectResponse);
          response.getWriter().flush();
          BufferedResponseWrapper bufferedResponseWrapper = new BufferedResponseWrapper(response);
          filterChain.doFilter(request, bufferedResponseWrapper);
          return;
        }
        filterChain.doFilter(request, response);
        break;
      default:
        throw new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
    }
  }
}
