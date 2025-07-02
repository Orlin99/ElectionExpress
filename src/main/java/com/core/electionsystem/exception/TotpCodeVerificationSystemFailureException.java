package com.core.electionsystem.exception;

public class TotpCodeVerificationSystemFailureException extends RuntimeException {

  public TotpCodeVerificationSystemFailureException(String message) {
    super(message);
  }

  public TotpCodeVerificationSystemFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
