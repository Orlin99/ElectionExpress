package com.core.electionsystem.elector.exception;

public class UserIsNotAdultException extends RuntimeException {

  public UserIsNotAdultException(String message) {
    super(message);
  }

  public UserIsNotAdultException(String message, Throwable cause) {
    super(message, cause);
  }
}
