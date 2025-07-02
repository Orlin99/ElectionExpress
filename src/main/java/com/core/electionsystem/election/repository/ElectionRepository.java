package com.core.electionsystem.election.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core.electionsystem.election.model.Election;
import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.election.model.properties.candidate.Candidate;
import com.core.electionsystem.election.model.properties.preference.Preference;
import com.core.electionsystem.election.model.properties.vote.Vote;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

  static final String SELECT_ALL_FROM_ELECTION_WHERE = "SELECT e FROM Election e WHERE e.";
  static final String SELECT_ALL_FROM_CANDIDATE_WHERE = "SELECT c FROM Candidate c WHERE c.";
  static final String SELECT_ALL_FROM_PREFERENCE_WHERE = "SELECT p FROM Preference p WHERE p.";

  @Query(SELECT_ALL_FROM_ELECTION_WHERE + "title = ?1")
  Optional<Election> findElectionByTitle(String title);

  @Query("SELECT v FROM Vote v WHERE v.votedElectorId = :votedElectorId AND v.election.id = :electionId")
  Optional<Vote> findVoteFromSpecificElector(@Param("votedElectorId") String votedElectorId, @Param("electionId") Long electionId);

  @Query(SELECT_ALL_FROM_ELECTION_WHERE + "status = ?1")
  List<Election> findAllElectionsWithTheSpecifiedStatus(@Param("status") Status status);

  @Query(SELECT_ALL_FROM_CANDIDATE_WHERE + "candidateId = :candidateId AND c.election.id = :electionId")
  Optional<Candidate> findCandidateById(@Param("candidateId") Integer candidateId, @Param("electionId") Long electionId);

  //@formatter:off
  @Query(SELECT_ALL_FROM_PREFERENCE_WHERE+"preferenceId = :preferenceId AND p.candidate.candidateId = :candidateId AND p.candidate.election.id = :electionId")
  Optional<Preference> findPreferenceById(@Param("preferenceId") Integer preferenceId, @Param("candidateId") Integer candidateId, @Param("electionId") Long electionId);
  //@formatter:on

  @Query(SELECT_ALL_FROM_ELECTION_WHERE + "status = ?1 ORDER BY e.id ASC")
  List<Election> findAllElectionsWithTheSpecifiedStatusOrderById(@Param("status") Status status, Pageable pageable);

  @Query(SELECT_ALL_FROM_CANDIDATE_WHERE + "election.id = :electionId ORDER BY c.candidateId ASC")
  List<Candidate> findAllCandidatesFromSpecificElectionOrderById(@Param("electionId") Long electionId, Pageable pageable);

  //@formatter:off
  @Query(SELECT_ALL_FROM_PREFERENCE_WHERE + "candidate.candidateId = :candidateId AND p.candidate.election.id = :electionId ORDER BY p.preferenceId ASC")
  List<Preference> findAllPreferencesFromSpecificCandidateFromSpecificElectionOrderById(@Param("candidateId") Integer candidateId, @Param("electionId") Long electionId, Pageable pageable);
  //@formatter:on
}
