package com.core.electionsystem.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.dto.recovery.PasswordRecovererDTO;
import com.core.electionsystem.dto.recovery.PasswordUpdaterDTO;
import com.core.electionsystem.dto.recovery.SecretAnswerRecovererDTO;
import com.core.electionsystem.dto.recovery.SecretAnswerUpdaterDTO;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.IncorrectSecretAnswerException;
import com.core.electionsystem.exception.IncorrectValueForPhoneNumberException;
import com.core.electionsystem.exception.IncorrectValueForUserIdException;
import com.core.electionsystem.exception.InvalidValueForUserIdException;
import com.core.electionsystem.exception.NonExistentJsonWebTokenException;
import com.core.electionsystem.exception.NonExistentUserException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.supervisor.exception.InvalidValueForSupervisorIdException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.EmailUtility;
import com.core.electionsystem.utility.Role;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@SuppressWarnings("java:S3655")
public class ForgottenPropertiesService {

  private final SupervisorRepository supervisorRepository;
  private final ElectorRepository electorRepository;
  private final SupervisorBearerTokenRepository supervisorBearerTokenRepository;
  private final ElectorBearerTokenRepository electorBearerTokenRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder encoder;
  private final JavaMailSender javaMailSender;

  @Autowired
  public ForgottenPropertiesService(SupervisorRepository supervisorRepository, ElectorRepository electorRepository,
      SupervisorBearerTokenRepository supervisorBearerTokenRepository, ElectorBearerTokenRepository electorBearerTokenRepository,
      JwtService jwtService, BCryptPasswordEncoder encoder, JavaMailSender javaMailSender) {
    this.supervisorRepository = supervisorRepository;
    this.electorRepository = electorRepository;
    this.supervisorBearerTokenRepository = supervisorBearerTokenRepository;
    this.electorBearerTokenRepository = electorBearerTokenRepository;
    this.jwtService = jwtService;
    this.encoder = encoder;
    this.javaMailSender = javaMailSender;
  }

