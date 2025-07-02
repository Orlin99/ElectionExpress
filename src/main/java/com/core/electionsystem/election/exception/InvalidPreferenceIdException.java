package com.core.electionsystem.election.exception;

public class InvalidPreferenceIdException extends RuntimeException {

  public InvalidPreferenceIdException(String message) {
    super(message);
  }

  public InvalidPreferenceIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
