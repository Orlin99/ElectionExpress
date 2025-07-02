package com.core.electionsystem.election.exception;

public class AlreadyVotedException extends RuntimeException {

  public AlreadyVotedException(String message) {
    super(message);
  }

  public AlreadyVotedException(String message, Throwable cause) {
    super(message, cause);
  }
}
