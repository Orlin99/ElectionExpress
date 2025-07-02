package com.core.electionsystem.dto.recovery;

import java.util.Arrays;
import java.util.Objects;

public class SecretAnswerRecovererDTO {

  private String email;
  private char[] userId;
  private String phoneNumber;

  public SecretAnswerRecovererDTO() {
    // Default Empty Constructor
  }

  public SecretAnswerRecovererDTO(String email, char[] userId, String phoneNumber) {
    this.email = email;
    this.userId = userId;
    this.phoneNumber = phoneNumber;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(userId);
    result = (prime * result) + Objects.hash(email, phoneNumber);
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
    SecretAnswerRecovererDTO other = (SecretAnswerRecovererDTO) object;
    return Objects.equals(email, other.email) && Arrays.equals(userId, other.userId) && Objects.equals(phoneNumber, other.phoneNumber);
  }

  @Override
  public String toString() {
    return "SecretAnswerRecovererDTO [email=" + email + ", userId=" + Arrays.toString(userId) + ", phoneNumber=" + phoneNumber + "]";
  }
}
