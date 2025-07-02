package com.core.electionsystem.election.exception;

public class InvalidDescriptionException extends RuntimeException {

  public InvalidDescriptionException(String message) {
    super(message);
  }

  public InvalidDescriptionException(String message, Throwable cause) {
    super(message, cause);
  }
}
