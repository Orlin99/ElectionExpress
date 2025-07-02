package com.core.electionsystem.elector.exception;

public class AlreadyUsedEgnException extends RuntimeException {

  public AlreadyUsedEgnException(String message) {
    super(message);
  }

  public AlreadyUsedEgnException(String message, Throwable cause) {
    super(message, cause);
  }
}
