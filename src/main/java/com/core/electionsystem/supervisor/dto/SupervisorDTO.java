package com.core.electionsystem.supervisor.dto;

import java.util.List;
import java.util.Objects;

import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.model.properties.SupervisorMetadata;

public class SupervisorDTO {

  private Long id;
  private String firstName;
  private String surname;
  private String supervisorEmail;
  private String passwordHash;
  private String secretKey;
  private String secretAnswer;
  private String supervisorPhone;
  private SupervisorMetadata supervisorMetadata;
  private List<SupervisorBearerToken> tokens;

  public SupervisorDTO() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public SupervisorDTO(Long id, String firstName, String surname, String supervisorEmail, String passwordHash, String secretKey, String secretAnswer,
      String supervisorPhone, SupervisorMetadata supervisorMetadata, List<SupervisorBearerToken> tokens) {
    this.id = id;
    this.firstName = firstName;
    this.surname = surname;
    this.supervisorEmail = supervisorEmail;
    this.passwordHash = passwordHash;
    this.secretKey = secretKey;
    this.secretAnswer = secretAnswer;
    this.supervisorPhone = supervisorPhone;
    this.supervisorMetadata = supervisorMetadata;
    this.tokens = tokens;
  }

  @SuppressWarnings("java:S107")
  public SupervisorDTO(String firstName, String surname, String supervisorEmail, String passwordHash, String secretKey, String secretAnswer,
      String supervisorPhone, SupervisorMetadata supervisorMetadata, List<SupervisorBearerToken> tokens) {
    this.firstName = firstName;
    this.surname = surname;
    this.supervisorEmail = supervisorEmail;
    this.passwordHash = passwordHash;
    this.secretKey = secretKey;
    this.secretAnswer = secretAnswer;
    this.supervisorPhone = supervisorPhone;
    this.supervisorMetadata = supervisorMetadata;
    this.tokens = tokens;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getSupervisorEmail() {
    return supervisorEmail;
  }

  public void setSupervisorEmail(String supervisorEmail) {
    this.supervisorEmail = supervisorEmail;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getSecretAnswer() {
    return secretAnswer;
  }

  public void setSecretAnswer(String secretAnswer) {
    this.secretAnswer = secretAnswer;
  }

  public String getSupervisorPhone() {
    return supervisorPhone;
  }

  public void setSupervisorPhone(String supervisorPhone) {
    this.supervisorPhone = supervisorPhone;
  }

  public SupervisorMetadata getSupervisorMetadata() {
    return supervisorMetadata;
  }

  public void setSupervisorMetadata(SupervisorMetadata supervisorMetadata) {
    this.supervisorMetadata = supervisorMetadata;
  }

  public List<SupervisorBearerToken> getTokens() {
    return tokens;
  }

  public void setTokens(List<SupervisorBearerToken> tokens) {
    this.tokens = tokens;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, surname, supervisorEmail, passwordHash, secretKey, secretAnswer, supervisorPhone, supervisorMetadata, tokens);
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
    SupervisorDTO other = (SupervisorDTO) object;
    return Objects.equals(id, other.id) && Objects.equals(firstName, other.firstName) && Objects.equals(surname, other.surname)
        && Objects.equals(supervisorEmail, other.supervisorEmail) && Objects.equals(passwordHash, other.passwordHash)
        && Objects.equals(secretKey, other.secretKey) && Objects.equals(secretAnswer, other.secretAnswer)
        && Objects.equals(supervisorPhone, other.supervisorPhone) && Objects.equals(supervisorMetadata, other.supervisorMetadata)
        && Objects.equals(tokens, other.tokens);
  }

  @Override
  public String toString() {
    return "SupervisorDTO [id=" + id + ", firstName=" + firstName + ", surname=" + surname + ", supervisorEmail=" + supervisorEmail
        + ", passwordHash=" + passwordHash + ", secretKey=" + secretKey + ", secretAnswer=" + secretAnswer + ", supervisorPhone=" + supervisorPhone
        + ", supervisorMetadata=" + supervisorMetadata + ", tokens=" + tokens.size() + "]";
  }
}
