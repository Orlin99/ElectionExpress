package com.core.electionsystem.dto;

import java.util.Arrays;
import java.util.Objects;

public class SecretAnswerHolderDTO {

  private char[] inputPassword;
  private String newSecretAnswerFirstInput;
  private String newSecretAnswerSecondInput;

  public SecretAnswerHolderDTO() {
    // Default Empty Constructor
  }

  public SecretAnswerHolderDTO(char[] inputPassword, String newSecretAnswerFirstInput, String newSecretAnswerSecondInput) {
    this.inputPassword = inputPassword;
    this.newSecretAnswerFirstInput = newSecretAnswerFirstInput;
    this.newSecretAnswerSecondInput = newSecretAnswerSecondInput;
  }

  public char[] getInputPassword() {
    return inputPassword;
  }

  public void setInputPassword(char[] inputPassword) {
    this.inputPassword = inputPassword;
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
    result = (prime * result) + Arrays.hashCode(inputPassword);
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
    SecretAnswerHolderDTO other = (SecretAnswerHolderDTO) object;
    return Arrays.equals(inputPassword, other.inputPassword) && Objects.equals(newSecretAnswerFirstInput, other.newSecretAnswerFirstInput)
        && Objects.equals(newSecretAnswerSecondInput, other.newSecretAnswerSecondInput);
  }

  @Override
  public String toString() {
    return "SecretAnswerHolderDTO [inputPassword=" + Arrays.toString(inputPassword) + ", newSecretAnswerFirstInput=" + newSecretAnswerFirstInput
        + ", newSecretAnswerSecondInput=" + newSecretAnswerSecondInput + "]";
  }
}
