package com.core.electionsystem.exception;

public class InvalidValueForTotpCodeException extends RuntimeException {

  public InvalidValueForTotpCodeException(String message) {
    super(message);
  }

  public InvalidValueForTotpCodeException(String message, Throwable cause) {
    super(message, cause);
  }
}
