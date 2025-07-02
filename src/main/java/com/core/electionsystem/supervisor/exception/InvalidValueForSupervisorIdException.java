package com.core.electionsystem.supervisor.exception;

public class InvalidValueForSupervisorIdException extends RuntimeException {

  public InvalidValueForSupervisorIdException(String message) {
    super(message);
  }

  public InvalidValueForSupervisorIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
