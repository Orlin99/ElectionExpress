package com.core.electionsystem.election.exception;

public class InvalidCandidateIdException extends RuntimeException {

  public InvalidCandidateIdException(String message) {
    super(message);
  }

  public InvalidCandidateIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
