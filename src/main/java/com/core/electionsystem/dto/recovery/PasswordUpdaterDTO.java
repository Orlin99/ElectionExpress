package com.core.electionsystem.dto.recovery;

import java.util.Arrays;
import java.util.Objects;

public class PasswordUpdaterDTO {

  private char[] userId;
  private String secretAnswer;
  private char[] newPasswordFirstInput;
  private char[] newPasswordSecondInput;

  public PasswordUpdaterDTO() {
    // Default Empty Constructor
  }

  public PasswordUpdaterDTO(char[] userId, String secretAnswer, char[] newPasswordFirstInput, char[] newPasswordSecondInput) {
    this.userId = userId;
    this.secretAnswer = secretAnswer;
    this.newPasswordFirstInput = newPasswordFirstInput;
    this.newPasswordSecondInput = newPasswordSecondInput;
  }

  public char[] getUserId() {
    return userId;
  }

  public void setUserId(char[] userId) {
    this.userId = userId;
  }

  public String getSecretAnswer() {
    return secretAnswer;
  }

  public void setSecretAnswer(String secretAnswer) {
    this.secretAnswer = secretAnswer;
  }

  public char[] getNewPasswordFirstInput() {
    return newPasswordFirstInput;
  }

  public void setNewPasswordFirstInput(char[] newPasswordFirstInput) {
    this.newPasswordFirstInput = newPasswordFirstInput;
  }

  public char[] getNewPasswordSecondInput() {
    return newPasswordSecondInput;
  }

  public void setNewPasswordSecondInput(char[] newPasswordSecondInput) {
    this.newPasswordSecondInput = newPasswordSecondInput;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(userId);
    result = (prime * result) + Objects.hash(secretAnswer);
    result = (prime * result) + Arrays.hashCode(newPasswordFirstInput);
    result = (prime * result) + Arrays.hashCode(newPasswordSecondInput);
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
    PasswordUpdaterDTO other = (PasswordUpdaterDTO) object;
    return Arrays.equals(userId, other.userId) && Objects.equals(secretAnswer, other.secretAnswer)
        && Arrays.equals(newPasswordFirstInput, other.newPasswordFirstInput) && Arrays.equals(newPasswordSecondInput, other.newPasswordSecondInput);
  }

  @Override
  public String toString() {
    return "PasswordUpdaterDTO [userId=" + Arrays.toString(userId) + ", secretAnswer=" + secretAnswer + ", newPasswordFirstInput="
        + Arrays.toString(newPasswordFirstInput) + ", newPasswordSecondInput=" + Arrays.toString(newPasswordSecondInput) + "]";
  }
}
