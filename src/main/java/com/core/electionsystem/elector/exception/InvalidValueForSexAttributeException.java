package com.core.electionsystem.elector.exception;

public class InvalidValueForSexAttributeException extends RuntimeException {

  public InvalidValueForSexAttributeException(String message) {
    super(message);
  }

  public InvalidValueForSexAttributeException(String message, Throwable cause) {
    super(message, cause);
  }
}
