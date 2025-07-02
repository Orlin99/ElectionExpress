package com.core.electionsystem.exception;

public class InvalidValueForEmailException extends RuntimeException {

  public InvalidValueForEmailException(String message) {
    super(message);
  }

  public InvalidValueForEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
