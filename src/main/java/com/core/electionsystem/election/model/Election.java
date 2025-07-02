package com.core.electionsystem.election.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.election.model.properties.candidate.Candidate;
import com.core.electionsystem.election.model.properties.vote.Vote;
import com.core.electionsystem.election.utility.StatusAttributeConverter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "Election")
@Table(name = "election", uniqueConstraints = { @UniqueConstraint(name = "title_unique", columnNames = "title") })
public class Election {

  @Id
  @SequenceGenerator(name = "election_sequence", sequenceName = "election_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "election_sequence")
  private Long id;

  @Column(name = "title", nullable = false, updatable = false, columnDefinition = "TEXT")
  private String title;

  @Column(name = "description", nullable = false, updatable = false, columnDefinition = "TEXT")
  private String description;

  @Column(name = "start_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime startTime;

  @Column(name = "end_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime endTime;

  @Column(name = "status", nullable = false, columnDefinition = "TEXT")
  @Convert(converter = StatusAttributeConverter.class)
  private Status status;

  @JsonManagedReference
  @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Candidate> candidates;

  @JsonManagedReference
  @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vote> votes;

  public Election() {
    // Default Empty Constructor
  }

  @SuppressWarnings("java:S107")
  public Election(Long id, String title, String description, LocalDateTime startTime, LocalDateTime endTime, Status status,
      List<Candidate> candidates, List<Vote> votes) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.candidates = candidates;
    this.votes = votes;
  }

  public Election(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Status status, List<Candidate> candidates,
      List<Vote> votes) {
    this.title = title;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
    this.candidates = candidates;
    this.votes = votes;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public List<Candidate> getCandidates() {
    return candidates;
  }

  public void setCandidates(List<Candidate> candidates) {
    this.candidates = candidates;
  }

  public void addCandidate(Candidate candidate) {
    candidates.add(candidate);
    candidate.setElection(this);
  }

  public void removeCandidate(Candidate candidate) {
    candidates.remove(candidate);
    candidate.setElection(null);
  }

  public List<Vote> getVotes() {
    return votes;
  }

  public void setVotes(List<Vote> votes) {
    this.votes = votes;
  }

  public void addVote(Vote vote) {
    votes.add(vote);
    vote.setElection(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, startTime, endTime, status, candidates, votes);
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
    Election other = (Election) object;
    return Objects.equals(id, other.id) && Objects.equals(title, other.title) && Objects.equals(description, other.description)
        && Objects.equals(startTime, other.startTime) && Objects.equals(endTime, other.endTime) && (status == other.status)
        && Objects.equals(candidates, other.candidates) && Objects.equals(votes, other.votes);
  }

  @Override
  public String toString() {
    return "Election [id=" + id + ", title=" + title + ", description=" + description + ", startTime=" + startTime + ", endTime=" + endTime
        + ", status=" + status + ", candidates=" + candidates.size() + ", votes=" + votes.size() + "]";
  }
}
