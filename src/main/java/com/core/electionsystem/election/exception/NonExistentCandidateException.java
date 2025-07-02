package com.core.electionsystem.election.exception;

public class NonExistentCandidateException extends RuntimeException {

  public NonExistentCandidateException(String message) {
    super(message);
  }

  public NonExistentCandidateException(String message, Throwable cause) {
    super(message, cause);
  }
}
