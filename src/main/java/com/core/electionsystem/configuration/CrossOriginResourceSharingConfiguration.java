package com.core.electionsystem.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CrossOriginResourceSharingConfiguration {

  private static final String ALLOWED_ORIGINS = "http://localhost:5173";
  private static final String GET = "GET";
  private static final String POST = "POST";
  private static final String PUT = "PUT";
  private static final String PATCH = "PATCH";
  private static final String DELETE = "DELETE";
  private static final String OPTIONS = "OPTIONS";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String TIME_BASED_ONE_TIME_PASSWORD_HEADER = "X-2FA-OTP";
  private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
  private static final long MAX_AGE_ONE_HOUR = 3600L;
  private static final String PATTERN_FOR_ALL_FRONT_END_API_ENDPOINTS = "/**";

  @Bean
  @Primary
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
    corsConfiguration.setAllowedMethods(List.of(GET, POST, PUT, PATCH, DELETE, OPTIONS));
    corsConfiguration
        .setAllowedHeaders(List.of(AUTHORIZATION_HEADER, CONTENT_TYPE_HEADER, TIME_BASED_ONE_TIME_PASSWORD_HEADER, ACCESS_CONTROL_ALLOW_HEADERS));
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setMaxAge(MAX_AGE_ONE_HOUR);
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration(PATTERN_FOR_ALL_FRONT_END_API_ENDPOINTS, corsConfiguration);
    return urlBasedCorsConfigurationSource;
  }
}
