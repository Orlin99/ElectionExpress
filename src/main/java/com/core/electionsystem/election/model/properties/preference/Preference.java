package com.core.electionsystem.election.model.properties.preference;

import java.util.Objects;

import com.core.electionsystem.election.model.properties.candidate.Candidate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity(name = "Preference")
@Table(name = "preference")
public class Preference {

  @EmbeddedId
  private PreferenceCompositeId preferenceCompositeId;

  @Column(name = "preference_id", nullable = false, updatable = false, columnDefinition = "SMALLINT")
  @NotNull(message = "Preference Id Cannot Be Null")
  @Min(value = 101, message = "Preference Id Must Be At Least 101")
  @Max(value = 180, message = "Preference Id Must Be At Most 180")
  private Integer preferenceId;

  @Column(name = "preference_name", nullable = false, updatable = false, columnDefinition = "TEXT")
  private String preferenceName;

  @Column(name = "preference_votes_count", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
  private Long preferenceVotesCount = 0L;

  @JsonBackReference
  @ManyToOne(optional = false)
  @MapsId("candidateCompositeId")
  @JoinColumn(name = "candidate_id_piece", nullable = false)
  @JoinColumn(name = "election_id_piece", nullable = false)
  private Candidate candidate;

  public Preference() {
    if (this.preferenceCompositeId == null) {
      this.preferenceCompositeId = new PreferenceCompositeId();
    }
  }

  public Preference(Integer preferenceId, String preferenceName, Candidate candidate) {
    this.preferenceCompositeId = new PreferenceCompositeId(preferenceId, candidate.getCandidateCompositeId());
    this.preferenceId = preferenceId;
    this.preferenceName = preferenceName;
    this.candidate = candidate;
  }

  public PreferenceCompositeId getPreferenceCompositeId() {
    return preferenceCompositeId;
  }

  public void setPreferenceCompositeId(PreferenceCompositeId preferenceCompositeId) {
    this.preferenceCompositeId = preferenceCompositeId;
    this.preferenceId = preferenceCompositeId.getPreferenceIdPiece();
  }

  public Integer getPreferenceId() {
    return preferenceId;
  }

  public void setPreferenceId(Integer preferenceId) {
    this.preferenceId = preferenceId;
    if (this.preferenceCompositeId != null) {
      this.preferenceCompositeId.setPreferenceIdPiece(preferenceId);
    }
  }

  public String getPreferenceName() {
    return preferenceName;
  }

  public void setPreferenceName(String preferenceName) {
    this.preferenceName = preferenceName;
  }

  public Long getPreferenceVotesCount() {
    return preferenceVotesCount;
  }

  private void setPreferenceVotesCount(Long preferenceVotesCount) {
    this.preferenceVotesCount = preferenceVotesCount;
  }

  public void incrementPreferenceVotesCount() {
    setPreferenceVotesCount(++this.preferenceVotesCount);
  }

  public Candidate getCandidate() {
    return candidate;
  }

  public void setCandidate(Candidate candidate) {
    this.candidate = candidate;
    if ((this.preferenceCompositeId != null) && (candidate != null)) {
      this.preferenceCompositeId.setCandidateCompositeId(candidate.getCandidateCompositeId());
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferenceCompositeId, preferenceId, preferenceName, preferenceVotesCount, candidate.getCandidateId(),
        candidate.getElection().getId());
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
    Preference other = (Preference) object;
    return Objects.equals(preferenceCompositeId, other.preferenceCompositeId) && Objects.equals(preferenceId, other.preferenceId)
        && Objects.equals(preferenceName, other.preferenceName) && Objects.equals(preferenceVotesCount, other.preferenceVotesCount)
        && Objects.equals(candidate.getCandidateId(), other.candidate.getCandidateId())
        && Objects.equals(candidate.getElection().getId(), other.candidate.getElection().getId());
  }

  @Override
  public String toString() {
    return "Preference [preferenceId=" + preferenceId + ", preferenceName=" + preferenceName + ", preferenceVotesCount=" + preferenceVotesCount
        + ", candidate=" + candidate.getCandidateId() + ", election=" + candidate.getElection().getId() + "]";
  }
}
