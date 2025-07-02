package com.core.electionsystem.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;

@Configuration
public class EmailConfiguration {

  @Value("${spring.mail.default}")
  private String defaultEmailProvider;

  @Value("${spring.mail.gmail.host}")
  private String gmailHost;

  @Value("${spring.mail.gmail.port}")
  private int gmailPort;

  @Value("${spring.mail.gmail.username}")
  private String gmailUserEmail;

  @Value("${spring.mail.gmail.password}")
  private String gmailUserPassword;

  @Value("${spring.mail.abv.host}")
  private String abvHost;

  @Value("${spring.mail.abv.port}")
  private int abvPort;

  @Value("${spring.mail.abv.username}")
  private String abvUserEmail;

  @Value("${spring.mail.abv.password}")
  private String abvUserPassword;

  @Bean
  public JavaMailSender javaMailSender() {
    String defaultProvider = defaultEmailProvider.toLowerCase();
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    Properties properties = new Properties();

    if (SecurityUtility.GMAIL_AS_DEFAULT_EMAIL_PROVIDER.equals(defaultProvider)) {
      javaMailSender.setHost(gmailHost);
      javaMailSender.setPort(gmailPort);
      javaMailSender.setUsername(gmailUserEmail);
      javaMailSender.setPassword(gmailUserPassword);
      properties.put(SecurityUtility.MAIL_SMTP_AUTHENTICATION, true);
      properties.put(SecurityUtility.MAIL_SMTP_STARTTLS_ENABLE, true);

    } else if (SecurityUtility.ABV_AS_DEFAULT_EMAIL_PROVIDER.equals(defaultProvider)) {
      javaMailSender.setHost(abvHost);
      javaMailSender.setPort(abvPort);
      javaMailSender.setUsername(abvUserEmail);
      javaMailSender.setPassword(abvUserPassword);
      properties.put(SecurityUtility.MAIL_SMTP_AUTHENTICATION, true);
      properties.put(SecurityUtility.MAIL_SMTP_SSL_ENABLE, true);
    }

    javaMailSender.setJavaMailProperties(properties);
    return javaMailSender;
  }
}
