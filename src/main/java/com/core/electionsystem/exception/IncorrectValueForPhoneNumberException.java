package com.core.electionsystem.exception;

public class IncorrectValueForPhoneNumberException extends RuntimeException {

  public IncorrectValueForPhoneNumberException(String message) {
    super(message);
  }

  public IncorrectValueForPhoneNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
