package com.core.electionsystem.exception;

public class InvalidValueForTokenTypeAttributeException extends RuntimeException {

  public InvalidValueForTokenTypeAttributeException(String message) {
    super(message);
  }

  public InvalidValueForTokenTypeAttributeException(String message, Throwable cause) {
    super(message, cause);
  }
}
