package com.core.electionsystem.exception;

public class AlreadyUsedEmailException extends RuntimeException {

  public AlreadyUsedEmailException(String message) {
    super(message);
  }

  public AlreadyUsedEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
