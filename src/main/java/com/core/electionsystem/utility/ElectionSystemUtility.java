package com.core.electionsystem.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.core.electionsystem.elector.model.properties.ElectorName;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.exception.AlreadyUsedEmailException;
import com.core.electionsystem.exception.AlreadyUsedPhoneNumberException;
import com.core.electionsystem.exception.InvalidValueForEmailException;
import com.core.electionsystem.exception.InvalidValueForNameException;
import com.core.electionsystem.exception.InvalidValueForPhoneNumberException;
import com.core.electionsystem.supervisor.dto.SupervisorDTO;
import com.core.electionsystem.supervisor.repository.SupervisorRepository;

public final class ElectionSystemUtility {

  private ElectionSystemUtility() {
    // Default Empty Constructor
  }

  public static final byte EMPTY_ARRAY_LENGTH = 0;
  public static final String REGEX_FOR_NAME = "^[A-Z][a-z]+$";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION = "The Provided Value Does Not Match The Name Requirements";

  public static final String REGEX_FOR_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_EMAIL_EXCEPTION = "The Provided Email Does Not Exist Or It Was Misspelled";
  public static final String MESSAGE_FOR_ALREADY_USED_EMAIL_EXCEPTION = "This Email Is Already Used";
  public static final String SUCCESSFULLY_REMOVED_USER_BY_EMAIL_RESPONSE = "Successfully Removed User With Email: ";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION = "User Does Not Exist With Such Email: ";

