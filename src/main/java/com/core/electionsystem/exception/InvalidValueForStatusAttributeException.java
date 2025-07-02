package com.core.electionsystem.exception;

public class InvalidValueForStatusAttributeException extends RuntimeException {

  public InvalidValueForStatusAttributeException(String message) {
    super(message);
  }

  public InvalidValueForStatusAttributeException(String message, Throwable cause) {
    super(message, cause);
  }
}
