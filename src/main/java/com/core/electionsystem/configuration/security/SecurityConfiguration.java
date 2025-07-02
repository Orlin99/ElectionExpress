package com.core.electionsystem.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.core.electionsystem.configuration.security.filter.JwtFilter;
import com.core.electionsystem.configuration.security.filter.SecondAuthenticationFactorFilter;
import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.service.CustomizedUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final CorsConfigurationSource corsConfigurationSource;
  private final JwtFilter jwtFilter;
  private final SecondAuthenticationFactorFilter secondAuthenticationFactorFilter;
  private final CustomizedUserDetailsService customizedUserDetailsService;
  private final SecuredLogoutHandler securedLogoutHandler;

  @Autowired
  public SecurityConfiguration(CorsConfigurationSource corsConfigurationSource, JwtFilter jwtFilter,
      SecondAuthenticationFactorFilter secondAuthenticationFactorFilter, CustomizedUserDetailsService customizedUserDetailsService,
      SecuredLogoutHandler securedLogoutHandler) {
    this.corsConfigurationSource = corsConfigurationSource;
    this.jwtFilter = jwtFilter;
    this.secondAuthenticationFactorFilter = secondAuthenticationFactorFilter;
    this.customizedUserDetailsService = customizedUserDetailsService;
    this.securedLogoutHandler = securedLogoutHandler;
  }

  // @formatter:off
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      BCryptPasswordEncoder passwordEncoder,
      SecuredAuthenticationEntryPoint entryPoint,
      SecuredAccessDeniedHandler handler) throws Exception {
    return httpSecurity
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(this::configureAuthorization)
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(customizedAuthenticationProvider(passwordEncoder))
        .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint).accessDeniedHandler(handler))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(secondAuthenticationFactorFilter, JwtFilter.class)
        .logout(logout -> logout
            .logoutUrl(SecurityUtility.LOGOUT_USER_ENDPOINT)
            .addLogoutHandler(securedLogoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
        .build();
  }

  private void configureAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
    request
    .requestMatchers(
        SecurityUtility.REGISTER_SUPERVISOR_ENDPOINT,
        SecurityUtility.LOGIN_SUPERVISOR_ENDPOINT,
        SecurityUtility.REMOVE_2FA_FOR_SUPERVISOR_ENDPOINT,
        SecurityUtility.REGISTER_ELECTOR_ENDPOINT,
        SecurityUtility.LOGIN_ELECTOR_ENDPOINT,
        SecurityUtility.REMOVE_2FA_FOR_ELECTOR_ENDPOINT,
        SecurityUtility.ALL_ENDPOINTS_OF_FORGOTTEN_PROPERTIES)
    .permitAll()
    .anyRequest()
    .authenticated();
  }
  // @formatter:on

  @Bean
  public AuthenticationProvider customizedAuthenticationProvider(BCryptPasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(customizedUserDetailsService);
    return provider;
  }
}
