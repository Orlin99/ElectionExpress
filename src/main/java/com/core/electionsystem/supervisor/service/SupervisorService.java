package com.core.electionsystem.supervisor.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.dto.LoginUserDTO;
import com.core.electionsystem.dto.PasswordHolderDTO;
import com.core.electionsystem.dto.PhoneNumberHolderDTO;
import com.core.electionsystem.dto.SecretAnswerHolderDTO;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.IncorrectSecretAnswerException;
import com.core.electionsystem.exception.UnauthenticatedUserException;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.dto.SupervisorDTO;
import com.core.electionsystem.supervisor.exception.NonExistentSupervisorException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class SupervisorService {

  private final SupervisorRepository supervisorRepository;
  private final ElectorRepository electorRepository;
  private final SupervisorBearerTokenRepository supervisorBearerTokenRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final ObjectMapper objectMapper;
  private final BCryptPasswordEncoder encoder;

  @Autowired
  public SupervisorService(SupervisorRepository supervisorRepository, ElectorRepository electorRepository,
      SupervisorBearerTokenRepository supervisorBearerTokenRepository, AuthenticationManager authenticationManager, JwtService jwtService,
      ObjectMapper objectMapper, BCryptPasswordEncoder encoder) {
    this.supervisorRepository = supervisorRepository;
    this.electorRepository = electorRepository;
    this.supervisorBearerTokenRepository = supervisorBearerTokenRepository;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.objectMapper = objectMapper;
    this.encoder = encoder;
  }

  public void registerNewSupervisor(SupervisorDTO supervisorDTO) {
    SupervisorUtility.validateAndHashCredentials(supervisorDTO, encoder);
    final String email = supervisorDTO.getSupervisorEmail();
    ElectionSystemUtility.validateEmail(email);
    ElectionSystemUtility.validateEmailIsUnique(supervisorRepository, electorRepository, email);
    final String phoneNumber = supervisorDTO.getSupervisorPhone();
    ElectionSystemUtility.validatePhoneNumber(phoneNumber);
    ElectionSystemUtility.validatePhoneNumberIsUnique(supervisorRepository, electorRepository, phoneNumber);
    final Supervisor supervisor = SupervisorUtility.toEntity(supervisorDTO);
    supervisorRepository.save(supervisor);
  }

  public Map<String, Object> loginExistingSupervisor(LoginUserDTO loginUserDTO) {
    final String email = loginUserDTO.getEmail();
    ElectionSystemUtility.validateEmail(email);
    final boolean isSupervisorPresentByEmail = supervisorRepository.findSupervisorByEmail(email).isPresent();
    if (!isSupervisorPresentByEmail) {
      throw new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email);
    }
    Authentication authentication = SecurityUtility.authenticateUser(authenticationManager, loginUserDTO);
    if (!authentication.isAuthenticated()) {
      throw new UnauthenticatedUserException(SecurityUtility.ERROR_MESSAGE_FOR_UNAUTHENTICATED_USER);
    }
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(email)
        .orElseThrow(() -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email));
    SupervisorUtility.revokeAndDeleteAllSupervisorTokens(jwtService, supervisorBearerTokenRepository, supervisor);
    final Role role = Role.SUPERVISOR;
    final String supervisorBearerTokenValue = jwtService.generateToken(email, role);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(ElectionSystemUtility.LOGIN_RESPONSE_MESSAGE_ATTRIBUTE, ElectionSystemUtility.SUCCESSFULLY_LOGGED_IN_USER_RESPONSE);
    responseMap.put(SupervisorUtility.SUPERVISOR_BEARER_TOKEN_FOR_THE_RESPONSE, supervisorBearerTokenValue);
    final SupervisorBearerToken bearerToken = jwtService.buildSupervisorToken(supervisor, supervisorBearerTokenValue);
    supervisorBearerTokenRepository.save(bearerToken);
    return responseMap;
  }

  public String getSupervisorById(Long id) throws JsonProcessingException {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Supervisor supervisor = supervisorRepository.findById(id)
        .orElseThrow(() -> new NonExistentSupervisorException(SupervisorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION + id));
    SupervisorDTO supervisorDTO = SupervisorUtility.toDTO(supervisor);
    return SupervisorUtility.fetchSupervisorData(objectMapper, supervisorDTO);
  }

  public String getSupervisorByEmail(String supervisorEmail) throws JsonProcessingException {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    ElectionSystemUtility.validateEmail(supervisorEmail);
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(supervisorEmail).orElseThrow(
        () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + supervisorEmail));
    SupervisorDTO supervisorDTO = SupervisorUtility.toDTO(supervisor);
    return SupervisorUtility.fetchSupervisorData(objectMapper, supervisorDTO);
  }

  public String getSupervisorByPhoneNumber(String supervisorPhone) throws JsonProcessingException {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    ElectionSystemUtility.validatePhoneNumber(supervisorPhone);
    Supervisor supervisor = supervisorRepository.findSupervisorByPhoneNumber(supervisorPhone).orElseThrow(
        () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_PHONE_NUMBER_EXCEPTION + supervisorPhone));
    SupervisorDTO supervisorDTO = SupervisorUtility.toDTO(supervisor);
    return SupervisorUtility.fetchSupervisorData(objectMapper, supervisorDTO);
  }

  public List<String> listSupervisors(int pageNumber, int pageSize) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    List<String> foundSupervisors = supervisorRepository.findAllSupervisorsById(pageable);
    final int numberOfSupervisors = supervisorRepository.findAll().size();
    return SupervisorUtility.getFormattedDataOfSupervisors(foundSupervisors, numberOfSupervisors);
  }

  @Transactional
  public void updatePassword(Long id, PasswordHolderDTO passwordHolderDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Supervisor supervisor = supervisorRepository.findById(id)
        .orElseThrow(() -> new NonExistentSupervisorException(SupervisorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION + id));

    final String supervisorSecretAnswer = supervisor.getSecretAnswer();
    final String inputSecretAnswer = passwordHolderDTO.getSecretAnswer();
    SecurityUtility.validateSecretAnswer(inputSecretAnswer, supervisorSecretAnswer, encoder);

    final char[] inputPassword = passwordHolderDTO.getNewPasswordFirstInput();
    SecurityUtility.validatePassword(inputPassword);
    final boolean passwordsAreEqual = Arrays.equals(inputPassword, passwordHolderDTO.getNewPasswordSecondInput());
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_PASSWORD_VALUE);
    }
    final String newPasswordHash = SecurityUtility.hashPassword(inputPassword, encoder);
    supervisor.setPasswordHash(newPasswordHash);
  }

  @Transactional
  public void updateSecretAnswer(Long id, SecretAnswerHolderDTO secretAnswerHolderDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Supervisor supervisor = supervisorRepository.findById(id)
        .orElseThrow(() -> new NonExistentSupervisorException(SupervisorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION + id));

    final String supervisorPassword = supervisor.getPasswordHash();
    final char[] inputPassword = secretAnswerHolderDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, supervisorPassword, encoder);

    final String newSecretAnswer = secretAnswerHolderDTO.getNewSecretAnswerFirstInput();
    final boolean secretAnswersAreEqual = newSecretAnswer.equals(secretAnswerHolderDTO.getNewSecretAnswerSecondInput());
    if (!secretAnswersAreEqual) {
      throw new IncorrectSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_MISMATCH_SECRET_ANSWER);
    }
    final String hashedSecretAnswer = SecurityUtility.hashSecretAnswer(newSecretAnswer, encoder);
    supervisor.setSecretAnswer(hashedSecretAnswer);
  }

  @Transactional
  public void updatePhoneNumber(Long id, PhoneNumberHolderDTO phoneNumberHolderDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Supervisor supervisor = supervisorRepository.findById(id)
        .orElseThrow(() -> new NonExistentSupervisorException(SupervisorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION + id));

    final String supervisorPassword = supervisor.getPasswordHash();
    final char[] inputPassword = phoneNumberHolderDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, supervisorPassword, encoder);

    final String newPhoneNumber = phoneNumberHolderDTO.getNewPhoneNumber();
    ElectionSystemUtility.validatePhoneNumber(newPhoneNumber);
    ElectionSystemUtility.validatePhoneNumberIsUnique(supervisorRepository, electorRepository, newPhoneNumber);
    supervisor.setSupervisorPhone(newPhoneNumber);
  }

  public void deleteSupervisorById(Long id) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final boolean supervisorExists = supervisorRepository.existsById(id);
    if (!supervisorExists) {
      throw new NonExistentSupervisorException(SupervisorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION + id);
    }
    supervisorRepository.deleteById(id);
  }

  public void deleteSupervisorByEmail(String supervisorEmail) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    ElectionSystemUtility.validateEmail(supervisorEmail);
    Supervisor supervisor = supervisorRepository.findSupervisorByEmail(supervisorEmail).orElseThrow(
        () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + supervisorEmail));
    final Long supervisorId = supervisor.getId();
    supervisorRepository.deleteById(supervisorId);
  }

  public void deleteSupervisorByPhoneNumber(String supervisorPhone) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    ElectionSystemUtility.validatePhoneNumber(supervisorPhone);
    Supervisor supervisor = supervisorRepository.findSupervisorByPhoneNumber(supervisorPhone).orElseThrow(
        () -> new NonExistentSupervisorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_PHONE_NUMBER_EXCEPTION + supervisorPhone));
    final Long supervisorId = supervisor.getId();
    supervisorRepository.deleteById(supervisorId);
  }
}
