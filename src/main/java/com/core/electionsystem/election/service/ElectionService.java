package com.core.electionsystem.election.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.core.electionsystem.election.dto.CandidateCreatorDTO;
import com.core.electionsystem.election.dto.ElectionEventCreatorDTO;
import com.core.electionsystem.election.dto.PreferenceCreatorDTO;
import com.core.electionsystem.election.dto.VoteCreatorDTO;
import com.core.electionsystem.election.exception.AlreadyVotedException;
import com.core.electionsystem.election.exception.NonExistentCandidateException;
import com.core.electionsystem.election.exception.NonExistentElectionException;
import com.core.electionsystem.election.exception.NonExistentPreferenceException;
import com.core.electionsystem.election.model.Election;
import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.election.model.properties.candidate.Candidate;
import com.core.electionsystem.election.model.properties.preference.Preference;
import com.core.electionsystem.election.model.properties.vote.Vote;
import com.core.electionsystem.election.repository.ElectionRepository;
import com.core.electionsystem.election.utility.ElectionUtility;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.service.JwtService;
import com.core.electionsystem.supervisor.utility.SupervisorUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class ElectionService {

  private final JwtService jwtService;
  private final ElectionRepository electionRepository;
  private final ElectorRepository electorRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public ElectionService(JwtService jwtService, ElectionRepository electionRepository, ElectorRepository electorRepository,
      ObjectMapper objectMapper) {
    this.jwtService = jwtService;
    this.electionRepository = electionRepository;
    this.electorRepository = electorRepository;
    this.objectMapper = objectMapper;
  }

  public void createElectionEvent(ElectionEventCreatorDTO electionEventCreatorDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    ElectionUtility.validateElectionEventInputData(electionEventCreatorDTO, electionRepository);
    final Election election = ElectionUtility.toEntity(electionEventCreatorDTO);
    electionRepository.save(election);
  }

  @Transactional
  public void addCandidate(CandidateCreatorDTO candidateCreatorDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final Long electionIdInput = candidateCreatorDTO.getElectionId();
    Election election = electionRepository.findById(electionIdInput)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + electionIdInput));
    final Status status = election.getStatus();
    ElectionUtility.validateInactiveStatus(status);
    ElectionUtility.validateCandidateInputData(candidateCreatorDTO, election);
    final Candidate candidate = ElectionUtility.toEntity(candidateCreatorDTO, election);
    election.getCandidates().add(candidate);
    electionRepository.save(election);
  }

  @Transactional
  public void addPreference(PreferenceCreatorDTO preferenceCreatorDTO) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    final Long electionIdInput = preferenceCreatorDTO.getElectionId();
    Election election = electionRepository.findById(electionIdInput)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + electionIdInput));
    final Status status = election.getStatus();
    ElectionUtility.validateInactiveStatus(status);
    Candidate candidate = ElectionUtility.getCandidateIfExists(preferenceCreatorDTO, election);
    ElectionUtility.validatePreferenceInputData(preferenceCreatorDTO, candidate);
    final Preference preference = ElectionUtility.toEntity(preferenceCreatorDTO, candidate);
    candidate.getPreferences().add(preference);
    electionRepository.save(election);
  }

  @Transactional
  public void vote(VoteCreatorDTO voteCreatorDTO) {
    ElectorUtility.authorizeAndValidateAccessForElectors(jwtService);
    ElectionUtility.validateElectorPersonalInputData(electorRepository, voteCreatorDTO);
    final Long electionIdInput = voteCreatorDTO.getElectionId();
    Election election = electionRepository.findById(electionIdInput)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + electionIdInput));
    final String votedElectorId = voteCreatorDTO.getVotedElectorId();
    final boolean isElectorAlreadyVoted = electionRepository.findVoteFromSpecificElector(votedElectorId, electionIdInput).isPresent();
    if (isElectorAlreadyVoted) {
      throw new AlreadyVotedException(ElectionUtility.MESSAGE_FOR_ALREADY_VOTED_RESPONSE_ERROR);
    }
    final Status status = election.getStatus();
    ElectionUtility.validateActiveStatus(status);
    final Integer candidateId = voteCreatorDTO.getCandidateId();
    final Integer preferenceId = voteCreatorDTO.getPreferenceId();
    ElectionUtility.validateVoteLegalityAndCastIt(election, candidateId, preferenceId);
    final Vote vote = ElectionUtility.toEntity(voteCreatorDTO, election);
    election.getVotes().add(vote);
    electionRepository.save(election);
  }

  public String getElectionEventById(Long id) throws JsonProcessingException {
    ElectionUtility.validateElectionEventId(id);
    final Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final ElectionEventCreatorDTO electionEventCreatorDTO = ElectionUtility.toDTO(election);
    return ElectionUtility.fetchElectionEventData(objectMapper, election, electionEventCreatorDTO);
  }

  public String getElectionEventByTitle(String title) throws JsonProcessingException {
    ElectionUtility.validateTitle(title);
    final Election election = electionRepository.findElectionByTitle(title)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_BY_TITLE_RESPONSE));
    final ElectionEventCreatorDTO electionEventCreatorDTO = ElectionUtility.toDTO(election);
    return ElectionUtility.fetchElectionEventData(objectMapper, election, electionEventCreatorDTO);
  }

  public String getCandidate(Integer candidateId, Long id) throws JsonProcessingException {
    ElectionUtility.validateElectionEventId(id);
    ElectionUtility.validateCandidateId(candidateId);
    final Candidate candidate = electionRepository.findCandidateById(candidateId, id)
        .orElseThrow(() -> new NonExistentCandidateException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateId));
    final CandidateCreatorDTO candidateCreatorDTO = ElectionUtility.toDTO(candidate);
    return ElectionUtility.fetchCandidateData(objectMapper, candidate, candidateCreatorDTO);
  }

  public String getPreference(Integer preferenceId, Integer candidateId, Long id) throws JsonProcessingException {
    ElectionUtility.validateElectionEventId(id);
    ElectionUtility.validateCandidateId(candidateId);
    ElectionUtility.validatePreferenceId(preferenceId);
    final Preference preference = electionRepository.findPreferenceById(preferenceId, candidateId, id)
        .orElseThrow(() -> new NonExistentPreferenceException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_PREFERENCE_RESPONSE + preferenceId));
    final PreferenceCreatorDTO preferenceCreatorDTO = ElectionUtility.toDTO(preference);
    return ElectionUtility.fetchPreferenceData(objectMapper, preference, preferenceCreatorDTO);
  }

  public String getVoterTurnoutForSpecificElection(Long id) {
    ElectionUtility.validateElectionEventId(id);
    final Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final long allVotesSoFar = election.getVotes().size();
    return ElectionUtility.MESSAGE_FOR_SUCCESSFULLY_RECEIVED_VOTER_TURNOUT + allVotesSoFar;
  }

  public String getPercentageResultsForSpecificElection(Long id) throws JsonProcessingException {
    ElectionUtility.validateElectionEventId(id);
    final Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final Status status = election.getStatus();
    ElectionUtility.validateCompletedStatus(status);
    return ElectionUtility.fetchElectionPercentageResults(objectMapper, election);
  }

  public List<String> listAllElectionsWithSpecificStatus(String status, int pageNumber, int pageSize) {
    final Status actualStatus = Status.fromString(status);
    final Pageable pageable = PageRequest.of(pageNumber, pageSize);
    final List<Election> elections = electionRepository.findAllElectionsWithTheSpecifiedStatusOrderById(actualStatus, pageable);
    return ElectionUtility.getFormattedDataOfElections(elections);
  }

  @SuppressWarnings("unused")
  public List<String> listAllCandidatesFromSpecificElection(Long id, int pageNumber, int pageSize) {
    ElectionUtility.validateElectionEventId(id);
    final Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final Pageable pageable = PageRequest.of(pageNumber, pageSize);
    final List<Candidate> candidates = electionRepository.findAllCandidatesFromSpecificElectionOrderById(id, pageable);
    return ElectionUtility.getFormattedDataOfCandidates(candidates);
  }

  //@formatter:off
  @SuppressWarnings("unused")
  public List<String> listAllPreferencesFromSpecificCandidateFromSpecificElection(Integer candidateId, Long id, int pageNumber, int pageSize) {
    ElectionUtility.validateElectionEventId(id);
    ElectionUtility.validateCandidateId(candidateId);
    final Candidate candidate = electionRepository.findCandidateById(candidateId, id)
        .orElseThrow(() -> new NonExistentCandidateException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateId));
    final Pageable pageable = PageRequest.of(pageNumber, pageSize);
    final List<Preference> preferences = electionRepository.findAllPreferencesFromSpecificCandidateFromSpecificElectionOrderById(candidateId, id, pageable);
    return ElectionUtility.getFormattedDataOfPreferences(preferences);
  }
  //@formatter:on

  @Transactional
  public void deleteElectionEvent(Long id) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final Status status = election.getStatus();
    ElectionUtility.validateInactiveStatus(status);
    electionRepository.deleteById(id);
  }

  @Transactional
  public void deleteCandidate(Integer candidateId, Long id) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final Status status = election.getStatus();
    ElectionUtility.validateInactiveStatus(status);
    ElectionUtility.removeCandidateById(election, candidateId);
    electionRepository.save(election);
  }

  @Transactional
  public void deletePreference(Integer preferenceId, Integer candidateId, Long id) {
    SupervisorUtility.authorizeAndValidateAccessForSupervisors(jwtService);
    Election election = electionRepository.findById(id)
        .orElseThrow(() -> new NonExistentElectionException(ElectionUtility.MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE + id));
    final Status status = election.getStatus();
    ElectionUtility.validateInactiveStatus(status);
    ElectionUtility.removePreferenceById(election, candidateId, preferenceId);
    electionRepository.save(election);
  }
}
