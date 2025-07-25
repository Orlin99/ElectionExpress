package com.core.electionsystem.exception;

public class UnauthenticatedUserException extends RuntimeException {

  public UnauthenticatedUserException(String message) {
    super(message);
  }

  public UnauthenticatedUserException(String message, Throwable cause) {
    super(message, cause);
  }
}
