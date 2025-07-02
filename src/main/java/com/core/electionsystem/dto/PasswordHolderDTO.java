package com.core.electionsystem.dto;

import java.util.Arrays;
import java.util.Objects;

public class PasswordHolderDTO {

  private char[] newPasswordFirstInput;
  private char[] newPasswordSecondInput;
  private String secretAnswer;

  public PasswordHolderDTO() {
    // Default Empty Constructor
  }

  public PasswordHolderDTO(char[] newPasswordFirstInput, char[] newPasswordSecondInput, String secretAnswer) {
    this.newPasswordFirstInput = newPasswordFirstInput;
    this.newPasswordSecondInput = newPasswordSecondInput;
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
    result = (prime * result) + Arrays.hashCode(newPasswordFirstInput);
    result = (prime * result) + Arrays.hashCode(newPasswordSecondInput);
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
    PasswordHolderDTO other = (PasswordHolderDTO) object;
    return Arrays.equals(newPasswordFirstInput, other.newPasswordFirstInput) && Arrays.equals(newPasswordSecondInput, other.newPasswordSecondInput)
        && Objects.equals(secretAnswer, other.secretAnswer);
  }

  @Override
  public String toString() {
    return "PasswordHolderDTO [newPasswordFirstInput=" + Arrays.toString(newPasswordFirstInput) + ", newPasswordSecondInput="
        + Arrays.toString(newPasswordSecondInput) + ", secretAnswer=" + secretAnswer + "]";
  }
}
