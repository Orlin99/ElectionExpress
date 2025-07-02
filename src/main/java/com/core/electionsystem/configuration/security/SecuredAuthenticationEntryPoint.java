package com.core.electionsystem.configuration.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecuredAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    setRealm(response);
    response.getWriter().print(SecurityUtility.ERROR_MESSAGE_FOR_UNAUTHENTICATED_USER);
  }

  private void setRealm(HttpServletResponse response) {
    response.addHeader(HttpHeaders.WWW_AUTHENTICATE, SecurityUtility.RESPONSE_HEADER_VALUE);
  }
}
