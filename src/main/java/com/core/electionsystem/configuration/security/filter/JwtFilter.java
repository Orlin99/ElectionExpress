package com.core.electionsystem.configuration.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.service.CustomizedUserDetailsService;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomizedUserDetailsService customizedUserDetailsService;
  private final SupervisorBearerTokenRepository supervisorTokenRepository;
  private final ElectorBearerTokenRepository electorTokenRepository;

  @Autowired
  public JwtFilter(JwtService jwtService, CustomizedUserDetailsService customizedUserDetailsService,
      SupervisorBearerTokenRepository supervisorTokenRepository, ElectorBearerTokenRepository electorTokenRepository) {
    this.jwtService = jwtService;
    this.customizedUserDetailsService = customizedUserDetailsService;
    this.supervisorTokenRepository = supervisorTokenRepository;
    this.electorTokenRepository = electorTokenRepository;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader(SecurityUtility.AUTHORIZATION_HEADER_NAME);
    if ((authorizationHeader == null) || !authorizationHeader.startsWith(SecurityUtility.AUTHENTICATION_HEADER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }
    final String jsonWebToken = authorizationHeader.substring(SecurityUtility.AUTHENTICATION_HEADER_PREFIX_LENGTH);
    final String userEmail = jwtService.extractUsername(jsonWebToken);
    final String userRole = jwtService.extractRole(jsonWebToken);
    if ((userEmail != null) && (userRole != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
      UserDetails userDetails = SecurityUtility.getUserDetailsByRole(userRole, userEmail, customizedUserDetailsService);
      boolean isTokenValid = jwtService.isTokenValid(jsonWebToken, userDetails);
      boolean isTokenValidFromDatabase = jwtService.isTokenValidFromDatabase(supervisorTokenRepository, electorTokenRepository, jsonWebToken);
      if ((userDetails != null) && isTokenValid && isTokenValidFromDatabase) {
        SecurityUtility.setUpAuthentication(userDetails, request);
      }
    }
    filterChain.doFilter(request, response);
  }
}
