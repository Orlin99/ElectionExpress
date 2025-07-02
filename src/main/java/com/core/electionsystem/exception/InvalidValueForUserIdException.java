package com.core.electionsystem.exception;

public class InvalidValueForUserIdException extends RuntimeException {

  public InvalidValueForUserIdException(String message) {
    super(message);
  }

  public InvalidValueForUserIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
