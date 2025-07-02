package com.core.electionsystem.election.exception;

public class InvalidPreferenceNameException extends RuntimeException {

  public InvalidPreferenceNameException(String message) {
    super(message);
  }

  public InvalidPreferenceNameException(String message, Throwable cause) {
    super(message, cause);
  }
}
