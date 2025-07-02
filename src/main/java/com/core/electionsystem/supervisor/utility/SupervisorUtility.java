package com.core.electionsystem.supervisor.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.exception.AlreadyOnboarded2faException;
import com.core.electionsystem.exception.InvalidValueForSecretAnswerException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.dto.SupervisorDTO;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorMetadata;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class SupervisorUtility {

  private SupervisorUtility() {
    // Default Empty Constructor
  }

  public static final int SUPERVISOR_STARTING_ID_NUMBER = 1;
  public static final int NUMBER_OF_MUNICIPALITIES_IN_BULGARIA_PLUS_TOLERANCE = 300;
  public static final String SUCCESSFULLY_REGISTERED_SUPERVISOR_RESPONSE = "Successfully Registered Supervisor";
  public static final String SUPERVISOR_BEARER_TOKEN_FOR_THE_RESPONSE = "supervisorBearerToken";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_BY_ID_EXCEPTION = "User Does Not Exist With Such Id: ";
  public static final String SUCCESSFULLY_REMOVED_USER_BY_ID_RESPONSE = "Successfully Removed User With Id: ";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_SUPERVISOR_ID = "Incorrect Value For Id. Please Provide Us With Your Real One";

  private static final String NUMBER_OF_REGISTERED_SUPERVISORS_INFORMATION = "The Current Number Of All Registered Supervisors Is: ";
  private static final String SUPERVISOR = "Supervisor ";
  private static final String REQUIRED_DATA = ": Id, First Name, Surname, Email, Phone Number: {";

  public static void validateAndHashCredentials(SupervisorDTO supervisorDTO, BCryptPasswordEncoder encoder) {
    SecurityUtility.validateAndHashSupervisorPassword(supervisorDTO, encoder);
    validateIfSecretAnswerIsNotBlank(supervisorDTO);
    SecurityUtility.hashSecretAnswerOfSupervisorOnRegistration(supervisorDTO, encoder);
    ElectionSystemUtility.validateSupervisorName(supervisorDTO);
  }

  public static SupervisorDTO toDTO(Supervisor supervisor) {
    SupervisorDTO supervisorDTO = new SupervisorDTO();
    supervisorDTO.setId(supervisor.getId());
    supervisorDTO.setFirstName(supervisor.getFirstName());
    supervisorDTO.setSurname(supervisor.getSurname());
    supervisorDTO.setSupervisorEmail(supervisor.getSupervisorEmail());
    supervisorDTO.setPasswordHash(supervisor.getPasswordHash());
    supervisorDTO.setSecretKey(supervisor.getSecretKey());
    supervisorDTO.setSecretAnswer(supervisor.getSecretAnswer());
    supervisorDTO.setSupervisorPhone(supervisor.getSupervisorPhone());
    supervisorDTO.setSupervisorMetadata(supervisor.getSupervisorMetadata());
    supervisorDTO.setTokens(supervisor.getTokens());
    return supervisorDTO;
  }

  public static Supervisor toEntity(SupervisorDTO supervisorDTO) {
    Supervisor supervisor = new Supervisor();
    supervisor.setId(supervisorDTO.getId());
    supervisor.setFirstName(supervisorDTO.getFirstName());
    supervisor.setSurname(supervisorDTO.getSurname());
    supervisor.setSupervisorEmail(supervisorDTO.getSupervisorEmail());
    supervisor.setPasswordHash(supervisorDTO.getPasswordHash());
    supervisor.setSecretKey(supervisorDTO.getSecretKey());
    supervisor.setSecretAnswer(supervisorDTO.getSecretAnswer());
    supervisor.setSupervisorPhone(supervisorDTO.getSupervisorPhone());
    supervisor.setSupervisorMetadata(supervisorDTO.getSupervisorMetadata());
    supervisor.setTokens(supervisorDTO.getTokens());
    return supervisor;
  }

  // @formatter:off
  public static void revokeAndDeleteAllSupervisorTokens(JwtService jwtService, SupervisorBearerTokenRepository supervisorBearerTokenRepository, Supervisor supervisor) {
    jwtService.revokeAllSupervisorTokens(supervisorBearerTokenRepository, supervisor);
    final Long supervisorId = supervisor.getId();
    supervisorBearerTokenRepository.deleteAllInvalidTokensBySupervisor(supervisorId);
  }
  // @formatter:on

  public static void authorizeAndValidateAccessForSupervisors(JwtService jwtService) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Role role = jwtService.getRoleFromAuthorities(authorities)
        .orElseThrow(() -> new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION));
    if (role != Role.SUPERVISOR) {
      throw new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
    }
  }

  public static List<String> getFormattedDataOfSupervisors(List<String> foundSupervisors, int numberOfSupervisors) {
    List<String> formattedDataOfSupervisors = new ArrayList<>();
    formattedDataOfSupervisors.add(NUMBER_OF_REGISTERED_SUPERVISORS_INFORMATION + numberOfSupervisors);
    int currentSupervisor = 1;
    for (String currentSupervisorData : foundSupervisors) {
      formattedDataOfSupervisors.add(SUPERVISOR + currentSupervisor + REQUIRED_DATA + currentSupervisorData + "}");
      currentSupervisor++;
    }
    return formattedDataOfSupervisors;
  }

  public static void validateIfEligibleFor2faSetup(Supervisor supervisor) {
    final SupervisorMetadata supervisorMetadata = supervisor.getSupervisorMetadata();
    final String supervisorSecretKey = supervisor.getSecretKey();
    final String salt = supervisorMetadata.getSalt();
    final String initializationVector = supervisorMetadata.getInitializationVector();
    if (((supervisorSecretKey != null) && (supervisorSecretKey.length() != SecurityUtility.DEFAULT_LENGTH_FOR_2FA_RAW_SECRET_KEY))
        || ((salt != null) || (initializationVector != null))) {
      throw new AlreadyOnboarded2faException(SecurityUtility.MESSAGE_FOR_ALREADY_ONBOARDED_2FA_EXCEPTION);
    }
  }

  public static void processSaltAndIvForMetadata(Supervisor supervisor, byte[] salt, byte[] initializationVector) {
    final SupervisorMetadata supervisorMetadata = supervisor.getSupervisorMetadata();
    final String based64Salt = SecurityUtility.encodeToBase64(salt);
    final String based64InitializationVector = SecurityUtility.encodeToBase64(initializationVector);
    SecurityUtility.clearArray(salt);
    SecurityUtility.clearArray(initializationVector);
    supervisorMetadata.setSalt(based64Salt);
    supervisorMetadata.setInitializationVector(based64InitializationVector);
  }

  public static String fetchSupervisorData(ObjectMapper objectMapper, SupervisorDTO supervisorDTO) throws JsonProcessingException {
    ObjectNode supervisorItems = objectMapper.createObjectNode();
    putSupervisorItems(supervisorDTO, supervisorItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(supervisorItems);
  }

  private static void putSupervisorItems(SupervisorDTO supervisorDTO, ObjectNode supervisorItems) {
    supervisorItems.put("id", supervisorDTO.getId());
    supervisorItems.put("full_name", supervisorDTO.getFirstName() + " " + supervisorDTO.getSurname());
    supervisorItems.put("email_address", supervisorDTO.getSupervisorEmail());
    supervisorItems.put("phone_number", supervisorDTO.getSupervisorPhone());
  }

  private static void validateIfSecretAnswerIsNotBlank(SupervisorDTO supervisorDTO) {
    final boolean secretAnswerIsBlank = supervisorDTO.getSecretAnswer().isBlank();
    if (secretAnswerIsBlank) {
      throw new InvalidValueForSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SECRET_ANSWER_EXCEPTION);
    }
  }
}
