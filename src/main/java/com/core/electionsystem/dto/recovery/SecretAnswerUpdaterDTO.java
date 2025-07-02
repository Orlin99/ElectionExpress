package com.core.electionsystem.dto.recovery;

import java.util.Arrays;
import java.util.Objects;

public class SecretAnswerUpdaterDTO {

  private char[] userId;
  private String newSecretAnswerFirstInput;
  private String newSecretAnswerSecondInput;

  public SecretAnswerUpdaterDTO() {
    // Default Empty Constructor
  }

  public SecretAnswerUpdaterDTO(char[] userId, String newSecretAnswerFirstInput, String newSecretAnswerSecondInput) {
    this.userId = userId;
    this.newSecretAnswerFirstInput = newSecretAnswerFirstInput;
    this.newSecretAnswerSecondInput = newSecretAnswerSecondInput;
  }

  public char[] getUserId() {
    return userId;
  }

  public void setUserId(char[] userId) {
    this.userId = userId;
  }

  public String getNewSecretAnswerFirstInput() {
    return newSecretAnswerFirstInput;
  }

  public void setNewSecretAnswerFirstInput(String newSecretAnswerFirstInput) {
    this.newSecretAnswerFirstInput = newSecretAnswerFirstInput;
  }

  public String getNewSecretAnswerSecondInput() {
    return newSecretAnswerSecondInput;
  }

  public void setNewSecretAnswerSecondInput(String newSecretAnswerSecondInput) {
    this.newSecretAnswerSecondInput = newSecretAnswerSecondInput;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(userId);
    result = (prime * result) + Objects.hash(newSecretAnswerFirstInput, newSecretAnswerSecondInput);
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
    SecretAnswerUpdaterDTO other = (SecretAnswerUpdaterDTO) object;
    return Arrays.equals(userId, other.userId) && Objects.equals(newSecretAnswerFirstInput, other.newSecretAnswerFirstInput)
        && Objects.equals(newSecretAnswerSecondInput, other.newSecretAnswerSecondInput);
  }

  @Override
  public String toString() {
    return "SecretAnswerUpdaterDTO [userId=" + Arrays.toString(userId) + ", newSecretAnswerFirstInput=" + newSecretAnswerFirstInput
        + ", newSecretAnswerSecondInput=" + newSecretAnswerSecondInput + "]";
  }
}
