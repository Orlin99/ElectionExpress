package com.core.electionsystem.elector.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.dto.ElectorDTO;
import com.core.electionsystem.elector.dto.ElectorResidenceDTO;
import com.core.electionsystem.elector.exception.AlreadyUsedDocumentIdException;
import com.core.electionsystem.elector.exception.AlreadyUsedEgnException;
import com.core.electionsystem.elector.exception.IllegalElectorPropertyException;
import com.core.electionsystem.elector.exception.InvalidValueForDocumentIdException;
import com.core.electionsystem.elector.exception.InvalidValueForEgnException;
import com.core.electionsystem.elector.exception.UserIsNotAdultException;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorCredentials;
import com.core.electionsystem.elector.model.properties.ElectorMetadata;
import com.core.electionsystem.elector.model.properties.ElectorName;
import com.core.electionsystem.elector.model.properties.ElectorResidence;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.exception.AlreadyOnboarded2faException;
import com.core.electionsystem.exception.InvalidValueForNameException;
import com.core.electionsystem.exception.InvalidValueForSecretAnswerException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class ElectorUtility {

  private ElectorUtility() {
    // Default Empty Constructor
  }

  public static final String SUCCESSFULLY_REGISTERED_USER_RESPONSE = "Successfully Registered User";
  public static final String MESSAGE_FOR_ALREADY_USED_EGN_EXCEPTION = "This EGN Is Already Used";
  public static final String MESSAGE_FOR_ALREADY_USED_DOCUMENT_ID_EXCEPTION = "This Document Id Is Already Used";
  public static final String ELECTOR_BEARER_TOKEN_FOR_THE_RESPONSE = "electorBearerToken";

  public static final String SUCCESSFULLY_UPDATED_FULL_NAME_RESPONSE = "Successfully Updated Name";
  public static final String SUCCESSFULLY_UPDATED_DOCUMENT_ID_RESPONSE = "Successfully Updated Document Id To: ";
  public static final String SUCCESSFULLY_UPDATED_FULL_RESIDENCE_RESPONSE = "Successfully Updated Residence";
  public static final String MESSAGE_FOR_UNCHANGED_VALUE_RESPONSE = "The Provided Value Is The Same As The Current One. No Changes Were Made";

  public static final String SUCCESSFULLY_REMOVED_USER_BY_EGN_RESPONSE = "Successfully Removed User With EGN: ";
  public static final String MESSAGE_FOR_INVALID_EGN_EXCEPTION = "The EGN You Have Provided Was Incorrect. Please Check Your EGN From Your Personal Id Card And Provide Us With It";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_BY_EGN_EXCEPTION = "User Does Not Exist With Such EGN: ";

  public static final String SUCCESSFULLY_REMOVED_USER_BY_DOCUMENT_ID_RESPONSE = "Successfully Removed User With Document Id: ";
  public static final String MESSAGE_FOR_INVALID_DOCUMENT_ID_EXCEPTION = "The Document Id You Have Provided Was Incorrect. Please Check Your Document Id From Your Personal Id Card And Provide Us With It";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_BY_DOCUMENT_ID_EXCEPTION = "User Does Not Exist With Such Document Id: ";

  public static final String MESSAGE_FOR_ALL_MISSING_NAME_PARAMETERS = "All Name Parameters ('firstName', 'middleName' And 'surname') Are Missing Or Empty";
  public static final String MESSAGE_FOR_FEW_MISSING_NAME_PARAMETERS = "Name Parameters ('firstName' And 'surname') Are Missing Or Empty";
  public static final String MESSAGE_FOR_MISSING_REGION_PARAMETER = "Residence Parameter 'region' Is Missing Or Empty";
  public static final String MESSAGE_FOR_MISSING_DATE_PARAMETERS = "Date Parameters ('day', 'month' And 'year') Are Missing Or Empty";
  public static final String MESSAGE_FOR_INVALID_DATE_PARAMETERS = "Date Parameters ('day', 'month' And 'year') Must Have Valid Values";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_FULL_NAME_EXCEPTION = "At Least One Name Must Be Provided In Order To Update Your Names";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_SEX_ATTRIBUTE = "Sex Attribute Cannot Be Null";

  public static final String ELECTOR_ID_PROPERTY = "electorId";
  public static final String DOCUMENT_ID_PROPERTY = "documentId";
  public static final String EMAIL_PROPERTY = "email";
  public static final String PHONE_NUMBER_PROPERTY = "phoneNumber";

  private static final String REGEX_FOR_EGN = "^\\d{10}$";
  private static final String MESSAGE_FOR_INVALID_VALUE_FOR_EGN_EXCEPTION = "The Provided Value Does Not Match EGN Requirements";
  private static final String REGEX_FOR_DOCUMENT_ID = "^\\d{9}$";
  private static final String MESSAGE_FOR_INVALID_VALUE_FOR_DOCUMENT_ID_EXCEPTION = "The Provided Value Does Not Match The Document Id Requirements";

  private static final int YEARS_REQUIRED_TO_VOTE = 18;
  private static final int PERMISSIBLE_TOLERANCE_IN_DAYS = 3;
  private static final String MESSAGE_FOR_USER_IS_NOT_ADULT_EXCEPTION = "You Need To Have At Least 18 Years Of Age To Be Able Vote";

  private static final String NUMBER_OF_REGISTERED_USERS_INFORMATION = "The Current Number Of All Registered Users Is: ";
  private static final String USER = "User ";
  private static final String REQUIRED_DATA = ": EGN, Email, Phone Number: {";
  private static final String MESSAGE_FOR_REPEATED_NAME_RESPONSE = "One Or More Of The Provided Names Is The Same As The Current One. Enter Only Those That You Want To Change";
  private static final String DATE_PATTERN = "dd LLLL yyyy";
  private static final String MESSAGE_FOR_EMPTY_PROPERTY = "The Requested Property Cannot Be Null Or Empty";
  private static final String MESSAGE_FOR_ILLEGAL_PROPERTY_EXCEPTION = "The Requested Property Cannot Be Accessed Or It Does Not Exist";

  public static void validateAndHashCredentials(ElectorDTO electorDTO, BCryptPasswordEncoder encoder) {
    SecurityUtility.validateAndHashElectorPassword(electorDTO, encoder);
    validateIfSecretAnswerIsNotBlank(electorDTO);
    SecurityUtility.hashSecretAnswerOfElectorOnRegistration(electorDTO, encoder);
  }

  public static ElectorDTO toDTO(Elector elector) {
    ElectorDTO electorDTO = new ElectorDTO();
    electorDTO.setElectorId(elector.getElectorId());
    electorDTO.setElectorName(elector.getElectorName());
    electorDTO.setSex(elector.getSex());
    electorDTO.setDateOfBirth(elector.getDateOfBirth());
    electorDTO.setDocumentId(elector.getDocumentId());
    electorDTO.setElectorResidence(elector.getElectorResidence());
    electorDTO.setElectorCredentials(elector.getElectorCredentials());
    electorDTO.setElectorMetadata(elector.getElectorMetadata());
    electorDTO.setTokens(elector.getTokens());
    return electorDTO;
  }

  public static Elector toEntity(ElectorDTO electorDTO) {
    Elector elector = new Elector();
    elector.setElectorId(electorDTO.getElectorId());
    elector.setElectorName(electorDTO.getElectorName());
    elector.setSex(electorDTO.getSex());
    elector.setDateOfBirth(electorDTO.getDateOfBirth());
    elector.setDocumentId(electorDTO.getDocumentId());
    elector.setElectorResidence(electorDTO.getElectorResidence());
    elector.setElectorCredentials(electorDTO.getElectorCredentials());
    elector.setElectorMetadata(electorDTO.getElectorMetadata());
    elector.setTokens(electorDTO.getTokens());
    return elector;
  }

  public static ElectorResidenceDTO toDTO(ElectorResidence electorResidence) {
    ElectorResidenceDTO electorResidenceDTO = new ElectorResidenceDTO();
    electorResidenceDTO.setRegion(electorResidence.getRegion());
    electorResidenceDTO.setMunicipality(electorResidence.getMunicipality());
    electorResidenceDTO.setLocality(electorResidence.getLocality());
    electorResidenceDTO.setResidentialArea(electorResidence.getResidentialArea());
    electorResidenceDTO.setStreet(electorResidence.getStreet());
    electorResidenceDTO.setLocationNumber(electorResidence.getLocationNumber());
    electorResidenceDTO.setEntrance(electorResidence.getEntrance());
    electorResidenceDTO.setFloor(electorResidence.getFloor());
    electorResidenceDTO.setApartment(electorResidence.getApartment());
    return electorResidenceDTO;
  }

  public static ElectorResidence toEntity(Elector elector, ElectorResidenceDTO electorResidenceDTO) {
    ElectorResidence electorResidence = new ElectorResidence();
    electorResidence.setElectorResidenceId(elector.getElectorResidence().getElectorResidenceId());
    electorResidence.setRegion(electorResidenceDTO.getRegion());
    electorResidence.setMunicipality(electorResidenceDTO.getMunicipality());
    electorResidence.setLocality(electorResidenceDTO.getLocality());
    electorResidence.setResidentialArea(electorResidenceDTO.getResidentialArea());
    electorResidence.setStreet(electorResidenceDTO.getStreet());
    electorResidence.setLocationNumber(electorResidenceDTO.getLocationNumber());
    electorResidence.setEntrance(electorResidenceDTO.getEntrance());
    electorResidence.setFloor(electorResidenceDTO.getFloor());
    electorResidence.setApartment(electorResidenceDTO.getApartment());
    electorResidence.setElector(elector);
    return electorResidence;
  }

  public static void validateEGN(String electorId) {
    ElectionSystemUtility.validateProperty(REGEX_FOR_EGN, electorId, new InvalidValueForEgnException(MESSAGE_FOR_INVALID_VALUE_FOR_EGN_EXCEPTION));
  }

  public static void validateEGNIsUnique(ElectorRepository electorRepository, String electorId) {
    final boolean electorIsPresentByEGN = electorRepository.findElectorById(electorId).isPresent();
    if (electorIsPresentByEGN) {
      throw new AlreadyUsedEgnException(ElectorUtility.MESSAGE_FOR_ALREADY_USED_EGN_EXCEPTION);
    }
  }

  public static void validateDocumentId(String documentId) {
    ElectionSystemUtility.validateProperty(REGEX_FOR_DOCUMENT_ID, documentId,
        new InvalidValueForDocumentIdException(MESSAGE_FOR_INVALID_VALUE_FOR_DOCUMENT_ID_EXCEPTION));
  }

  public static void validateDocumentIdIsUnique(ElectorRepository electorRepository, String documentId) {
    final boolean electorIsPresentByDocumentId = electorRepository.findElectorByDocumentId(documentId).isPresent();
    if (electorIsPresentByDocumentId) {
      throw new AlreadyUsedDocumentIdException(ElectorUtility.MESSAGE_FOR_ALREADY_USED_DOCUMENT_ID_EXCEPTION);
    }
  }

  public static void validateIfIsAdult(Elector elector) {
    LocalDate currentDate = LocalDate.now();
    LocalDate dateToBeAdult = currentDate.minusYears(YEARS_REQUIRED_TO_VOTE).plusDays(PERMISSIBLE_TOLERANCE_IN_DAYS);
    boolean isAdult = elector.getDateOfBirth().isBefore(dateToBeAdult);
    if (!isAdult) {
      throw new UserIsNotAdultException(MESSAGE_FOR_USER_IS_NOT_ADULT_EXCEPTION);
    }
  }

  // @formatter:off
  public static void revokeAndDeleteAllElectorTokens(JwtService jwtService, ElectorBearerTokenRepository electorBearerTokenRepository, Elector elector) {
    jwtService.revokeAllElectorTokens(electorBearerTokenRepository, elector);
    final String electorId = elector.getElectorId();
    electorBearerTokenRepository.deleteAllInvalidTokensByElector(electorId);
  }

  public static void authorizeAndValidateAccess(JwtService jwtService, ElectorRepository electorRepository, Map<String, String> wantedProperty) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Role role = jwtService.getRoleFromAuthorities(authorities)
        .orElseThrow(() -> new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION));
    if (!isSupervisorOrElector(role)) {
      throw new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
    }
    if (role == Role.ELECTOR) {
      Elector elector = electorRepository.findElectorByEmail(currentUserEmail)
          .orElseThrow(() -> new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE));
      validateElectorAccessForCurrentProperty(elector, wantedProperty);
    }
  }
  // @formatter:on

  public static void authorizeAndValidateAccessForElectors(JwtService jwtService) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Role role = jwtService.getRoleFromAuthorities(authorities)
        .orElseThrow(() -> new NonExistentUserRoleException(SecurityUtility.MESSAGE_FOR_NON_EXISTENT_USER_ROLE_EXCEPTION));
    if (role != Role.ELECTOR) {
      throw new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
    }
  }

  public static List<String> getFormattedDataOfUsers(List<String> foundUsers, int numberOfUsers) {
    List<String> formattedDataOfUsers = new ArrayList<>();
    formattedDataOfUsers.add(NUMBER_OF_REGISTERED_USERS_INFORMATION + numberOfUsers);
    int currentUser = 1;
    for (String currentUserData : foundUsers) {
      formattedDataOfUsers.add(USER + currentUser + REQUIRED_DATA + currentUserData + "}");
      currentUser++;
    }
    return formattedDataOfUsers;
  }

  // @formatter:off
  public static String updateNameIfValid(String userInputName, String currentName) {
    if (userInputName == null) {
      return currentName;
    }
    ElectionSystemUtility.validateProperty(ElectionSystemUtility.REGEX_FOR_NAME, userInputName, new InvalidValueForNameException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION));
    if (userInputName.equals(currentName)) {
      throw new InvalidValueForNameException(MESSAGE_FOR_REPEATED_NAME_RESPONSE);
    }
    return userInputName;
  }
  // @formatter:on

  public static void validateIfEligibleFor2faSetup(Elector elector, ElectorCredentials electorCredentials) {
    final ElectorMetadata electorMetadata = elector.getElectorMetadata();
    final String electorSecretKey = electorCredentials.getSecretKey();
    final String salt = electorMetadata.getSalt();
    final String initializationVector = electorMetadata.getInitializationVector();
    if (((electorSecretKey != null) && (electorSecretKey.length() != SecurityUtility.DEFAULT_LENGTH_FOR_2FA_RAW_SECRET_KEY))
        || ((salt != null) || (initializationVector != null))) {
      throw new AlreadyOnboarded2faException(SecurityUtility.MESSAGE_FOR_ALREADY_ONBOARDED_2FA_EXCEPTION);
    }
  }

  public static void processSaltAndIvForMetadata(Elector elector, byte[] salt, byte[] initializationVector) {
    final ElectorMetadata electorMetadata = elector.getElectorMetadata();
    final String based64Salt = SecurityUtility.encodeToBase64(salt);
    final String based64InitializationVector = SecurityUtility.encodeToBase64(initializationVector);
    SecurityUtility.clearArray(salt);
    SecurityUtility.clearArray(initializationVector);
    electorMetadata.setSalt(based64Salt);
    electorMetadata.setInitializationVector(based64InitializationVector);
  }

  public static String fetchElectorData(ObjectMapper objectMapper, ElectorDTO electorDTO) throws JsonProcessingException {
    ObjectNode electorItems = objectMapper.createObjectNode();
    ObjectNode residenceItems = objectMapper.createObjectNode();
    putElectorItems(electorDTO, electorItems);
    putElectorCredentials(electorDTO.getElectorCredentials(), electorItems);
    putElectorResidenceItems(electorDTO.getElectorResidence(), electorItems, residenceItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(electorItems);
  }

  private static void putElectorItems(ElectorDTO electorDTO, ObjectNode electorItems) {
    electorItems.put("EGN", electorDTO.getElectorId());
    electorItems.put("full_name", getFullName(electorDTO.getElectorName()));
    electorItems.put("date_of_birth", getFormattedLocalDate(electorDTO.getDateOfBirth()));
    electorItems.put("document_id", electorDTO.getDocumentId());
    electorItems.put("sex", electorDTO.getSex().toString());
  }

  private static String getFullName(ElectorName electorName) {
    if ((electorName.getMiddleName() == null) || electorName.getMiddleName().isBlank()) {
      return electorName.getFirstName() + " " + electorName.getSurname();
    }
    return electorName.getFirstName() + " " + electorName.getMiddleName() + " " + electorName.getSurname();
  }

  private static String getFormattedLocalDate(LocalDate localDate) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    return localDate.format(dateTimeFormatter);
  }

  private static void putElectorCredentials(ElectorCredentials electorCredentials, ObjectNode electorItems) {
    electorItems.put("email_address", electorCredentials.getEmail());
    electorItems.put("phone_number", electorCredentials.getPhoneNumber());
  }

  private static void putElectorResidenceItems(ElectorResidence electorResidence, ObjectNode electorItems, ObjectNode residenceItems) {
    residenceItems.put("region", electorResidence.getRegion());
    residenceItems.put("municipality", electorResidence.getMunicipality());
    residenceItems.put("locality", electorResidence.getLocality());
    putItemIfNotNull(residenceItems, "residential_area", electorResidence.getResidentialArea());
    putItemIfNotNull(residenceItems, "street", electorResidence.getStreet());
    residenceItems.put("location_number", electorResidence.getLocationNumber());
    putItemIfNotNull(residenceItems, "entrance", electorResidence.getEntrance());
    putItemIfNotNull(residenceItems, "floor", electorResidence.getFloor());
    putItemIfNotNull(residenceItems, "apartment", electorResidence.getApartment());
    electorItems.set("residence", residenceItems);
  }

  private static void putItemIfNotNull(ObjectNode residenceItems, String itemName, Object itemValue) {
    if (itemValue instanceof String stringType) {
      residenceItems.put(itemName, stringType);
    } else if (itemValue instanceof Integer integerType) {
      residenceItems.put(itemName, integerType);
    } else if (itemValue instanceof Character characterType) {
      residenceItems.put(itemName, characterType.toString());
    } else if (itemValue != null) {
      residenceItems.put(itemName, itemValue.toString());
    }
  }

  private static void validateIfSecretAnswerIsNotBlank(ElectorDTO electorDTO) {
    final boolean secretAnswerIsBlank = electorDTO.getElectorCredentials().getSecretAnswer().isBlank();
    if (secretAnswerIsBlank) {
      throw new InvalidValueForSecretAnswerException(ElectionSystemUtility.MESSAGE_FOR_INVALID_VALUE_FOR_SECRET_ANSWER_EXCEPTION);
    }
  }

  private static boolean isSupervisorOrElector(Role role) {
    return (role == Role.SUPERVISOR) || (role == Role.ELECTOR);
  }

  private static void validateElectorAccessForCurrentProperty(Elector elector, Map<String, String> wantedProperty) {
    if (wantedProperty.isEmpty()) {
      throw new IllegalElectorPropertyException(MESSAGE_FOR_EMPTY_PROPERTY);
    }
    wantedProperty.forEach((key, value) -> {
      switch (key) {
        case ELECTOR_ID_PROPERTY:
          if (!elector.getElectorId().equals(value)) {
            throwAccessDeniedException();
          }
          break;
        case DOCUMENT_ID_PROPERTY:
          if (!elector.getDocumentId().equals(value)) {
            throwAccessDeniedException();
          }
          break;
        case EMAIL_PROPERTY:
          if (!elector.getElectorCredentials().getEmail().equals(value)) {
            throwAccessDeniedException();
          }
          break;
        case PHONE_NUMBER_PROPERTY:
          if (!elector.getElectorCredentials().getPhoneNumber().equals(value)) {
            throwAccessDeniedException();
          }
          break;
        default:
          throw new IllegalElectorPropertyException(MESSAGE_FOR_ILLEGAL_PROPERTY_EXCEPTION);
      }
    });
  }

  private static void throwAccessDeniedException() {
    throw new AccessDeniedException(SecurityUtility.ERROR_MESSAGE_FOR_ACCESS_DENIED_RESPONSE);
  }
}
