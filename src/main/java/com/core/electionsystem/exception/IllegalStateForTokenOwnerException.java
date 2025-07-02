package com.core.electionsystem.exception;

public class IllegalStateForTokenOwnerException extends RuntimeException {

  public IllegalStateForTokenOwnerException(String message) {
    super(message);
  }

  public IllegalStateForTokenOwnerException(String message, Throwable cause) {
    super(message, cause);
  }
}
