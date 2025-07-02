package com.core.electionsystem.supervisor.exception;

public class NonExistentSupervisorException extends RuntimeException {

  public NonExistentSupervisorException(String message) {
    super(message);
  }

  public NonExistentSupervisorException(String message, Throwable cause) {
    super(message, cause);
  }
}
