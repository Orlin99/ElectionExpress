package com.core.electionsystem.election.exception;

public class InvalidStatusException extends RuntimeException {

  public InvalidStatusException(String message) {
    super(message);
  }

  public InvalidStatusException(String message, Throwable cause) {
    super(message, cause);
  }
}
