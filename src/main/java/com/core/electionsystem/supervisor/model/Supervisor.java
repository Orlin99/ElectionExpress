package com.core.electionsystem.supervisor.model;

import java.util.List;
import java.util.Objects;

import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.model.properties.SupervisorMetadata;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;

@Entity(name = "Supervisor")
@Table(name = "supervisor", uniqueConstraints = { @UniqueConstraint(name = "supervisor_email_unique", columnNames = "supervisor_email"),
    @UniqueConstraint(name = "supervisor_phone_unique", columnNames = "supervisor_phone") })
public class Supervisor {

  @Id
  @SequenceGenerator(name = "supervisor_sequence", sequenceName = "supervisor_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisor_sequence")
  private Long id;

  @Column(name = "first_name", nullable = false, columnDefinition = "TEXT")
  private String firstName;

  @Column(name = "surname", nullable = false, columnDefinition = "TEXT")
  private String surname;

  @Column(name = "supervisor_email", nullable = false, updatable = false, columnDefinition = "TEXT")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid Value For The Email Address")
  private String supervisorEmail;

  @Column(name = "password_hash", nullable = false, columnDefinition = "VARCHAR(60)")
  private String passwordHash;

  @Column(name = "secret_key", columnDefinition = "TEXT")
  private String secretKey;

  @Column(name = "secret_answer", nullable = false, columnDefinition = "TEXT")
  private String secretAnswer;

  @Column(name = "supervisor_phone", nullable = false, columnDefinition = "VARCHAR(20)")
  @Pattern(regexp = "^\\+?[0-9. ()-]{7,20}$", message = "Invalid Value For The Phone Number")
  private String supervisorPhone;

  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "supervisor_metadata", nullable = false, columnDefinition = "BIGINT")
  private SupervisorMetadata supervisorMetadata;

  @JsonManagedReference
  @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SupervisorBearerToken> tokens;

  public Supervisor() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public Supervisor(Long id, String firstName, String surname, String supervisorEmail, String passwordHash, String secretKey, String secretAnswer,
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
  public Supervisor(String firstName, String surname, String supervisorEmail, String passwordHash, String secretKey, String secretAnswer,
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

  public void addToken(SupervisorBearerToken token) {
    tokens.add(token);
    token.setSupervisor(this);
  }

  public void removeToken(SupervisorBearerToken token) {
    tokens.remove(token);
    token.setSupervisor(null);
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
    Supervisor other = (Supervisor) object;
    return Objects.equals(id, other.id) && Objects.equals(firstName, other.firstName) && Objects.equals(surname, other.surname)
        && Objects.equals(supervisorEmail, other.supervisorEmail) && Objects.equals(passwordHash, other.passwordHash)
        && Objects.equals(secretKey, other.secretKey) && Objects.equals(secretAnswer, other.secretAnswer)
        && Objects.equals(supervisorPhone, other.supervisorPhone) && Objects.equals(supervisorMetadata, other.supervisorMetadata)
        && Objects.equals(tokens, other.tokens);
  }

  @Override
  public String toString() {
    return "Supervisor [id=" + id + ", firstName=" + firstName + ", surname=" + surname + ", supervisorEmail=" + supervisorEmail + ", passwordHash="
        + passwordHash + ", secretKey=" + secretKey + ", secretAnswer=" + secretAnswer + ", supervisorPhone=" + supervisorPhone
        + ", supervisorMetadata=" + supervisorMetadata + ", tokens=" + tokens.size() + "]";
  }
}
