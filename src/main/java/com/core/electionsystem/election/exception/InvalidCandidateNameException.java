package com.core.electionsystem.election.exception;

public class InvalidCandidateNameException extends RuntimeException {

  public InvalidCandidateNameException(String message) {
    super(message);
  }

  public InvalidCandidateNameException(String message, Throwable cause) {
    super(message, cause);
  }
}
