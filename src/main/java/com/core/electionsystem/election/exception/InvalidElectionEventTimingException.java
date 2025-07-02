package com.core.electionsystem.election.exception;

public class InvalidElectionEventTimingException extends RuntimeException {

  public InvalidElectionEventTimingException(String message) {
    super(message);
  }

  public InvalidElectionEventTimingException(String message, Throwable cause) {
    super(message, cause);
  }
}
