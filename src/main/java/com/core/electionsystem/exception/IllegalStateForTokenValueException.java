package com.core.electionsystem.exception;

public class IllegalStateForTokenValueException extends RuntimeException {

  public IllegalStateForTokenValueException(String message) {
    super(message);
  }

  public IllegalStateForTokenValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
