package com.core.electionsystem.exception;

public class IncorrectPasswordException extends RuntimeException {

  public IncorrectPasswordException(String message) {
    super(message);
  }

  public IncorrectPasswordException(String message, Throwable cause) {
    super(message, cause);
  }
}
