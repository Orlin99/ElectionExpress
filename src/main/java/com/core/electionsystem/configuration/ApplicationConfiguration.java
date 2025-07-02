package com.core.electionsystem.configuration;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ApplicationConfiguration {

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(SecurityUtility.BCRYPT_PASSWORD_HASHING_ROUNDS);
  }

  @Bean
  public TimeBasedOneTimePasswordGenerator totpGenerator() {
    return new TimeBasedOneTimePasswordGenerator();
  }

  @Bean
  public SecureRandom secureRandom() {
    return new SecureRandom();
  }

  @Bean
  public Base32 base32() {
    return new Base32();
  }
}
