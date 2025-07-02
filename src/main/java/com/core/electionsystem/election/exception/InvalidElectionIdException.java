package com.core.electionsystem.election.exception;

public class InvalidElectionIdException extends RuntimeException {

  public InvalidElectionIdException(String message) {
    super(message);
  }

  public InvalidElectionIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
