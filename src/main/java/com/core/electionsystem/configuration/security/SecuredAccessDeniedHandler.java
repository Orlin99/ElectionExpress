package com.core.electionsystem.configuration.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecuredAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    setRealm(response);
    response.getWriter().print(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
  }

  private void setRealm(HttpServletResponse response) {
    response.addHeader(HttpHeaders.WWW_AUTHENTICATE, SecurityUtility.RESPONSE_HEADER_VALUE);
  }
}
