package com.core.electionsystem.elector.service;

import java.time.DateTimeException;
import java.time.LocalDate;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.dto.LoginUserDTO;
import com.core.electionsystem.dto.PasswordHolderDTO;
import com.core.electionsystem.dto.PhoneNumberHolderDTO;
import com.core.electionsystem.dto.SecretAnswerHolderDTO;
import com.core.electionsystem.elector.dto.ElectorDTO;
import com.core.electionsystem.elector.dto.ElectorResidenceDTO;
import com.core.electionsystem.elector.exception.AlreadyUsedDocumentIdException;
import com.core.electionsystem.elector.exception.EmptyDateParameterException;
import com.core.electionsystem.elector.exception.EmptyNameParameterException;
import com.core.electionsystem.elector.exception.EmptyRegionParameterException;
import com.core.electionsystem.elector.exception.InvalidDateParameterException;
import com.core.electionsystem.elector.exception.InvalidSexParameterException;
import com.core.electionsystem.elector.exception.InvalidValueForDocumentIdException;
import com.core.electionsystem.elector.exception.NonExistentElectorException;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.model.properties.ElectorName;
import com.core.electionsystem.elector.model.properties.ElectorResidence;
import com.core.electionsystem.elector.model.properties.Sex;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.IncorrectSecretAnswerException;
import com.core.electionsystem.exception.InvalidValueForNameException;
import com.core.electionsystem.exception.UnauthenticatedUserException;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class ElectorService {

  private final ElectorRepository electorRepository;
  private final SupervisorRepository supervisorRepository;
  private final ElectorBearerTokenRepository electorBearerTokenRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final ObjectMapper objectMapper;
  private final BCryptPasswordEncoder encoder;

  @Autowired
  public ElectorService(ElectorRepository electorRepository, SupervisorRepository supervisorRepository,
      ElectorBearerTokenRepository electorBearerTokenRepository, AuthenticationManager authenticationManager, JwtService jwtService,
      ObjectMapper objectMapper, BCryptPasswordEncoder encoder) {
    this.electorRepository = electorRepository;
    this.supervisorRepository = supervisorRepository;
    this.electorBearerTokenRepository = electorBearerTokenRepository;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.objectMapper = objectMapper;
    this.encoder = encoder;
  }

  public void registerNewElector(ElectorDTO electorDTO) {
    ElectorUtility.validateAndHashCredentials(electorDTO, encoder);
    final Elector elector = ElectorUtility.toEntity(electorDTO);
    final String electorId = elector.getElectorId();
    ElectorUtility.validateEGN(electorId);
    ElectorUtility.validateEGNIsUnique(electorRepository, electorId);
    ElectionSystemUtility.validateElectorName(elector.getElectorName());
    ElectorUtility.validateIfIsAdult(elector);
    final String documentId = elector.getDocumentId();
    ElectorUtility.validateDocumentId(documentId);
    ElectorUtility.validateDocumentIdIsUnique(electorRepository, documentId);
    final String email = elector.getElectorCredentials().getEmail();
    ElectionSystemUtility.validateEmail(email);
    ElectionSystemUtility.validateEmailIsUnique(supervisorRepository, electorRepository, email);
    final String phoneNumber = elector.getElectorCredentials().getPhoneNumber();
    ElectionSystemUtility.validatePhoneNumber(phoneNumber);
    ElectionSystemUtility.validatePhoneNumberIsUnique(supervisorRepository, electorRepository, phoneNumber);
    electorRepository.save(elector);
  }

  public Map<String, Object> loginExistingElector(LoginUserDTO loginUserDTO) {
    final String email = loginUserDTO.getEmail();
    ElectionSystemUtility.validateEmail(email);
    final boolean isElectorPresentByEmail = electorRepository.findElectorByEmail(email).isPresent();
    if (!isElectorPresentByEmail) {
      throw new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email);
    }
    Authentication authentication = SecurityUtility.authenticateUser(authenticationManager, loginUserDTO);
    if (!authentication.isAuthenticated()) {
      throw new UnauthenticatedUserException(SecurityUtility.ERROR_MESSAGE_FOR_UNAUTHENTICATED_USER);
    }
    Elector elector = electorRepository.findElectorByEmail(email)
        .orElseThrow(() -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email));
    ElectorUtility.revokeAndDeleteAllElectorTokens(jwtService, electorBearerTokenRepository, elector);
    final Role role = Role.ELECTOR;
    final String electorBearerTokenValue = jwtService.generateToken(email, role);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(ElectionSystemUtility.LOGIN_RESPONSE_MESSAGE_ATTRIBUTE, ElectionSystemUtility.SUCCESSFULLY_LOGGED_IN_USER_RESPONSE);
    responseMap.put(ElectorUtility.ELECTOR_BEARER_TOKEN_FOR_THE_RESPONSE, electorBearerTokenValue);
    final ElectorBearerToken bearerToken = jwtService.buildElectorToken(elector, electorBearerTokenValue);
    electorBearerTokenRepository.save(bearerToken);
    return responseMap;
  }

  public String getElectorById(String electorId) throws JsonProcessingException {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));
    ElectorDTO electorDTO = ElectorUtility.toDTO(elector);
    return ElectorUtility.fetchElectorData(objectMapper, electorDTO);
  }

  public String getElectorByDocumentId(String documentId) throws JsonProcessingException {
    ElectorUtility.validateDocumentId(documentId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.DOCUMENT_ID_PROPERTY, documentId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByDocumentId(documentId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_DOCUMENT_ID_EXCEPTION + documentId));
    ElectorDTO electorDTO = ElectorUtility.toDTO(elector);
    return ElectorUtility.fetchElectorData(objectMapper, electorDTO);
  }

  public String getElectorByEmail(String email) throws JsonProcessingException {
    ElectionSystemUtility.validateEmail(email);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.EMAIL_PROPERTY, email);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByEmail(email)
        .orElseThrow(() -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email));
    ElectorDTO electorDTO = ElectorUtility.toDTO(elector);
    return ElectorUtility.fetchElectorData(objectMapper, electorDTO);
  }

  public String getElectorByPhoneNumber(String phoneNumber) throws JsonProcessingException {
    ElectionSystemUtility.validatePhoneNumber(phoneNumber);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.PHONE_NUMBER_PROPERTY, phoneNumber);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByPhoneNumber(phoneNumber).orElseThrow(
        () -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_PHONE_NUMBER_EXCEPTION + phoneNumber));
    ElectorDTO electorDTO = ElectorUtility.toDTO(elector);
    return ElectorUtility.fetchElectorData(objectMapper, electorDTO);
  }

  public List<String> listElectorsByNames(String firstName, String middleName, String surname, int pageNumber, int pageSize) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final boolean firstNameIsEmpty = ElectionSystemUtility.isNullOrBlank(firstName);
    final boolean middleNameIsEmpty = ElectionSystemUtility.isNullOrBlank(middleName);
    final boolean surnameIsEmpty = ElectionSystemUtility.isNullOrBlank(surname);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    final int numberOfUsers = electorRepository.findAll().size();

    if (firstNameIsEmpty && middleNameIsEmpty && surnameIsEmpty) {
      throw new EmptyNameParameterException(ElectorUtility.MESSAGE_FOR_ALL_MISSING_NAME_PARAMETERS);
    } else if (firstNameIsEmpty && surnameIsEmpty) {
      throw new EmptyNameParameterException(ElectorUtility.MESSAGE_FOR_FEW_MISSING_NAME_PARAMETERS);
    } else if (firstNameIsEmpty && middleNameIsEmpty) {
      List<String> foundElectorsBySurname = electorRepository.findBySurname(surname, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsBySurname, numberOfUsers);
    } else if (firstNameIsEmpty) {
      List<String> foundElectorsByMiddleNameAndSurname = electorRepository.findByMiddleNameAndSurname(middleName, surname, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByMiddleNameAndSurname, numberOfUsers);
    } else if (middleNameIsEmpty && surnameIsEmpty) {
      List<String> foundElectorsByFirstName = electorRepository.findByFirstName(firstName, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByFirstName, numberOfUsers);
    } else if (surnameIsEmpty) {
      List<String> foundElectorsByFirstNameAndMiddleName = electorRepository.findByFirstNameAndMiddleName(firstName, middleName, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByFirstNameAndMiddleName, numberOfUsers);
    } else if (middleNameIsEmpty) {
      List<String> foundElectorsByFirstNameAndSurname = electorRepository.findByFirstNameAndSurname(firstName, surname, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByFirstNameAndSurname, numberOfUsers);
    } else {
      List<String> foundElectorsByFullName = electorRepository.findByFullName(firstName, middleName, surname, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByFullName, numberOfUsers);
    }
  }

  public List<String> listElectorsByResidence(String region, String municipality, String locality, int pageNumber, int pageSize) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final boolean regionIsEmpty = ElectionSystemUtility.isNullOrBlank(region);
    final boolean municipalityIsEmpty = ElectionSystemUtility.isNullOrBlank(municipality);
    final boolean localityIsEmpty = ElectionSystemUtility.isNullOrBlank(locality);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    int numberOfUsers = electorRepository.findAll().size();

    if (regionIsEmpty) {
      throw new EmptyRegionParameterException(ElectorUtility.MESSAGE_FOR_MISSING_REGION_PARAMETER);
    } else if (municipalityIsEmpty && localityIsEmpty) {
      List<String> foundElectorsByRegion = electorRepository.findByRegion(region, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByRegion, numberOfUsers);
    } else if (municipalityIsEmpty) {
      List<String> foundElectorsByRegionAndLocality = electorRepository.findByRegionAndLocality(region, locality, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByRegionAndLocality, numberOfUsers);
    } else if (localityIsEmpty) {
      List<String> foundElectorsByRegionAndMunicipality = electorRepository.findByRegionAndMunicipality(region, municipality, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByRegionAndMunicipality, numberOfUsers);
    } else {
      List<String> foundElectorsByMainResidenceProperties = electorRepository.findByMainResidenceProperties(region, municipality, locality, pageable);
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByMainResidenceProperties, numberOfUsers);
    }
  }

  public List<String> listElectorsByBirthDate(Integer day, Integer month, Integer year, int pageNumber, int pageSize) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final boolean dayIsNull = ElectionSystemUtility.isNull(day);
    final boolean monthIsNull = ElectionSystemUtility.isNull(month);
    final boolean yearIsNull = ElectionSystemUtility.isNull(year);

    if (dayIsNull || monthIsNull || yearIsNull) {
      throw new EmptyDateParameterException(ElectorUtility.MESSAGE_FOR_MISSING_DATE_PARAMETERS);
    }
    try {
      LocalDate dateOfBirth = LocalDate.of(year, month, day);
      Pageable pageable = PageRequest.of(pageNumber, pageSize);
      List<String> foundElectorsByDateOfBirth = electorRepository.findByDateOfBirth(dateOfBirth, pageable);
      int numberOfUsers = electorRepository.findAll().size();
      return ElectorUtility.getFormattedDataOfUsers(foundElectorsByDateOfBirth, numberOfUsers);
    } catch (MethodArgumentTypeMismatchException | DateTimeException exception) {
      throw new InvalidDateParameterException(ElectorUtility.MESSAGE_FOR_INVALID_DATE_PARAMETERS);
    }
  }

  public Integer getElectorsCountBySex(String sex) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    try {
      Sex sexCode = Sex.fromString(sex);
      return electorRepository.getElectorsCountBySex(sexCode);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new InvalidSexParameterException(Sex.MESSAGE_FOR_INVALID_SEX_PARAMETER);
    }
  }

  @Transactional
  public void updateUserFullName(String electorId, String firstName, String middleName, String surname) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    if ((firstName == null) && (middleName == null) && (surname == null)) {
      throw new InvalidValueForNameException(ElectorUtility.MESSAGE_FOR_INVALID_VALUE_FOR_FULL_NAME_EXCEPTION);
    }
    ElectorName electorName = elector.getElectorName();
    final String newFirstName = ElectorUtility.updateNameIfValid(firstName, electorName.getFirstName());
    electorName.setFirstName(newFirstName);
    final String newMiddleName = ElectorUtility.updateNameIfValid(middleName, electorName.getMiddleName());
    electorName.setMiddleName(newMiddleName);
    final String newSurname = ElectorUtility.updateNameIfValid(surname, electorName.getSurname());
    electorName.setSurname(newSurname);
  }

  @Transactional
  public void updateDocumentId(String electorId, String documentId) {
    ElectorUtility.validateEGN(electorId);
    ElectorUtility.validateDocumentId(documentId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    if (elector.getDocumentId().equals(documentId)) {
      throw new InvalidValueForDocumentIdException(ElectorUtility.MESSAGE_FOR_UNCHANGED_VALUE_RESPONSE);
    }
    final boolean electorIsPresentByDocumentId = electorRepository.findElectorByDocumentId(documentId).isPresent();
    if (electorIsPresentByDocumentId) {
      throw new AlreadyUsedDocumentIdException(ElectorUtility.MESSAGE_FOR_ALREADY_USED_DOCUMENT_ID_EXCEPTION);
    }
    elector.setDocumentId(documentId);
  }

  @Transactional
  public void updateUserFullResidence(String electorId, ElectorResidenceDTO electorResidenceDTO) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    ElectorResidence electorResidence = ElectorUtility.toEntity(elector, electorResidenceDTO);
    elector.setElectorResidence(electorResidence);
    electorRepository.save(elector);
  }

  @Transactional
  public void updatePassword(String electorId, PasswordHolderDTO passwordHolderDTO) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    final String electorSecretAnswer = elector.getElectorCredentials().getSecretAnswer();
    final String inputSecretAnswer = passwordHolderDTO.getSecretAnswer();
    SecurityUtility.validateSecretAnswer(inputSecretAnswer, electorSecretAnswer, encoder);

    final char[] inputPassword = passwordHolderDTO.getNewPasswordFirstInput();
    SecurityUtility.validatePassword(inputPassword);
    final boolean passwordsAreEqual = Arrays.equals(inputPassword, passwordHolderDTO.getNewPasswordSecondInput());
    if (!passwordsAreEqual) {
      throw new IncorrectPasswordException(ElectionSystemUtility.MESSAGE_FOR_INCORRECT_PASSWORD_VALUE);
    }
    final String newPasswordHash = SecurityUtility.hashPassword(inputPassword, encoder);
    elector.getElectorCredentials().setPasswordHash(newPasswordHash);
  }

  @Transactional
  public void updateSecretAnswer(String electorId, SecretAnswerHolderDTO secretAnswerHolderDTO) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    final String electorPassword = elector.getElectorCredentials().getPasswordHash();
    final char[] inputPassword = secretAnswerHolderDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, electorPassword, encoder);

    final String newSecretAnswer = secretAnswerHolderDTO.getNewSecretAnswerFirstInput();
    final boolean secretAnswersAreEqual = newSecretAnswer.equals(secretAnswerHolderDTO.getNewSecretAnswerSecondInput());
    if (!secretAnswersAreEqual) {
      throw new IncorrectSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_MISMATCH_SECRET_ANSWER);
    }
    final String secretAnswerHash = SecurityUtility.hashSecretAnswer(newSecretAnswer, encoder);
    elector.getElectorCredentials().setSecretAnswer(secretAnswerHash);
  }

  @Transactional
  public void updatePhoneNumber(String electorId, PhoneNumberHolderDTO phoneNumberHolderDTO) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findById(electorId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId));

    final String electorPassword = elector.getElectorCredentials().getPasswordHash();
    final char[] inputPassword = phoneNumberHolderDTO.getInputPassword();
    SecurityUtility.validateLoginPassword(inputPassword, electorPassword, encoder);

    final String newPhoneNumber = phoneNumberHolderDTO.getNewPhoneNumber();
    ElectionSystemUtility.validatePhoneNumber(newPhoneNumber);
    ElectionSystemUtility.validatePhoneNumberIsUnique(supervisorRepository, electorRepository, newPhoneNumber);
    elector.getElectorCredentials().setPhoneNumber(newPhoneNumber);
  }

  public void deleteElectorById(String electorId) {
    ElectorUtility.validateEGN(electorId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.ELECTOR_ID_PROPERTY, electorId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    final boolean electorExists = electorRepository.existsById(electorId);
    if (!electorExists) {
      throw new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION + electorId);
    }
    electorRepository.deleteById(electorId);
  }

  public void deleteElectorByDocumentId(String documentId) {
    ElectorUtility.validateDocumentId(documentId);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.DOCUMENT_ID_PROPERTY, documentId);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByDocumentId(documentId)
        .orElseThrow(() -> new NonExistentElectorException(ElectorUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_DOCUMENT_ID_EXCEPTION + documentId));
    final String electorId = elector.getElectorId();
    electorRepository.deleteById(electorId);
  }

  public void deleteElectorByEmail(String email) {
    ElectionSystemUtility.validateEmail(email);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.EMAIL_PROPERTY, email);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByEmail(email)
        .orElseThrow(() -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + email));
    final String electorId = elector.getElectorId();
    electorRepository.deleteById(electorId);
  }

  public void deleteElectorByPhoneNumber(String phoneNumber) {
    ElectionSystemUtility.validatePhoneNumber(phoneNumber);
    Map<String, String> wantedProperty = Map.of(ElectorUtility.PHONE_NUMBER_PROPERTY, phoneNumber);
    ElectorUtility.authorizeAndValidateAccess(jwtService, electorRepository, wantedProperty);
    Elector elector = electorRepository.findElectorByPhoneNumber(phoneNumber).orElseThrow(
        () -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_PHONE_NUMBER_EXCEPTION + phoneNumber));
    final String electorId = elector.getElectorId();
    electorRepository.deleteById(electorId);
  }
}
