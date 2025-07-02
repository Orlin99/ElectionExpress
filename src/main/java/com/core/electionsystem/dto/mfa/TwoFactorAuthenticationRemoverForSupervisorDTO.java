package com.core.electionsystem.dto.mfa;

import java.util.Arrays;
import java.util.Objects;

public class TwoFactorAuthenticationRemoverForSupervisorDTO {

  private String supervisorEmail;
  private char[] passwordFirstInput;
  private char[] passwordSecondInput;
  private String secretAnswer;

  public TwoFactorAuthenticationRemoverForSupervisorDTO() {
    // Default Empty Constructor
  }

  // @formatter:off
  public TwoFactorAuthenticationRemoverForSupervisorDTO(String supervisorEmail, char[] passwordFirstInput, char[] passwordSecondInput, String secretAnswer) {
    this.supervisorEmail = supervisorEmail;
    this.passwordFirstInput = passwordFirstInput;
    this.passwordSecondInput = passwordSecondInput;
    this.secretAnswer = secretAnswer;
  }
  // @formatter:on

  public String getSupervisorEmail() {
    return supervisorEmail;
  }

  public void setSupervisorEmail(String supervisorEmail) {
    this.supervisorEmail = supervisorEmail;
  }

  public char[] getPasswordFirstInput() {
    return passwordFirstInput;
  }

  public void setPasswordFirstInput(char[] passwordFirstInput) {
    this.passwordFirstInput = passwordFirstInput;
  }

  public char[] getPasswordSecondInput() {
    return passwordSecondInput;
  }

  public void setPasswordSecondInput(char[] passwordSecondInput) {
    this.passwordSecondInput = passwordSecondInput;
  }

  public String getSecretAnswer() {
    return secretAnswer;
  }

  public void setSecretAnswer(String secretAnswer) {
    this.secretAnswer = secretAnswer;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Objects.hash(supervisorEmail);
    result = (prime * result) + Arrays.hashCode(passwordFirstInput);
    result = (prime * result) + Arrays.hashCode(passwordSecondInput);
    result = (prime * result) + Objects.hash(secretAnswer);
    return result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (getClass() != object.getClass()) {
      return false;
    }
    TwoFactorAuthenticationRemoverForSupervisorDTO other = (TwoFactorAuthenticationRemoverForSupervisorDTO) object;
    return Objects.equals(supervisorEmail, other.supervisorEmail) && Arrays.equals(passwordFirstInput, other.passwordFirstInput)
        && Arrays.equals(passwordSecondInput, other.passwordSecondInput) && Objects.equals(secretAnswer, other.secretAnswer);
  }

  @Override
  public String toString() {
    return "TwoFactorAuthenticationRemoverForSupervisorDTO [supervisorEmail=" + supervisorEmail + ", passwordFirstInput="
        + Arrays.toString(passwordFirstInput) + ", passwordSecondInput=" + Arrays.toString(passwordSecondInput) + ", secretAnswer=" + secretAnswer
        + "]";
  }
}
