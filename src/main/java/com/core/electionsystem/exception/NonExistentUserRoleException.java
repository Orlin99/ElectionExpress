package com.core.electionsystem.exception;

public class NonExistentUserRoleException extends RuntimeException {

  public NonExistentUserRoleException(String message) {
    super(message);
  }

  public NonExistentUserRoleException(String message, Throwable cause) {
    super(message, cause);
  }
}
