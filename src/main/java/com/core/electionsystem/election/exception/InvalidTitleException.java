package com.core.electionsystem.election.exception;

public class InvalidTitleException extends RuntimeException {

  public InvalidTitleException(String message) {
    super(message);
  }

  public InvalidTitleException(String message, Throwable cause) {
    super(message, cause);
  }
}
