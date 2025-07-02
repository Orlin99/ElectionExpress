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

@Entity(name = "ElectorMetadata")
@Table(name = "elector_metadata")
public class ElectorMetadata {

  @Id
  @SequenceGenerator(name = "elector_metadata_sequence", sequenceName = "elector_metadata_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elector_metadata_sequence")
  private Long electorMetadataId;

  @Column(name = "salt", columnDefinition = "TEXT")
  private String salt;

  @Column(name = "initialization_vector", columnDefinition = "TEXT")
  private String initializationVector;

  @JsonBackReference
  @OneToOne(mappedBy = "electorMetadata", cascade = CascadeType.ALL, orphanRemoval = true)
  private Elector elector;

  public ElectorMetadata() {
    // Default Empty Constructor
  }

  public ElectorMetadata(Long electorMetadataId, String salt, String initializationVector, Elector elector) {
    this.electorMetadataId = electorMetadataId;
    this.salt = salt;
    this.initializationVector = initializationVector;
    this.elector = elector;
  }

  public ElectorMetadata(String salt, String initializationVector, Elector elector) {
    this.salt = salt;
    this.initializationVector = initializationVector;
    this.elector = elector;
  }

  public Long getElectorMetadataId() {
    return electorMetadataId;
  }

  public void setElectorMetadataId(Long electorMetadataId) {
    this.electorMetadataId = electorMetadataId;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getInitializationVector() {
    return initializationVector;
  }

  public void setInitializationVector(String initializationVector) {
    this.initializationVector = initializationVector;
  }

  public Elector getElector() {
    return elector;
  }

  public void setElector(Elector elector) {
    this.elector = elector;
  }

  @Override
  public int hashCode() {
    return Objects.hash(electorMetadataId, salt, initializationVector, elector.getElectorId());
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
    ElectorMetadata other = (ElectorMetadata) object;
    return Objects.equals(electorMetadataId, other.electorMetadataId) && Objects.equals(salt, other.salt)
        && Objects.equals(initializationVector, other.initializationVector) && Objects.equals(elector.getElectorId(), other.elector.getElectorId());
  }

  @Override
  public String toString() {
    return "ElectorMetadata [electorMetadataId=" + electorMetadataId + ", salt=" + salt + ", initializationVector=" + initializationVector
        + ", elector=" + elector.getElectorId() + "]";
  }
}
