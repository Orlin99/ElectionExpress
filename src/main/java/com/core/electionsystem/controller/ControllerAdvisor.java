package com.core.electionsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.core.electionsystem.election.exception.AlreadyVotedException;
import com.core.electionsystem.election.exception.InvalidCandidateIdException;
import com.core.electionsystem.election.exception.InvalidCandidateNameException;
import com.core.electionsystem.election.exception.InvalidDescriptionException;
import com.core.electionsystem.election.exception.InvalidElectionEventTimingException;
import com.core.electionsystem.election.exception.InvalidElectionIdException;
import com.core.electionsystem.election.exception.InvalidPreferenceIdException;
import com.core.electionsystem.election.exception.InvalidPreferenceNameException;
import com.core.electionsystem.election.exception.InvalidStatusException;
import com.core.electionsystem.election.exception.InvalidTitleException;
import com.core.electionsystem.election.exception.NonExistentCandidateException;
import com.core.electionsystem.election.exception.NonExistentElectionException;
import com.core.electionsystem.election.exception.NonExistentPreferenceException;
import com.core.electionsystem.elector.exception.AlreadyUsedDocumentIdException;
import com.core.electionsystem.elector.exception.AlreadyUsedEgnException;
import com.core.electionsystem.elector.exception.EmptyDateParameterException;
import com.core.electionsystem.elector.exception.EmptyNameParameterException;
import com.core.electionsystem.elector.exception.EmptyRegionParameterException;
import com.core.electionsystem.elector.exception.EmptySexParameterException;
import com.core.electionsystem.elector.exception.IllegalElectorPropertyException;
import com.core.electionsystem.elector.exception.InvalidDateParameterException;
import com.core.electionsystem.elector.exception.InvalidDocumentIdException;
import com.core.electionsystem.elector.exception.InvalidEgnException;
import com.core.electionsystem.elector.exception.InvalidSexParameterException;
import com.core.electionsystem.elector.exception.InvalidSexParameterLengthException;
import com.core.electionsystem.elector.exception.InvalidValueForDocumentIdException;
import com.core.electionsystem.elector.exception.InvalidValueForEgnException;
import com.core.electionsystem.elector.exception.InvalidValueForSexAttributeException;
import com.core.electionsystem.elector.exception.NonExistentElectorException;
import com.core.electionsystem.elector.exception.UserIsNotAdultException;
import com.core.electionsystem.exception.AlreadyOnboarded2faException;
import com.core.electionsystem.exception.AlreadyUsedEmailException;
import com.core.electionsystem.exception.AlreadyUsedPhoneNumberException;
import com.core.electionsystem.exception.IllegalStateForTokenOwnerException;
import com.core.electionsystem.exception.IllegalStateForTokenTypeException;
import com.core.electionsystem.exception.IllegalStateForTokenValueException;
import com.core.electionsystem.exception.IncorrectPasswordException;
import com.core.electionsystem.exception.IncorrectSecretAnswerException;
import com.core.electionsystem.exception.IncorrectValueForPhoneNumberException;
import com.core.electionsystem.exception.IncorrectValueForUserIdException;
import com.core.electionsystem.exception.InvalidArgumentsForElectorException;
import com.core.electionsystem.exception.InvalidArgumentsForSupervisorException;
import com.core.electionsystem.exception.InvalidPasswordException;
import com.core.electionsystem.exception.InvalidSecurityAlgorithmException;
import com.core.electionsystem.exception.InvalidValueForEmailException;
import com.core.electionsystem.exception.InvalidValueForNameException;
import com.core.electionsystem.exception.InvalidValueForPhoneNumberException;
import com.core.electionsystem.exception.InvalidValueForSecretAnswerException;
import com.core.electionsystem.exception.InvalidValueForStatusAttributeException;
import com.core.electionsystem.exception.InvalidValueForTokenTypeAttributeException;
import com.core.electionsystem.exception.InvalidValueForTotpCodeException;
import com.core.electionsystem.exception.InvalidValueForUserIdException;
import com.core.electionsystem.exception.NonExistentJsonWebTokenException;
import com.core.electionsystem.exception.NonExistentUserException;
import com.core.electionsystem.exception.NonExistentUserRoleException;
import com.core.electionsystem.exception.OtpAuthURISchemeGenerationException;
import com.core.electionsystem.exception.QrCodeGenerationFailureException;
import com.core.electionsystem.exception.SecretKeyDecryptionException;
import com.core.electionsystem.exception.SecretKeyEncryptionException;
import com.core.electionsystem.exception.TotpCodeVerificationSystemFailureException;
import com.core.electionsystem.exception.UnauthenticatedUserException;
import com.core.electionsystem.exception.UnknownAuthorityException;
import com.core.electionsystem.supervisor.exception.InvalidValueForSupervisorIdException;
import com.core.electionsystem.supervisor.exception.NonExistentSupervisorException;

