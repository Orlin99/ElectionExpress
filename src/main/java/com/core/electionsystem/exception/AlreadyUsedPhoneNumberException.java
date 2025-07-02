package com.core.electionsystem.exception;

public class AlreadyUsedPhoneNumberException extends RuntimeException {

  public AlreadyUsedPhoneNumberException(String message) {
    super(message);
  }

  public AlreadyUsedPhoneNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
