package com.core.electionsystem.exception;

public class InvalidArgumentsForSupervisorException extends RuntimeException {

  public InvalidArgumentsForSupervisorException(String message) {
    super(message);
  }

  public InvalidArgumentsForSupervisorException(String message, Throwable cause) {
    super(message, cause);
  }
}
