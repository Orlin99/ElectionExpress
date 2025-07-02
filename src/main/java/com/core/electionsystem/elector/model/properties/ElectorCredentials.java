package com.core.electionsystem.elector.model.properties;

import java.util.Objects;

import com.core.electionsystem.elector.model.Elector;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;

@Entity(name = "ElectorCredentials")
@Table(name = "elector_credentials", uniqueConstraints = { @UniqueConstraint(name = "email_unique", columnNames = "email"),
    @UniqueConstraint(name = "phone_number_unique", columnNames = "phone_number") })
public class ElectorCredentials {

  @Id
  @SequenceGenerator(name = "elector_credentials_sequence", sequenceName = "elector_credentials_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elector_credentials_sequence")
  private Long electorCredentialsId;

  @Column(name = "email", nullable = false, updatable = false, columnDefinition = "TEXT")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid Value For The Email Address")
  private String email;

  @Column(name = "password_hash", nullable = false, columnDefinition = "VARCHAR(60)")
  private String passwordHash;

  @Column(name = "secret_key", columnDefinition = "TEXT")
  private String secretKey;

  @Column(name = "secret_answer", nullable = false, columnDefinition = "TEXT")
  private String secretAnswer;

  @Column(name = "phone_number", nullable = false, columnDefinition = "VARCHAR(20)")
  @Pattern(regexp = "^\\+?[0-9. ()-]{7,20}$", message = "Invalid Value For The Phone Number")
  private String phoneNumber;

  @JsonBackReference
  @OneToOne(mappedBy = "electorCredentials", cascade = CascadeType.ALL, orphanRemoval = true)
  private Elector elector;

  public ElectorCredentials() {
    // Default Empty Constructor
  }

  public ElectorCredentials(Long electorCredentialsId, String email, String passwordHash, String secretKey, String secretAnswer, String phoneNumber,
      Elector elector) {
    this.electorCredentialsId = electorCredentialsId;
    this.email = email;
    this.passwordHash = passwordHash;
    this.secretKey = secretKey;
    this.secretAnswer = secretAnswer;
    this.phoneNumber = phoneNumber;
    this.elector = elector;
  }

  public ElectorCredentials(String email, String passwordHash, String secretKey, String secretAnswer, String phoneNumber, Elector elector) {
    this.email = email;
    this.passwordHash = passwordHash;
    this.secretKey = secretKey;
    this.secretAnswer = secretAnswer;
    this.phoneNumber = phoneNumber;
    this.elector = elector;
  }

  public Long getElectorCredentialsId() {
    return electorCredentialsId;
  }

  public void setElectorCredentialsId(Long electorCredentialsId) {
    this.electorCredentialsId = electorCredentialsId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Elector getElector() {
    return elector;
  }

  public void setElector(Elector elector) {
    this.elector = elector;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorCredentialsId, email, passwordHash, secretKey, secretAnswer, phoneNumber);
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
    ElectorCredentials other = (ElectorCredentials) object;
    return Objects.equals(electorCredentialsId, other.electorCredentialsId) && Objects.equals(email, other.email)
        && Objects.equals(passwordHash, other.passwordHash) && Objects.equals(secretKey, other.secretKey)
        && Objects.equals(secretAnswer, other.secretAnswer) && Objects.equals(phoneNumber, other.phoneNumber);
  }

  @Override
  public String toString() {
    return "ElectorCredentials [electorCredentialsId=" + electorCredentialsId + ", email=" + email + ", passwordHash=" + passwordHash + ", secretKey="
        + secretKey + ", secretAnswer=" + secretAnswer + ", phoneNumber=" + phoneNumber + ", elector=" + elector.getElectorId() + "]";
  }
}
