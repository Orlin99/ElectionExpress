package com.core.electionsystem.elector.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.model.properties.ElectorCredentials;
import com.core.electionsystem.elector.model.properties.ElectorMetadata;
import com.core.electionsystem.elector.model.properties.ElectorName;
import com.core.electionsystem.elector.model.properties.ElectorResidence;
import com.core.electionsystem.elector.model.properties.Sex;
import com.core.electionsystem.elector.utility.SexAttributeConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;

@Entity(name = "Elector")
@Table(name = "elector", uniqueConstraints = { @UniqueConstraint(name = "document_id_unique", columnNames = "document_id") })
public class Elector {

  @Id
  @Column(name = "elector_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(10)")
  @Pattern(regexp = "^\\d{10}$", message = "Invalid Value For The EGN")
  private String electorId;

  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "elector_name", nullable = false, columnDefinition = "BIGINT")
  private ElectorName electorName;

  @Column(name = "sex", nullable = false, updatable = false, columnDefinition = "CHAR(1)")
  @Convert(converter = SexAttributeConverter.class)
  private Sex sex;

  @Column(name = "date_of_birth", nullable = false, updatable = false, columnDefinition = "DATE")
  private LocalDate dateOfBirth;

  @Column(name = "document_id", nullable = false, columnDefinition = "VARCHAR(9)")
  @Pattern(regexp = "^\\d{9}$", message = "Invalid Value For The Personal Card Number")
  private String documentId;

  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "elector_residence", nullable = false, columnDefinition = "BIGINT")
  private ElectorResidence electorResidence;

  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "elector_credentials", nullable = false, columnDefinition = "BIGINT")
  private ElectorCredentials electorCredentials;

  @JsonManagedReference
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "elector_metadata", nullable = false, columnDefinition = "BIGINT")
  private ElectorMetadata electorMetadata;

  @JsonManagedReference
  @OneToMany(mappedBy = "elector", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ElectorBearerToken> tokens;

  public Elector() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public Elector(String electorId, ElectorName electorName, Sex sex, LocalDate dateOfBirth, String documentId, ElectorResidence electorResidence,
      ElectorCredentials electorCredentials, ElectorMetadata electorMetadata, List<ElectorBearerToken> tokens) {
    this.electorId = electorId;
    this.electorName = electorName;
    this.sex = sex;
    this.dateOfBirth = dateOfBirth;
    this.documentId = documentId;
    this.electorResidence = electorResidence;
    this.electorCredentials = electorCredentials;
    this.electorMetadata = electorMetadata;
    this.tokens = tokens;
  }

  public String getElectorId() {
    return electorId;
  }

  public void setElectorId(String electorId) {
    this.electorId = electorId;
  }

  public ElectorName getElectorName() {
    return electorName;
  }

  public void setElectorName(ElectorName electorName) {
    this.electorName = electorName;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public ElectorResidence getElectorResidence() {
    return electorResidence;
  }

  public void setElectorResidence(ElectorResidence electorResidence) {
    this.electorResidence = electorResidence;
  }

  public ElectorCredentials getElectorCredentials() {
    return electorCredentials;
  }

  public void setElectorCredentials(ElectorCredentials electorCredentials) {
    this.electorCredentials = electorCredentials;
  }

  public ElectorMetadata getElectorMetadata() {
    return electorMetadata;
  }

  public void setElectorMetadata(ElectorMetadata electorMetadata) {
    this.electorMetadata = electorMetadata;
  }

  public List<ElectorBearerToken> getTokens() {
    return tokens;
  }

  public void setTokens(List<ElectorBearerToken> tokens) {
    this.tokens = tokens;
  }

  public void addToken(ElectorBearerToken token) {
    tokens.add(token);
    token.setElector(this);
  }

  public void removeToken(ElectorBearerToken token) {
    tokens.remove(token);
    token.setElector(null);
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorId, documentId);
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
    Elector other = (Elector) object;
    return Objects.equals(electorId, other.electorId) && Objects.equals(documentId, other.documentId);
  }

  @Override
  public String toString() {
    return "Elector [electorId=" + electorId + ", electorName=" + electorName + ", sex=" + sex + ", dateOfBirth=" + dateOfBirth + ", documentId="
        + documentId + ", electorResidence=" + electorResidence + ", electorCredentials=" + electorCredentials + ", electorMetadata="
        + electorMetadata + ", tokens=" + tokens.size() + "]";
  }
}