  public static final String REGEX_FOR_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};:,<.>/?]).{16,}$";
  public static final String INVALID_PASSWORD_MESSAGE = "The Password You Entered Is Invalid. It Must Have At Least One Lowercase Letter, One Uppercase Letter, One Number, One Special Character And Minimum Length Of 16 Characters";
  public static final String INCORRECT_PASSWORD_MESSAGE = "The Password You Entered Is Incorrect. Please Double-Check Your Password And Try Again";
  public static final String MESSAGE_FOR_INCORRECT_PASSWORD_VALUE = "Incorrect Password: Please Be Sure That Both Password Fields Are Correct And Equal";
  public static final String SUCCESSFULLY_UPDATED_PASSWORD_RESPONSE = "Successfully Updated Password";

  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_SECRET_ANSWER_EXCEPTION = "The Secret Answer Cannot Be Empty";
  public static final String MESSAGE_FOR_INCORRECT_SECRET_ANSWER = "Incorrect Or Misspelled Secret Answer";
  public static final String MESSAGE_FOR_MISMATCH_SECRET_ANSWER = "Incorrect Secret Answer: Please Be Sure That Both Secret Answer Fields Are Correct And Equal";
  public static final String SUCCESSFULLY_UPDATED_SECRET_ANSWER_RESPONSE = "Successfully Updated Secret Answer";

  public static final String REGEX_FOR_PHONE_NUMBER = "^\\+?[0-9. ()-]{7,20}$";
  public static final String SUCCESSFULLY_UPDATED_PHONE_NUMBER_RESPONSE = "Successfully Updated Phone Number";
  public static final String SUCCESSFULLY_REMOVED_USER_BY_PHONE_NUMBER_RESPONSE = "Successfully Removed User With Phone Number: ";
  public static final String MESSAGE_FOR_NON_EXISTENT_USER_BY_PHONE_NUMBER_EXCEPTION = "User Does Not Exist With Such Phone Number: ";

  public static final String MESSAGE_FOR_INVALID_SUPERVISOR_ARGUMENTS_EXCEPTION = "Invalid Arguments For Supervisor Type Of User";
  public static final String MESSAGE_FOR_INVALID_ELECTOR_ARGUMENTS_EXCEPTION = "Invalid Arguments For Elector Type Of User";

  public static final String LOGIN_RESPONSE_MESSAGE_ATTRIBUTE = "message";
  public static final String SUCCESSFULLY_LOGGED_IN_USER_RESPONSE = "Login Successful";
  public static final String SUPERVISOR_ROLE = "SUPERVISOR";
  public static final String ELECTOR_ROLE = "ELECTOR";
  public static final String MESSAGE_FOR_UNKNOWN_AUTHORITY_EXCEPTION = "This Authority Is Unknown: ";

  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_USER_ID = "The Id Field Cannot Be Empty. Please Provide Your Real Id Or EGN";
  public static final String MESSAGE_FOR_INCORRECT_VALUE_FOR_USER_ID = "The Id Was Incorrect. Please Provide Your Real Id Or EGN";
  public static final String MESSAGE_FOR_INCORRECT_VALUE_FOR_PHONE_NUMBER = "The Phone Number Provided Is Incorrect. Please Provide Us With Your Real One";
  public static final String SUCCESSFULLY_SENT_EMAIL_FOR_SECRET_ANSWER_RECOVERY = "You Have Successfully Began Your Secret Answer Recovery. For Further Instructions Check The Email We Have Sent To Your Email Address. There You Will Find Out How To Restore Your Secret Answer";
  public static final String SUCCESSFULLY_SENT_EMAIL_FOR_PASSWORD_RECOVERY = "You Have Successfully Began Your Password Recovery. For Further Instructions Check The Email We Have Sent To Your Email Address. There You Will Find Out How To Restore Your Password";

  public static final String MESSAGE_FOR_ILLEGAL_TOKEN_VALUE_EXCEPTION = "The Value Of The Token Must Not Be Null Or Empty";
  public static final String MESSAGE_FOR_ILLEGAL_TOKEN_TYPE_EXCEPTION = "All Tokens Must Be Of Some Type";
  public static final String MESSAGE_FOR_ILLEGAL_TOKEN_OWNER_EXCEPTION = "All Tokens Must Have Owner";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_TOKEN_TYPE_ATTRIBUTE = "Token Type Attribute Cannot Be Null";
  public static final String MESSAGE_FOR_INVALID_VALUE_FOR_STATUS_ATTRIBUTE = "Status Attribute Cannot Be Null";

  public static final String SUCCESSFULLY_ACTIVATED_2FA_RESPONSE = "You Have Successfully Enabled Your Second Authentication Factor. From Now On You Will Have To Use The Six Digit Code From Your Authenticator App To Log In";
  public static final String SUCCESSFULLY_REMOVED_2FA_RESPONSE = "You Have Successfully Deactivated Your Second Authentication Factor. From Now On You Will Not Be Required To Use The Six Digit Code From Your Authenticator App To Log In";

  private static final String SPECIFICALLY_FOR_FIRST_NAME = " - For The First Name";
  private static final String SPECIFICALLY_FOR_MIDDLE_NAME = " - For The Middle Name";
  private static final String SPECIFICALLY_FOR_SURNAME = " - For The Surname";

  private static final String MESSAGE_FOR_INVALID_VALUE_FOR_PHONE_NUMBER_EXCEPTION = "The Provided Phone Number Does Not Exist Or It Was Misspelled";
  private static final String MESSAGE_FOR_ALREADY_USED_PHONE_NUMBER_EXCEPTION = "This Phone Number Is Already Used";

  public static void validateElectorName(ElectorName electorName) {
    validateProperty(REGEX_FOR_NAME, electorName.getFirstName(),
        new InvalidValueForNameException(MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION + SPECIFICALLY_FOR_FIRST_NAME));

    validateProperty(REGEX_FOR_NAME, electorName.getMiddleName(),
        new InvalidValueForNameException(MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION + SPECIFICALLY_FOR_MIDDLE_NAME));

    validateProperty(REGEX_FOR_NAME, electorName.getSurname(),
        new InvalidValueForNameException(MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION + SPECIFICALLY_FOR_SURNAME));
  }

  public static void validateSupervisorName(SupervisorDTO supervisorDTO) {
    validateProperty(REGEX_FOR_NAME, supervisorDTO.getFirstName(),
        new InvalidValueForNameException(MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION + SPECIFICALLY_FOR_FIRST_NAME));

    validateProperty(REGEX_FOR_NAME, supervisorDTO.getSurname(),
        new InvalidValueForNameException(MESSAGE_FOR_INVALID_VALUE_FOR_NAME_EXCEPTION + SPECIFICALLY_FOR_SURNAME));
  }

  public static void validateEmail(String email) {
    validateProperty(REGEX_FOR_EMAIL, email, new InvalidValueForEmailException(MESSAGE_FOR_INVALID_VALUE_FOR_EMAIL_EXCEPTION));
  }

  public static void validateEmailIsUnique(SupervisorRepository supervisorRepository, ElectorRepository electorRepository, String email) {
    final boolean supervisorIsPresentByEmail = supervisorRepository.findSupervisorByEmail(email).isPresent();
    final boolean electorIsPresentByEmail = electorRepository.findElectorByEmail(email).isPresent();
    if (supervisorIsPresentByEmail || electorIsPresentByEmail) {
      throw new AlreadyUsedEmailException(MESSAGE_FOR_ALREADY_USED_EMAIL_EXCEPTION);
    }
  }

  public static void validatePhoneNumber(String phoneNumber) {
    validateProperty(REGEX_FOR_PHONE_NUMBER, phoneNumber,
        new InvalidValueForPhoneNumberException(MESSAGE_FOR_INVALID_VALUE_FOR_PHONE_NUMBER_EXCEPTION));
  }

  public static void validatePhoneNumberIsUnique(SupervisorRepository supervisorRepository, ElectorRepository electorRepository, String phoneNumber) {
    final boolean supervisorIsPresentByPhoneNumber = supervisorRepository.findSupervisorByPhoneNumber(phoneNumber).isPresent();
    final boolean electorIsPresentByPhoneNumber = electorRepository.findElectorByPhoneNumber(phoneNumber).isPresent();
    if (supervisorIsPresentByPhoneNumber || electorIsPresentByPhoneNumber) {
      throw new AlreadyUsedPhoneNumberException(MESSAGE_FOR_ALREADY_USED_PHONE_NUMBER_EXCEPTION);
    }
  }

  public static void validateProperty(String regex, String property, RuntimeException exception) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(property);
    if (!matcher.matches()) {
      throw exception;
    }
  }

  public static boolean isNullOrBlank(String value) {
    return (value == null) || value.isBlank();
  }

  public static boolean isNull(Integer value) {
    return (value == null);
  }
}
