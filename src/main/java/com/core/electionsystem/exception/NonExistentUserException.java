package com.core.electionsystem.exception;

public class NonExistentUserException extends RuntimeException {

  public NonExistentUserException(String message) {
    super(message);
  }

  public NonExistentUserException(String message, Throwable cause) {
    super(message, cause);
  }
}
