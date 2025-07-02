package com.core.electionsystem.exception;

public class InvalidValueForPhoneNumberException extends RuntimeException {

  public InvalidValueForPhoneNumberException(String message) {
    super(message);
  }

  public InvalidValueForPhoneNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
