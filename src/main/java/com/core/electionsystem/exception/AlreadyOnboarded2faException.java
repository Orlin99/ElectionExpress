package com.core.electionsystem.exception;

public class AlreadyOnboarded2faException extends RuntimeException {

  public AlreadyOnboarded2faException(String message) {
    super(message);
  }

  public AlreadyOnboarded2faException(String message, Throwable cause) {
    super(message, cause);
  }
}
