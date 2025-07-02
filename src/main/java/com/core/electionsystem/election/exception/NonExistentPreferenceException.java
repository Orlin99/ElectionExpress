package com.core.electionsystem.election.exception;

public class NonExistentPreferenceException extends RuntimeException {

  public NonExistentPreferenceException(String message) {
    super(message);
  }

  public NonExistentPreferenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