  public void validateInputDataForSecretAnswerRecovery(SecretAnswerRecovererDTO secretAnswerRecovererDTO) {
    final String inputEmail = secretAnswerRecovererDTO.getEmail();
    ElectionSystemUtility.validateEmail(inputEmail);
    final boolean isSupervisor = supervisorRepository.findSupervisorByEmail(inputEmail).isPresent();
    final boolean isElector = electorRepository.findElectorByEmail(inputEmail).isPresent();
    if (isSupervisor) {
      Supervisor supervisor = supervisorRepository.findSupervisorByEmail(inputEmail).get();
      final char[] inputUserId = secretAnswerRecovererDTO.getUserId();
      validateSupervisorId(supervisor, inputUserId);
      final String inputPhoneNumber = secretAnswerRecovererDTO.getPhoneNumber();
      ElectionSystemUtility.validatePhoneNumber(inputPhoneNumber);
      final String supervisorPhoneNumber = supervisor.getSupervisorPhone();
      if (!inputPhoneNumber.equals(supervisorPhoneNumber)) {
        throw new IncorrectValueForPhoneNumberException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_PHONE_NUMBER);
      }
    } else if (isElector) {
      Elector elector = electorRepository.findElectorByEmail(inputEmail).get();
      final char[] inputUserId = secretAnswerRecovererDTO.getUserId();
      validateElectorId(elector, inputUserId);
      final String inputPhoneNumber = secretAnswerRecovererDTO.getPhoneNumber();
      ElectionSystemUtility.validatePhoneNumber(inputPhoneNumber);
      final String electorPhoneNumber = elector.getElectorCredentials().getPhoneNumber();
      if (!inputPhoneNumber.equals(electorPhoneNumber)) {
        throw new IncorrectValueForPhoneNumberException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_PHONE_NUMBER);
      }
    } else {
      throw new NonExistentUserException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + inputEmail);
    }
  }

  @Transactional
  public void patchForgottenSecretAnswer(SecretAnswerUpdaterDTO secretAnswerUpdaterDTO, String recoveryToken, char[] userIdFromRecoveryToken) {
    final String newSecretAnswer = secretAnswerUpdaterDTO.getNewSecretAnswerFirstInput();
    final boolean secretAnswersAreEqual = newSecretAnswer.equals(secretAnswerUpdaterDTO.getNewSecretAnswerSecondInput());
    if (!secretAnswersAreEqual) {
      throw new IncorrectSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_MISMATCH_SECRET_ANSWER);
    }
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    final String userIdFromRecoveryTokenToString = String.valueOf(userIdFromRecoveryToken);
    SecurityUtility.clearArray(userIdFromRecoveryToken);
    if (role == Role.SUPERVISOR) {
      try {
        final Long userIdFromRecoveryTokenToLong = Long.parseLong(userIdFromRecoveryTokenToString);
        Supervisor supervisor = supervisorRepository.findById(userIdFromRecoveryTokenToLong).get();
        final String hashedSecretAnswer = SecurityUtility.hashSecretAnswer(newSecretAnswer, encoder);
        supervisor.setSecretAnswer(hashedSecretAnswer);
        supervisorRepository.save(supervisor);
      } catch (NumberFormatException exception) {
        throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
      }
    } else if (role == Role.ELECTOR) {
      Elector elector = electorRepository.findById(userIdFromRecoveryTokenToString).get();
      final String hashedSecretAnswer = SecurityUtility.hashSecretAnswer(newSecretAnswer, encoder);
      elector.getElectorCredentials().setSecretAnswer(hashedSecretAnswer);
      electorRepository.save(elector);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  public void validateInputDataForPasswordRecovery(PasswordRecovererDTO passwordRecovererDTO) {
    final String inputEmail = passwordRecovererDTO.getEmail();
    ElectionSystemUtility.validateEmail(inputEmail);
    final boolean isSupervisor = supervisorRepository.findSupervisorByEmail(inputEmail).isPresent();
    final boolean isElector = electorRepository.findElectorByEmail(inputEmail).isPresent();
    if (isSupervisor) {
      Supervisor supervisor = supervisorRepository.findSupervisorByEmail(inputEmail).get();
      final char[] inputUserId = passwordRecovererDTO.getUserId();
      validateSupervisorId(supervisor, inputUserId);
      final String inputPhoneNumber = passwordRecovererDTO.getPhoneNumber();
      ElectionSystemUtility.validatePhoneNumber(inputPhoneNumber);
      final String supervisorPhoneNumber = supervisor.getSupervisorPhone();
      if (!inputPhoneNumber.equals(supervisorPhoneNumber)) {
        throw new IncorrectValueForPhoneNumberException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_PHONE_NUMBER);
      }
      final String inputSecretAnswer = passwordRecovererDTO.getSecretAnswer();
      final String secretAnswer = supervisor.getSecretAnswer();
      SecurityUtility.validateSecretAnswer(inputSecretAnswer, secretAnswer, encoder);
    } else if (isElector) {
      Elector elector = electorRepository.findElectorByEmail(inputEmail).get();
      final char[] inputUserId = passwordRecovererDTO.getUserId();
      validateElectorId(elector, inputUserId);
      final String inputPhoneNumber = passwordRecovererDTO.getPhoneNumber();
      ElectionSystemUtility.validatePhoneNumber(inputPhoneNumber);
      final String electorPhoneNumber = elector.getElectorCredentials().getPhoneNumber();
      if (!inputPhoneNumber.equals(electorPhoneNumber)) {
        throw new IncorrectValueForPhoneNumberException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_PHONE_NUMBER);
      }
      final String inputSecretAnswer = passwordRecovererDTO.getSecretAnswer();
      final String secretAnswer = elector.getElectorCredentials().getSecretAnswer();
      SecurityUtility.validateSecretAnswer(inputSecretAnswer, secretAnswer, encoder);
    } else {
      throw new NonExistentUserException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + inputEmail);
    }
  }

  @Transactional
  public void patchForgottenPassword(PasswordUpdaterDTO passwordUpdaterDTO, String recoveryToken, char[] userIdFromRecoveryToken) {
    final char[] inputPassword = passwordUpdaterDTO.getNewPasswordFirstInput();
    SecurityUtility.validatePassword(inputPassword);
    final boolean passwordsAreEqual = Arrays.equals(inputPassword, passwordUpdaterDTO.getNewPasswordSecondInput());
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_PASSWORD_VALUE);
    }
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    final String userIdFromRecoveryTokenToString = String.valueOf(userIdFromRecoveryToken);
    SecurityUtility.clearArray(userIdFromRecoveryToken);
    if (role == Role.SUPERVISOR) {
      try {
        final Long userIdFromRecoveryTokenToLong = Long.parseLong(userIdFromRecoveryTokenToString);
        Supervisor supervisor = supervisorRepository.findById(userIdFromRecoveryTokenToLong).get();
        final String newPasswordHash = SecurityUtility.hashPassword(inputPassword, encoder);
        supervisor.setPasswordHash(newPasswordHash);
        supervisorRepository.save(supervisor);
      } catch (NumberFormatException exception) {
        throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
      }
    } else if (role == Role.ELECTOR) {
      Elector elector = electorRepository.findById(userIdFromRecoveryTokenToString).get();
      final String newPasswordHash = SecurityUtility.hashPassword(inputPassword, encoder);
      elector.getElectorCredentials().setPasswordHash(newPasswordHash);
      electorRepository.save(elector);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  public char[] generateRecoveryToken(String validatedUserEmail) {
    final boolean isSupervisor = supervisorRepository.findSupervisorByEmail(validatedUserEmail).isPresent();
    final boolean isElector = electorRepository.findElectorByEmail(validatedUserEmail).isPresent();
    if (isSupervisor) {
      final Role role = Role.SUPERVISOR;
      final String supervisorRecoveryTokenValue = jwtService.generateRecoveryToken(validatedUserEmail, role);
      Supervisor supervisor = supervisorRepository.findSupervisorByEmail(validatedUserEmail).get();
      SupervisorBearerToken supervisorRecoveryToken = jwtService.buildSupervisorToken(supervisor, supervisorRecoveryTokenValue);
      supervisorBearerTokenRepository.save(supervisorRecoveryToken);
      return supervisorRecoveryTokenValue.toCharArray();
    } else if (isElector) {
      final Role role = Role.ELECTOR;
      final String electorRecoveryTokenValue = jwtService.generateRecoveryToken(validatedUserEmail, role);
      Elector elector = electorRepository.findElectorByEmail(validatedUserEmail).get();
      ElectorBearerToken electorRecoveryToken = jwtService.buildElectorToken(elector, electorRecoveryTokenValue);
      electorBearerTokenRepository.save(electorRecoveryToken);
      return electorRecoveryTokenValue.toCharArray();
    } else {
      throw new NonExistentUserException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + validatedUserEmail);
    }
  }

  public void sendSecretAnswerRecoveryToken(String validatedUserEmail, char[] recoveryToken) throws MessagingException {
    MimeMessage recoveryTokenMimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper recoveryTokenHelper = new MimeMessageHelper(recoveryTokenMimeMessage, true, EmailUtility.DEFAULT_MIME_MESSAGE_EMAIL_ENCODING);
    recoveryTokenHelper.setSubject(EmailUtility.SUBJECT_OF_THE_EMAIL_FOR_SECRET_ANSWER_RECOVERY);
    recoveryTokenHelper.setFrom(EmailUtility.DEFAULT_APPLICATION_ADMIN_GMAIL_COM_EMAIL);
    recoveryTokenHelper.setTo(validatedUserEmail);
    final String urlForSecretAnswerRecovery = EmailUtility.buildUrlForSecretAnswerRecovery(recoveryToken);
    recoveryTokenHelper.setText(EmailUtility.generateEmailContentForSecretAnswerRecovery(validatedUserEmail, urlForSecretAnswerRecovery), true);
    javaMailSender.send(recoveryTokenMimeMessage);
  }

  public void sendPasswordRecoveryToken(String validatedUserEmail, char[] recoveryToken) throws MessagingException {
    MimeMessage recoveryTokenMimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper recoveryTokenHelper = new MimeMessageHelper(recoveryTokenMimeMessage, true, EmailUtility.DEFAULT_MIME_MESSAGE_EMAIL_ENCODING);
    recoveryTokenHelper.setSubject(EmailUtility.SUBJECT_OF_THE_EMAIL_FOR_PASSWORD_RECOVERY);
    recoveryTokenHelper.setFrom(EmailUtility.DEFAULT_APPLICATION_ADMIN_GMAIL_COM_EMAIL);
    recoveryTokenHelper.setTo(validatedUserEmail);
    final String urlForPasswordRecovery = EmailUtility.buildUrlForPasswordRecovery(recoveryToken);
    recoveryTokenHelper.setText(EmailUtility.generateEmailContentForPasswordRecovery(validatedUserEmail, urlForPasswordRecovery), true);
    javaMailSender.send(recoveryTokenMimeMessage);
  }

  public char[] validateRecoveryTokenAndGetUserId(String recoveryToken) {
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    if (role == Role.SUPERVISOR) {
      SupervisorBearerToken supervisorRecoveryToken = supervisorBearerTokenRepository.findByTokenItself(recoveryToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Supervisor supervisor = supervisorRecoveryToken.getSupervisor();
      final String supervisorIdAsString = String.valueOf(supervisor.getId());
      return supervisorIdAsString.toCharArray();
    } else if (role == Role.ELECTOR) {
      ElectorBearerToken electorRecoveryToken = electorBearerTokenRepository.findByTokenItself(recoveryToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Elector elector = electorRecoveryToken.getElector();
      final String electorId = elector.getElectorId();
      return electorId.toCharArray();
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  public void validateUserIdByRecoveryToken(String recoveryToken, char[] userIdFromRecoveryToken, char[] inputUserId) {
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    final String userIdFromRecoveryTokenToString = String.valueOf(userIdFromRecoveryToken);
    if (role == Role.SUPERVISOR) {
      try {
        final Long userIdFromRecoveryTokenToLong = Long.parseLong(userIdFromRecoveryTokenToString);
        final Supervisor supervisor = supervisorRepository.findById(userIdFromRecoveryTokenToLong).get();
        validateSupervisorId(supervisor, inputUserId);
      } catch (NumberFormatException exception) {
        throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
      }
    } else if (role == Role.ELECTOR) {
      final Elector elector = electorRepository.findById(userIdFromRecoveryTokenToString).get();
      validateElectorId(elector, inputUserId);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  public void validateSecretAnswerForPasswordRecovery(String recoveryToken, char[] userIdFromRecoveryToken, PasswordUpdaterDTO passwordUpdaterDTO) {
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    final String userIdFromRecoveryTokenToString = String.valueOf(userIdFromRecoveryToken);
    final String inputSecretAnswer = passwordUpdaterDTO.getSecretAnswer();
    if (role == Role.SUPERVISOR) {
      try {
        final Long userIdFromRecoveryTokenToLong = Long.parseLong(userIdFromRecoveryTokenToString);
        final Supervisor supervisor = supervisorRepository.findById(userIdFromRecoveryTokenToLong).get();
        final String secretAnswer = supervisor.getSecretAnswer();
        SecurityUtility.validateSecretAnswer(inputSecretAnswer, secretAnswer, encoder);
      } catch (NumberFormatException exception) {
        throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
      }
    } else if (role == Role.ELECTOR) {
      final Elector elector = electorRepository.findById(userIdFromRecoveryTokenToString).get();
      final String secretAnswer = elector.getElectorCredentials().getSecretAnswer();
      SecurityUtility.validateSecretAnswer(inputSecretAnswer, secretAnswer, encoder);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  @Transactional
  public void invalidateAndDeleteAllUserTokens(String recoveryToken) {
    final Role role = extractRoleFromRecoveryToken(recoveryToken);
    if (role == Role.SUPERVISOR) {
      SupervisorBearerToken supervisorRecoveryToken = supervisorBearerTokenRepository.findByTokenItself(recoveryToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Supervisor supervisor = supervisorRecoveryToken.getSupervisor();
      jwtService.revokeAllSupervisorTokens(supervisorBearerTokenRepository, supervisor);
      final Long supervisorId = supervisor.getId();
      supervisorBearerTokenRepository.deleteAllInvalidTokensBySupervisor(supervisorId);
    } else if (role == Role.ELECTOR) {
      ElectorBearerToken electorRecoveryToken = electorBearerTokenRepository.findByTokenItself(recoveryToken)
          .orElseThrow(() -> new NonExistentJsonWebTokenException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_JSON_WEB_TOKEN_EXCEPTION));
      Elector elector = electorRecoveryToken.getElector();
      jwtService.revokeAllElectorTokens(electorBearerTokenRepository, elector);
      final String electorId = elector.getElectorId();
      electorBearerTokenRepository.deleteAllInvalidTokensByElector(electorId);
    } else {
      throw new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION);
    }
  }

  private Role extractRoleFromRecoveryToken(String recoveryToken) {
    final String roleAsString = jwtService.extractRole(recoveryToken);
    return Role.fromValue(roleAsString);
  }

  private void validateSupervisorId(Supervisor supervisor, char[] inputUserId) {
    validateIdLegality(inputUserId);
    final String inputUserIdToString = String.valueOf(inputUserId);
    SecurityUtility.clearArray(inputUserId);
    try {
      final Long inputUserIdToLong = Long.parseLong(inputUserIdToString);
      if ((inputUserIdToLong < SupervisorUtility.SUPERVISOR_STARTING_ID_NUMBER)
          || (inputUserIdToLong > SupervisorUtility.NUMBER_OF_MUNICIPALITIES_IN_BULGARIA_PLUS_TOLERANCE)) {
        throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
      }
      final Long supervisorId = supervisor.getId();
      if (!inputUserIdToLong.equals(supervisorId)) {
        throw new IncorrectValueForUserIdException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_USER_ID);
      }
    } catch (NumberFormatException exception) {
      throw new InvalidValueForSupervisorIdException(SupervisorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID);
    }
  }

  private void validateElectorId(Elector elector, char[] inputUserId) {
    validateIdLegality(inputUserId);
    final String inputUserIdToString = String.valueOf(inputUserId);
    SecurityUtility.clearArray(inputUserId);
    ElectorUtility.validateEGN(inputUserIdToString);
    final String electorId = elector.getElectorId();
    if (!inputUserIdToString.equals(electorId)) {
      throw new IncorrectValueForUserIdException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_VALUE_FOR_USER_ID);
    }
  }

  private void validateIdLegality(char[] inputUserId) {
    if ((inputUserId == null) || (inputUserId.length == ElectionSystemUtility.EMPTY_ARRAY_LENGTH)) {
      throw new InvalidValueForUserIdException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_USER_ID);
    }
  }
}
