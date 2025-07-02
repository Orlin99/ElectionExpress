package com.core.electionsystem.dto.mfa;

import java.util.Arrays;
import java.util.Objects;

public class TwoFactorAuthenticationRemoverForElectorDTO {

  private char[] electorId;
  private String email;
  private char[] passwordFirstInput;
  private char[] passwordSecondInput;
  private String secretAnswer;

  public TwoFactorAuthenticationRemoverForElectorDTO() {
    // Default Empty Constructor
  }

  // @formatter:off
  public TwoFactorAuthenticationRemoverForElectorDTO(char[] electorId, String email, char[] passwordFirstInput, char[] passwordSecondInput, String secretAnswer) {
    this.electorId = electorId;
    this.email = email;
    this.passwordFirstInput = passwordFirstInput;
    this.passwordSecondInput = passwordSecondInput;
    this.secretAnswer = secretAnswer;
  }
  // @formatter:on

  public char[] getElectorId() {
    return electorId;
  }

  public void setElectorId(char[] electorId) {
    this.electorId = electorId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
    result = (prime * result) + Arrays.hashCode(electorId);
    result = (prime * result) + Objects.hash(email);
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
    TwoFactorAuthenticationRemoverForElectorDTO other = (TwoFactorAuthenticationRemoverForElectorDTO) object;
    return Arrays.equals(electorId, other.electorId) && Objects.equals(email, other.email)
        && Arrays.equals(passwordFirstInput, other.passwordFirstInput) && Arrays.equals(passwordSecondInput, other.passwordSecondInput)
        && Objects.equals(secretAnswer, other.secretAnswer);
  }

  @Override
  public String toString() {
    return "TwoFactorAuthenticationRemoverForElectorDTO [electorId=" + Arrays.toString(electorId) + ", email=" + email + ", passwordFirstInput="
        + Arrays.toString(passwordFirstInput) + ", passwordSecondInput=" + Arrays.toString(passwordSecondInput) + ", secretAnswer=" + secretAnswer
        + "]";
  }
}
