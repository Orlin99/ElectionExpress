package com.core.electionsystem.election.exception;

public class NonExistentElectionException extends RuntimeException {

  public NonExistentElectionException(String message) {
    super(message);
  }

  public NonExistentElectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
