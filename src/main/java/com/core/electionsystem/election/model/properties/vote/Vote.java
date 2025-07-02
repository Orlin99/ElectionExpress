package com.core.electionsystem.election.model.properties.vote;

import java.util.Objects;
import java.util.UUID;

import com.core.electionsystem.election.model.Election;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity(name = "Vote")
@Table(name = "vote")
public class Vote {

  @Id
  @GeneratedValue
  private UUID voteId;

  @Column(name = "voted_elector_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(10)")
  @Pattern(regexp = "^\\d{10}$", message = "Invalid Value For The EGN")
  private String votedElectorId;

  @Column(name = "voted_elector_document_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(9)")
  @Pattern(regexp = "^\\d{9}$", message = "Invalid Value For The Personal Card Number")
  private String votedElectorDocumentId;

  @JsonBackReference
  @ManyToOne(optional = false)
  @JoinColumn(name = "id", nullable = false)
  private Election election;

  public Vote() {
    // Default Empty Constructor
  }

  public Vote(UUID voteId, String votedElectorId, String votedElectorDocumentId, Election election) {
    this.voteId = voteId;
    this.votedElectorId = votedElectorId;
    this.votedElectorDocumentId = votedElectorDocumentId;
    this.election = election;
  }

  public Vote(String votedElectorId, String votedElectorDocumentId, Election election) {
    this.votedElectorId = votedElectorId;
    this.votedElectorDocumentId = votedElectorDocumentId;
    this.election = election;
  }

  public UUID getVoteId() {
    return voteId;
  }

  public void setVoteId(UUID voteId) {
    this.voteId = voteId;
  }

  public String getVotedElectorId() {
    return votedElectorId;
  }

  public void setVotedElectorId(String votedElectorId) {
    this.votedElectorId = votedElectorId;
  }

  public String getVotedElectorDocumentId() {
    return votedElectorDocumentId;
  }

  public void setVotedElectorDocumentId(String votedElectorDocumentId) {
    this.votedElectorDocumentId = votedElectorDocumentId;
  }

  public Election getElection() {
    return election;
  }

  public void setElection(Election election) {
    this.election = election;
  }

  @Override
  public int hashCode() {
    return Objects.hash(voteId, votedElectorId, votedElectorDocumentId, election.getId());
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
    Vote other = (Vote) object;
    return Objects.equals(voteId, other.voteId) && Objects.equals(votedElectorId, other.votedElectorId)
        && Objects.equals(votedElectorDocumentId, other.votedElectorDocumentId) && Objects.equals(election.getId(), other.election.getId());
  }

  @Override
  public String toString() {
    return "Vote [voteId=" + voteId + ", votedElectorId=" + votedElectorId + ", votedElectorDocumentId=" + votedElectorDocumentId + ", election="
        + election.getId() + "]";
  }
}
