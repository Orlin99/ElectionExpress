package com.core.electionsystem.supervisor.model.properties;

import java.util.Objects;

import com.core.electionsystem.supervisor.model.Supervisor;
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

@Entity(name = "SupervisorMetadata")
@Table(name = "supervisor_metadata")
public class SupervisorMetadata {

  @Id
  @SequenceGenerator(name = "supervisor_metadata_sequence", sequenceName = "supervisor_metadata_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisor_metadata_sequence")
  private Long supervisorMetadataId;

  @Column(name = "salt", columnDefinition = "TEXT")
  private String salt;

  @Column(name = "initialization_vector", columnDefinition = "TEXT")
  private String initializationVector;

  @JsonBackReference
  @OneToOne(mappedBy = "supervisorMetadata", cascade = CascadeType.ALL, orphanRemoval = true)
  private Supervisor supervisor;

  public SupervisorMetadata() {
    // Default Empty Constructor
  }

  public SupervisorMetadata(Long supervisorMetadataId, String salt, String initializationVector, Supervisor supervisor) {
    this.supervisorMetadataId = supervisorMetadataId;
    this.salt = salt;
    this.initializationVector = initializationVector;
    this.supervisor = supervisor;
  }

  public SupervisorMetadata(String salt, String initializationVector, Supervisor supervisor) {
    this.salt = salt;
    this.initializationVector = initializationVector;
    this.supervisor = supervisor;
  }

  public Long getSupervisorMetadataId() {
    return supervisorMetadataId;
  }

  public void setSupervisorMetadataId(Long supervisorMetadataId) {
    this.supervisorMetadataId = supervisorMetadataId;
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

  public Supervisor getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(Supervisor supervisor) {
    this.supervisor = supervisor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(supervisorMetadataId, salt, initializationVector, supervisor.getId());
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
    SupervisorMetadata other = (SupervisorMetadata) object;
    return Objects.equals(supervisorMetadataId, other.supervisorMetadataId) && Objects.equals(salt, other.salt)
        && Objects.equals(initializationVector, other.initializationVector) && Objects.equals(supervisor.getId(), other.supervisor.getId());
  }

  @Override
  public String toString() {
    return "SupervisorMetadata [supervisorMetadataId=" + supervisorMetadataId + ", salt=" + salt + ", initializationVector=" + initializationVector
        + ", supervisor=" + supervisor.getId() + "]";
  }
}
