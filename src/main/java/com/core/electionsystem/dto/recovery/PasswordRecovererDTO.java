package com.core.electionsystem.dto.recovery;

import java.util.Arrays;
import java.util.Objects;

public class PasswordRecovererDTO {

  private String email;
  private char[] userId;
  private String phoneNumber;
  private String secretAnswer;

  public PasswordRecovererDTO() {
    // Default Empty Constructor
  }

  public PasswordRecovererDTO(String email, char[] userId, String phoneNumber, String secretAnswer) {
    this.email = email;
    this.userId = userId;
    this.phoneNumber = phoneNumber;
    this.secretAnswer = secretAnswer;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public char[] getUserId() {
    return userId;
  }

  public void setUserId(char[] userId) {
    this.userId = userId;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
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
    result = (prime * result) + Arrays.hashCode(userId);
    result = (prime * result) + Objects.hash(email, phoneNumber, secretAnswer);
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
    PasswordRecovererDTO other = (PasswordRecovererDTO) object;
    return Objects.equals(email, other.email) && Arrays.equals(userId, other.userId) && Objects.equals(phoneNumber, other.phoneNumber)
        && Objects.equals(secretAnswer, other.secretAnswer);
  }

  @Override
  public String toString() {
    return "PasswordRecovererDTO [email=" + email + ", userId=" + Arrays.toString(userId) + ", phoneNumber=" + phoneNumber + ", secretAnswer="
        + secretAnswer + "]";
  }
}