import io.jsonwebtoken.security.InvalidKeyException;

@SuppressWarnings("java:S6539")
@ControllerAdvice(basePackages = "com.core.electionsystem")
public class ControllerAdvisor {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(UnauthenticatedUserException.class)
  public ResponseEntity<String> handleUnauthenticatedUserException(UnauthenticatedUserException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyUsedDocumentIdException.class)
  public ResponseEntity<String> handleAlreadyUsedDocumentIdException(AlreadyUsedDocumentIdException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyUsedEgnException.class)
  public ResponseEntity<String> handleAlreadyUsedEgnException(AlreadyUsedEgnException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyUsedEmailException.class)
  public ResponseEntity<String> handleAlreadyUsedEmailException(AlreadyUsedEmailException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyUsedPhoneNumberException.class)
  public ResponseEntity<String> handleAlreadyUsedPhoneNumberException(AlreadyUsedPhoneNumberException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(EmptyDateParameterException.class)
  public ResponseEntity<String> handleEmptyDateParameterException(EmptyDateParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(EmptyNameParameterException.class)
  public ResponseEntity<String> handleEmptyNameParameterException(EmptyNameParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(EmptyRegionParameterException.class)
  public ResponseEntity<String> handleEmptyRegionParameterException(EmptyRegionParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(EmptySexParameterException.class)
  public ResponseEntity<String> handleEmptySexParameterException(EmptySexParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(IncorrectValueForUserIdException.class)
  public ResponseEntity<String> handleIncorrectValueForUserIdException(IncorrectValueForUserIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(IncorrectPasswordException.class)
  public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(IncorrectSecretAnswerException.class)
  public ResponseEntity<String> handleIncorrectSecretAnswerException(IncorrectSecretAnswerException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(IncorrectValueForPhoneNumberException.class)
  public ResponseEntity<String> handleIncorrectValueForPhoneNumberException(IncorrectValueForPhoneNumberException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidDateParameterException.class)
  public ResponseEntity<String> handleInvalidDateParameterException(InvalidDateParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidSexParameterException.class)
  public ResponseEntity<String> handleInvalidSexParameterException(InvalidSexParameterException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidSexParameterLengthException.class)
  public ResponseEntity<String> handleInvalidSexParameterLengthException(InvalidSexParameterLengthException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidDocumentIdException.class)
  public ResponseEntity<String> handleInvalidDocumentIdException(InvalidDocumentIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForDocumentIdException.class)
  public ResponseEntity<String> handleInvalidValueForDocumentIdException(InvalidValueForDocumentIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidEgnException.class)
  public ResponseEntity<String> handleInvalidEgnException(InvalidEgnException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForUserIdException.class)
  public ResponseEntity<String> handleInvalidValueForUserIdException(InvalidValueForUserIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForSupervisorIdException.class)
  public ResponseEntity<String> handleInvalidValueForSupervisorIdException(InvalidValueForSupervisorIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForEgnException.class)
  public ResponseEntity<String> handleInvalidValueForEgnException(InvalidValueForEgnException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForEmailException.class)
  public ResponseEntity<String> handleInvalidValueForEmailException(InvalidValueForEmailException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForNameException.class)
  public ResponseEntity<String> handleInvalidValueForNameException(InvalidValueForNameException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForPhoneNumberException.class)
  public ResponseEntity<String> handleInvalidValueForPhoneNumberException(InvalidValueForPhoneNumberException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForSecretAnswerException.class)
  public ResponseEntity<String> handleInvalidValueForSecretAnswerException(InvalidValueForSecretAnswerException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentUserException.class)
  public ResponseEntity<String> handleNonExistentUserException(NonExistentUserException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentSupervisorException.class)
  public ResponseEntity<String> handleNonExistentSupervisorException(NonExistentSupervisorException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentElectorException.class)
  public ResponseEntity<String> handleNonExistentElectorException(NonExistentElectorException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(UserIsNotAdultException.class)
  public ResponseEntity<String> handleUserIsNotAdultException(UserIsNotAdultException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidSecurityAlgorithmException.class)
  public ResponseEntity<String> handleInvalidSecurityAlgorithmException(InvalidSecurityAlgorithmException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentUserRoleException.class)
  public ResponseEntity<String> handleNonExistentUserRoleException(NonExistentUserRoleException exception) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidArgumentsForSupervisorException.class)
  public ResponseEntity<String> handleInvalidArgumentsForSupervisorException(InvalidArgumentsForSupervisorException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidArgumentsForElectorException.class)
  public ResponseEntity<String> handleInvalidArgumentsForElectorException(InvalidArgumentsForElectorException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(UnknownAuthorityException.class)
  public ResponseEntity<String> handleUnknownAuthorityException(UnknownAuthorityException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }

  @ExceptionHandler(IllegalElectorPropertyException.class)
  public ResponseEntity<String> handleIllegalElectorPropertyException(IllegalElectorPropertyException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidKeyException.class)
  public ResponseEntity<String> handleInvalidKeyException(InvalidKeyException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(OtpAuthURISchemeGenerationException.class)
  public ResponseEntity<String> handleOtpAuthURISchemeGenerationException(OtpAuthURISchemeGenerationException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(QrCodeGenerationFailureException.class)
  public ResponseEntity<String> handleQrCodeGenerationFailureException(QrCodeGenerationFailureException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForTotpCodeException.class)
  public ResponseEntity<String> handleInvalidValueForTotpCodeException(InvalidValueForTotpCodeException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(SecretKeyEncryptionException.class)
  public ResponseEntity<String> handleSecretKeyEncryptionException(SecretKeyEncryptionException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(SecretKeyDecryptionException.class)
  public ResponseEntity<String> handleSecretKeyDecryptionException(SecretKeyDecryptionException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyOnboarded2faException.class)
  public ResponseEntity<String> handleAlreadyOnboarded2faException(AlreadyOnboarded2faException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }

  @ExceptionHandler(TotpCodeVerificationSystemFailureException.class)
  public ResponseEntity<String> handleTotpCodeVerificationSystemFailureException(TotpCodeVerificationSystemFailureException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(IllegalStateForTokenValueException.class)
  public ResponseEntity<String> handleIllegalStateForTokenValueException(IllegalStateForTokenValueException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(IllegalStateForTokenTypeException.class)
  public ResponseEntity<String> handleIllegalStateForTokenTypeException(IllegalStateForTokenTypeException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(IllegalStateForTokenOwnerException.class)
  public ResponseEntity<String> handleIllegalStateForTokenOwnerException(IllegalStateForTokenOwnerException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentJsonWebTokenException.class)
  public ResponseEntity<String> handleNonExistentJsonWebTokenException(NonExistentJsonWebTokenException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForTokenTypeAttributeException.class)
  public ResponseEntity<String> handleInvalidValueForTokenTypeAttributeException(InvalidValueForTokenTypeAttributeException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForStatusAttributeException.class)
  public ResponseEntity<String> handleInvalidValueForStatusAttributeException(InvalidValueForStatusAttributeException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidValueForSexAttributeException.class)
  public ResponseEntity<String> handleInvalidValueForSexAttributeException(InvalidValueForSexAttributeException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidTitleException.class)
  public ResponseEntity<String> handleInvalidTitleException(InvalidTitleException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidDescriptionException.class)
  public ResponseEntity<String> handleInvalidDescriptionException(InvalidDescriptionException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidElectionIdException.class)
  public ResponseEntity<String> handleInvalidElectionIdException(InvalidElectionIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidElectionEventTimingException.class)
  public ResponseEntity<String> handleInvalidElectionEventTimingException(InvalidElectionEventTimingException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentElectionException.class)
  public ResponseEntity<String> handleNonExistentElectionException(NonExistentElectionException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidCandidateIdException.class)
  public ResponseEntity<String> handleInvalidCandidateIdException(InvalidCandidateIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidStatusException.class)
  public ResponseEntity<String> handleInvalidStatusException(InvalidStatusException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidCandidateNameException.class)
  public ResponseEntity<String> handleInvalidCandidateNameException(InvalidCandidateNameException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentCandidateException.class)
  public ResponseEntity<String> handleNonExistentCandidateException(NonExistentCandidateException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidPreferenceIdException.class)
  public ResponseEntity<String> handleInvalidPreferenceIdException(InvalidPreferenceIdException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidPreferenceNameException.class)
  public ResponseEntity<String> handleInvalidPreferenceNameException(InvalidPreferenceNameException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(NonExistentPreferenceException.class)
  public ResponseEntity<String> handleNonExistentPreferenceException(NonExistentPreferenceException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyVotedException.class)
  public ResponseEntity<String> handleAlreadyVotedException(AlreadyVotedException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }
}
