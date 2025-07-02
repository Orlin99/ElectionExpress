package com.core.electionsystem.exception;

public class IllegalStateForTokenTypeException extends RuntimeException {

  public IllegalStateForTokenTypeException(String message) {
    super(message);
  }

  public IllegalStateForTokenTypeException(String message, Throwable cause) {
    super(message, cause);
  }
}
