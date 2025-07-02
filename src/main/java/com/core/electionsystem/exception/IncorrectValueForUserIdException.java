package com.core.electionsystem.exception;

public class IncorrectValueForUserIdException extends RuntimeException {

  public IncorrectValueForUserIdException(String message) {
    super(message);
  }

  public IncorrectValueForUserIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
