package com.core.electionsystem.election.model.properties.candidate;

import java.util.List;
import java.util.Objects;

import com.core.electionsystem.election.model.Election;
import com.core.electionsystem.election.model.properties.preference.Preference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "Candidate")
@Table(name = "candidate")
public class Candidate {

  @EmbeddedId
  private CandidateCompositeId candidateCompositeId;

  @Column(name = "candidate_id", nullable = false, updatable = false, columnDefinition = "SMALLINT")
  @NotNull(message = "Candidate Id Cannot Be Null")
  @Min(value = 1, message = "Candidate Id Must Be At Least 1")
  @Max(value = 99, message = "Candidate Id Must Be At Most 99")
  private Integer candidateId;

  @Column(name = "candidate_name", nullable = false, updatable = false, columnDefinition = "TEXT")
  private String candidateName;

  @Column(name = "votes_count", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
  private Long votesCount = 0L;

  @JsonManagedReference
  @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Preference> preferences;

  @JsonBackReference
  @MapsId("electionIdPiece")
  @ManyToOne(optional = false)
  @JoinColumn(name = "id", nullable = false)
  private Election election;

  public Candidate() {
    if (this.candidateCompositeId == null) {
      this.candidateCompositeId = new CandidateCompositeId();
    }
  }

  public Candidate(Integer candidateId, String candidateName, List<Preference> preferences, Election election) {
    this.candidateCompositeId = new CandidateCompositeId(candidateId, election.getId());
    this.candidateId = candidateId;
    this.candidateName = candidateName;
    this.preferences = preferences;
    this.election = election;
  }

  public CandidateCompositeId getCandidateCompositeId() {
    return candidateCompositeId;
  }

  public void setCandidateCompositeId(CandidateCompositeId candidateCompositeId) {
    this.candidateCompositeId = candidateCompositeId;
    this.candidateId = candidateCompositeId.getCandidateIdPiece();
  }

  public Integer getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(Integer candidateId) {
    this.candidateId = candidateId;
    if (this.candidateCompositeId != null) {
      this.candidateCompositeId.setCandidateIdPiece(candidateId);
    }
  }

  public String getCandidateName() {
    return candidateName;
  }

  public void setCandidateName(String candidateName) {
    this.candidateName = candidateName;
  }

  public Long getVotesCount() {
    return votesCount;
  }

  private void setVotesCount(Long votesCount) {
    this.votesCount = votesCount;
  }

  public void incrementVotesCount() {
    setVotesCount(++this.votesCount);
  }

  public List<Preference> getPreferences() {
    return preferences;
  }

  public void setPreferences(List<Preference> preferences) {
    this.preferences = preferences;
  }

  public void addPreference(Preference preference) {
    preferences.add(preference);
    preference.setCandidate(this);
  }

  public void removePreference(Preference preference) {
    preferences.remove(preference);
    preference.setCandidate(null);
  }

  public Election getElection() {
    return election;
  }

  public void setElection(Election election) {
    this.election = election;
    if ((this.candidateCompositeId != null) && (election != null)) {
      this.candidateCompositeId.setElectionIdPiece(election.getId());
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(candidateCompositeId, candidateId, candidateName, votesCount, preferences, election.getId());
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
    Candidate other = (Candidate) object;
    return Objects.equals(candidateCompositeId, other.candidateCompositeId) && Objects.equals(candidateId, other.candidateId)
        && Objects.equals(candidateName, other.candidateName) && Objects.equals(votesCount, other.votesCount)
        && Objects.equals(preferences, other.preferences) && Objects.equals(election.getId(), other.election.getId());
  }

  @Override
  public String toString() {
    return "Candidate [candidateId=" + candidateId + ", candidateName=" + candidateName + ", votesCount=" + votesCount + ", preferences="
        + preferences.size() + ", election=" + election.getId() + "]";
  }
}
