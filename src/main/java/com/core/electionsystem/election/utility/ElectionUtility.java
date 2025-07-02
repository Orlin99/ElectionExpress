package com.core.electionsystem.election.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.core.electionsystem.election.dto.CandidateCreatorDTO;
import com.core.electionsystem.election.dto.ElectionEventCreatorDTO;
import com.core.electionsystem.election.dto.PreferenceCreatorDTO;
import com.core.electionsystem.election.dto.VoteCreatorDTO;
import com.core.electionsystem.election.exception.InvalidCandidateIdException;
import com.core.electionsystem.election.exception.InvalidCandidateNameException;
import com.core.electionsystem.election.exception.InvalidDescriptionException;
import com.core.electionsystem.election.exception.InvalidElectionEventTimingException;
import com.core.electionsystem.election.exception.InvalidElectionIdException;
import com.core.electionsystem.election.exception.InvalidPreferenceIdException;
import com.core.electionsystem.election.exception.InvalidPreferenceNameException;
import com.core.electionsystem.election.exception.InvalidStatusException;
import com.core.electionsystem.election.exception.InvalidTitleException;
import com.core.electionsystem.election.exception.NonExistentCandidateException;
import com.core.electionsystem.election.exception.NonExistentPreferenceException;
import com.core.electionsystem.election.model.Election;
import com.core.electionsystem.election.model.properties.Status;
import com.core.electionsystem.election.model.properties.candidate.Candidate;
import com.core.electionsystem.election.model.properties.preference.Preference;
import com.core.electionsystem.election.model.properties.vote.Vote;
import com.core.electionsystem.election.repository.ElectionRepository;
import com.core.electionsystem.elector.exception.InvalidDocumentIdException;
import com.core.electionsystem.elector.exception.InvalidEgnException;
import com.core.electionsystem.elector.exception.NonExistentElectorException;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.repository.ElectorRepository;
import com.core.electionsystem.elector.utility.ElectorUtility;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ElectionUtility {

  private ElectionUtility() {
    // Default Empty Constructor
  }

  public static final String SUCCESSFULLY_CREATED_ELECTION_EVENT_RESPONSE = "The Election Event Was Successfully Created. Now You Must Add The Candidates With Their Preferences, If Any Exist";
  public static final String MESSAGE_FOR_NON_EXISTENT_ELECTION_RESPONSE = "Election Event With Such Id Does Not Exist: ";
  public static final String MESSAGE_FOR_NON_EXISTENT_ELECTION_BY_TITLE_RESPONSE = "Election Event With Such Title Does Not Exist";
  public static final String SUCCESSFULLY_ADDED_CANDIDATE_RESPONSE = "The Candidate Was Added Successfully. Now You Must Add The Preferences, If Any Exist";
  public static final String SUCCESSFULLY_ADDED_PREFERENCE_RESPONSE = "The Preference Was Added Successfully";
  public static final String MESSAGE_FOR_ALREADY_VOTED_RESPONSE_ERROR = "You Cannot Vote For This Election Because You Have Already Voted. Please Wait Until The End Of The Election Day To See The Full Results";
  public static final String SUCCESSFULLY_CAST_VOTE_RESPONSE = "You Have Successfully Voted In This Election. Please Wait Until The End Of The Election Day To See The Full Results";
  public static final String MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE = "Candidate With Such Id Does Not Exist: ";
  public static final String MESSAGE_FOR_NON_EXISTENT_PREFERENCE_RESPONSE = "Preference With Such Id Does Not Exist: ";
  public static final String MESSAGE_FOR_SUCCESSFULLY_RECEIVED_VOTER_TURNOUT = "The Current Voter Turnout For This Election Is: ";
  public static final String SUCCESSFULLY_DELETED_ELECTION_EVENT_RESPONSE = "The Election Event Was Successfully Deleted. All Candidates And All Preferences That Were In This Event Were Also Removed";
  public static final String SUCCESSFULLY_DELETED_CANDIDATE_RESPONSE = "The Candidate Was Removed Successfully. All Preferences That Were Registered For This Candidate Were Also Removed";
  public static final String SUCCESSFULLY_DELETED_PREFERENCE_RESPONSE = "The Preference Was Removed Successfully";

  private static final String ELECTION = "Election: ";
  private static final String WITH_TITLE = ", With Title: ";
  private static final String HAS = " Has ";
  private static final String CANDIDATES = " Candidates;";
  private static final String CANDIDATE = "Candidate: ";
  private static final String WITH_NAME = ", With Name: ";
  private static final String PREFERENCES = " Preferences;";
  private static final String PREFERENCE_WITH_ID = "Preference With Id: ";
  private static final String HAS_NAME = " Has Name: ";
  private static final String MESSAGE_FOR_INVALID_ELECTION_ID_RESPONSE = "Invalid Value For Election Id. Please Try Again";
  private static final String MESSAGE_FOR_INVALID_STATUS_RESPONSE = "Candidates And Preferences Cannot Be Added Or Deleted In Active Or Completed Elections";
  private static final String MESSAGE_FOR_FORBIDDEN_VOTING_RESPONSE = "You Are Unable To Vote Because The Elections Are Not Yet Started Or Are Already Completed";
  private static final String MESSAGE_FOR_FORBIDDEN_RESULTS_CHECKING_RESPONSE = "You Are Unable To See The Results For This Election Because The Elections Are Not Completed Yet";
  private static final int TITLE_MIN_LENGTH = 10;
  private static final int TITLE_MAX_LENGTH = 30;
  private static final String MESSAGE_FOR_INVALID_TITLE_RESPONSE = "Invalid Title For The Election. It Must Be At Least 10 Characters Long And At Most 30 Characters Long";
  private static final String MESSAGE_FOR_ALREADY_EXISTING_TITLE_RESPONSE = "Invalid Title For The Election. Election Event With This Title Already Exist";
  private static final int DESCRIPTION_MIN_LENGTH = 30;
  private static final int DESCRIPTION_MAX_LENGTH = 120;
  private static final String MESSAGE_FOR_INVALID_DESCRIPTION_RESPONSE = "Invalid Description For The Election. It Must Be At Least 30 Characters Long And At Most 120 Characters Long";
  private static final String MESSAGE_FOR_REQUIRED_ELECTION_EVENT_TIMING_PARAMETERS_RESPONSE = "The Starting Time And The Ending Time Must Be Specified";
  private static final String MESSAGE_FOR_INVALID_STARTING_TIME_FOR_ELECTION_EVENT_RESPONSE = "You Cannot Create Election Event With Retroactively";
  private static final String MESSAGE_FOR_INVALID_ELECTION_EVENT_TIMING_RESPONSE = "Invalid Timing For The Election Event. The Starting Time Must Be Before The Ending Time";
  private static final int CANDIDATE_ID_MIN_VALUE = 1;
  private static final int CANDIDATE_ID_MAX_VALUE = 99;
  private static final String MESSAGE_FOR_INVALID_CANDIDATE_ID_RESPONSE = "Invalid Value For Candidate Id. It Should Be From 1 To 99";
  private static final String MESSAGE_FOR_REPEATED_CANDIDATE_ID_RESPONSE = "Candidate With This Id Already Exist";
  private static final String MESSAGE_FOR_INVALID_CANDIDATE_NAME_RESPONSE = "Invalid Value For Candidate Name";
  private static final String MESSAGE_FOR_REPEATED_CANDIDATE_NAME_RESPONSE = "Candidate With This Name Already Exist";
  private static final int PREFERENCE_ID_MIN_VALUE = 101;
  private static final int PREFERENCE_ID_MAX_VALUE = 180;
  private static final String MESSAGE_FOR_INVALID_PREFERENCE_ID_RESPONSE = "Invalid Value For Preference Id. It Should Be From 101 To 180";
  private static final String MESSAGE_FOR_REPEATED_PREFERENCE_ID_RESPONSE = "Preference With This Id Already Exist";
  private static final String MESSAGE_FOR_INVALID_PREFERENCE_NAME_RESPONSE = "Invalid Value For Preference Name";
  private static final String MESSAGE_FOR_REPEATED_PREFERENCE_NAME_RESPONSE = "Preference With This Name Already Exist";
  private static final int MAX_POSSIBLE_PERCENTAGE = 100;
  private static final int DIGITS_AFTER_THE_FLOATING_POINT = 2;

  public static ElectionEventCreatorDTO toDTO(Election election) {
    ElectionEventCreatorDTO electionEventCreatorDTO = new ElectionEventCreatorDTO();
    electionEventCreatorDTO.setTitle(election.getTitle());
    electionEventCreatorDTO.setDescription(election.getDescription());
    electionEventCreatorDTO.setStartTime(election.getStartTime());
    electionEventCreatorDTO.setEndTime(election.getEndTime());
    return electionEventCreatorDTO;
  }

  public static Election toEntity(ElectionEventCreatorDTO electionEventCreatorDTO) {
    Election election = new Election();
    election.setTitle(electionEventCreatorDTO.getTitle());
    election.setDescription(electionEventCreatorDTO.getDescription());
    election.setStartTime(electionEventCreatorDTO.getStartTime());
    election.setEndTime(electionEventCreatorDTO.getEndTime());
    election.setStatus(Status.INACTIVE);
    election.setCandidates(new ArrayList<>());
    election.setVotes(new ArrayList<>());
    return election;
  }

  public static CandidateCreatorDTO toDTO(Candidate candidate) {
    CandidateCreatorDTO candidateCreatorDTO = new CandidateCreatorDTO();
    candidateCreatorDTO.setCandidateId(candidate.getCandidateId());
    candidateCreatorDTO.setCandidateName(candidate.getCandidateName());
    candidateCreatorDTO.setElectionId(candidate.getElection().getId());
    return candidateCreatorDTO;
  }

  public static Candidate toEntity(CandidateCreatorDTO candidateCreatorDTO, Election election) {
    Candidate candidate = new Candidate();
    candidate.setCandidateId(candidateCreatorDTO.getCandidateId());
    candidate.setCandidateName(candidateCreatorDTO.getCandidateName());
    candidate.setPreferences(new ArrayList<>());
    candidate.setElection(election);
    return candidate;
  }

  public static PreferenceCreatorDTO toDTO(Preference preference) {
    PreferenceCreatorDTO preferenceCreatorDTO = new PreferenceCreatorDTO();
    preferenceCreatorDTO.setPreferenceId(preference.getPreferenceId());
    preferenceCreatorDTO.setPreferenceName(preference.getPreferenceName());
    final Candidate candidate = preference.getCandidate();
    preferenceCreatorDTO.setElectionId(candidate.getElection().getId());
    preferenceCreatorDTO.setCandidateId(candidate.getCandidateId());
    return preferenceCreatorDTO;
  }

  public static Preference toEntity(PreferenceCreatorDTO preferenceCreatorDTO, Candidate candidate) {
    Preference preference = new Preference();
    preference.setPreferenceId(preferenceCreatorDTO.getPreferenceId());
    preference.setPreferenceName(preferenceCreatorDTO.getPreferenceName());
    preference.setCandidate(candidate);
    return preference;
  }

  public static VoteCreatorDTO toDTO(Vote vote) {
    VoteCreatorDTO voteCreatorDTO = new VoteCreatorDTO();
    voteCreatorDTO.setVotedElectorId(vote.getVotedElectorId());
    voteCreatorDTO.setVotedElectorDocumentId(vote.getVotedElectorDocumentId());
    voteCreatorDTO.setElectionId(vote.getElection().getId());
    voteCreatorDTO.setCandidateId(null);
    voteCreatorDTO.setPreferenceId(null);
    return voteCreatorDTO;
  }

  public static Vote toEntity(VoteCreatorDTO voteCreatorDTO, Election election) {
    Vote vote = new Vote();
    vote.setVotedElectorId(voteCreatorDTO.getVotedElectorId());
    vote.setVotedElectorDocumentId(voteCreatorDTO.getVotedElectorDocumentId());
    final Long electionId = election.getId();
    final Long electionIdInput = voteCreatorDTO.getElectionId();
    if (!electionId.equals(electionIdInput)) {
      throw new InvalidElectionIdException(MESSAGE_FOR_INVALID_ELECTION_ID_RESPONSE);
    }
    vote.setElection(election);
    return vote;
  }

  public static void validateInactiveStatus(Status status) {
    if (status != Status.INACTIVE) {
      throw new InvalidStatusException(MESSAGE_FOR_INVALID_STATUS_RESPONSE);
    }
  }

  public static void validateActiveStatus(Status status) {
    if (status != Status.ACTIVE) {
      throw new InvalidStatusException(MESSAGE_FOR_FORBIDDEN_VOTING_RESPONSE);
    }
  }

  public static void validateCompletedStatus(Status status) {
    if (status != Status.COMPLETED) {
      throw new InvalidStatusException(MESSAGE_FOR_FORBIDDEN_RESULTS_CHECKING_RESPONSE);
    }
  }

  public static void validateElectionEventInputData(ElectionEventCreatorDTO electionEventCreatorDTO, ElectionRepository electionRepository) {
    final String title = electionEventCreatorDTO.getTitle();
    validateTitle(title);
    validateTitleIsUnique(electionRepository, title);
    final String description = electionEventCreatorDTO.getDescription();
    validateDescription(description);
    validateElectionEventTiming(electionEventCreatorDTO);
  }

  public static void validateCandidateInputData(CandidateCreatorDTO candidateCreatorDTO, Election election) {
    final Integer candidateIdInput = candidateCreatorDTO.getCandidateId();
    validateCandidateId(candidateIdInput);
    validateCandidateIdIsUnique(election, candidateIdInput);
    final String candidateNameInput = candidateCreatorDTO.getCandidateName();
    validateCandidateNameIsValidAndUnique(election, candidateNameInput);
  }

  public static Candidate getCandidateIfExists(PreferenceCreatorDTO preferenceCreatorDTO, Election election) {
    final Integer candidateIdInput = preferenceCreatorDTO.getCandidateId();
    validateCandidateId(candidateIdInput);
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      Integer candidateId = candidate.getCandidateId();
      if (candidateId.equals(candidateIdInput)) {
        return candidate;
      }
    }
    throw new NonExistentCandidateException(MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateIdInput);
  }

  public static void validatePreferenceInputData(PreferenceCreatorDTO preferenceCreatorDTO, Candidate candidate) {
    final Integer preferenceIdInput = preferenceCreatorDTO.getPreferenceId();
    validatePreferenceId(preferenceIdInput);
    validatePreferenceIdIsUnique(candidate, preferenceIdInput);
    final String preferenceNameInput = preferenceCreatorDTO.getPreferenceName();
    validatePreferenceNameIsValidAndUnique(candidate, preferenceNameInput);
  }

  public static void validateElectorPersonalInputData(ElectorRepository electorRepository, VoteCreatorDTO voteCreatorDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String currentUserEmail = authentication.getName();
    final Elector elector = electorRepository.findElectorByEmail(currentUserEmail).orElseThrow(
        () -> new NonExistentElectorException(ElectionSystemUtility.MESSAGE_FOR_NON_EXISTENT_USER_BY_EMAIL_EXCEPTION + currentUserEmail));
    validateElectorPersonalInputData(elector, voteCreatorDTO);
  }

  public static void validateVoteLegalityAndCastIt(Election election, Integer candidateId, Integer preferenceId) {
    validateCandidateId(candidateId);
    validatePreferenceIdOnVoting(preferenceId);
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      Integer currentCandidateId = candidate.getCandidateId();
      if (currentCandidateId.equals(candidateId)) {
        candidate.incrementVotesCount();
        final List<Preference> preferences = candidate.getPreferences();
        if (preferences.isEmpty() || (preferenceId == null)) {
          return;
        }
        for (Preference preference : preferences) {
          Integer currentPreferenceId = preference.getPreferenceId();
          if (currentPreferenceId.equals(preferenceId)) {
            preference.incrementPreferenceVotesCount();
            return;
          }
        }
        throw new NonExistentPreferenceException(MESSAGE_FOR_NON_EXISTENT_PREFERENCE_RESPONSE + preferenceId);
      }
    }
    throw new NonExistentCandidateException(MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateId);
  }

  // @formatter:off
  public static String fetchElectionEventData(ObjectMapper objectMapper, Election election, ElectionEventCreatorDTO electionEventCreatorDTO) throws JsonProcessingException {
    ObjectNode electionItems = objectMapper.createObjectNode();
    putElectionItems(election, electionEventCreatorDTO, electionItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(electionItems);
  }

  public static String fetchCandidateData(ObjectMapper objectMapper, Candidate candidate, CandidateCreatorDTO candidateCreatorDTO) throws JsonProcessingException {
    ObjectNode candidateItems = objectMapper.createObjectNode();
    putCandidateItems(candidate, candidateCreatorDTO, candidateItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(candidateItems);
  }

  public static String fetchPreferenceData(ObjectMapper objectMapper, Preference preference, PreferenceCreatorDTO preferenceCreatorDTO) throws JsonProcessingException {
    ObjectNode preferenceItems = objectMapper.createObjectNode();
    putPreferenceItems(preference, preferenceCreatorDTO, preferenceItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(preferenceItems);
  }
  // @formatter:on

  public static String fetchElectionPercentageResults(ObjectMapper objectMapper, Election election) throws JsonProcessingException {
    ObjectNode electionResultsItems = objectMapper.createObjectNode();
    putElectionPercentageResults(election, electionResultsItems);
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(electionResultsItems);
  }

  //@formatter:off
  public static List<String> getFormattedDataOfElections(List<Election> elections) {
    List<String> formattedDataOfElections = new ArrayList<>();
    for (Election currentElectionData : elections) {
      formattedDataOfElections.add(ELECTION + currentElectionData.getId() + WITH_TITLE + currentElectionData.getTitle() + HAS + currentElectionData.getCandidates().size() + CANDIDATES);
    }
    return formattedDataOfElections;
  }

  public static List<String> getFormattedDataOfCandidates(List<Candidate> candidates) {
    List<String> formattedDataOfCandidates = new ArrayList<>();
    for (Candidate currentCandidateData : candidates) {
      formattedDataOfCandidates.add(CANDIDATE + currentCandidateData.getCandidateId() + WITH_NAME + currentCandidateData.getCandidateName() + HAS + currentCandidateData.getPreferences().size() + PREFERENCES);
    }
    return formattedDataOfCandidates;
  }

  public static List<String> getFormattedDataOfPreferences(List<Preference> preferences) {
    List<String> formattedDataOfPreferences = new ArrayList<>();
    for (Preference currentPreferenceData : preferences) {
      formattedDataOfPreferences.add(PREFERENCE_WITH_ID + currentPreferenceData.getPreferenceId() + HAS_NAME + currentPreferenceData.getPreferenceName() + ";");
    }
    return formattedDataOfPreferences;
  }
  //@formatter:on

  public static void removeCandidateById(Election election, Integer candidateId) {
    validateCandidateId(candidateId);
    List<Candidate> candidates = election.getCandidates();
    boolean isCandidatePresent = candidates.stream().anyMatch(candidate -> candidate.getCandidateId().equals(candidateId));
    if (!isCandidatePresent) {
      throw new NonExistentCandidateException(MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateId);
    }
    candidates.removeIf(candidate -> candidate.getCandidateId().equals(candidateId));
  }

  public static void removePreferenceById(Election election, Integer candidateId, Integer preferenceId) {
    validateCandidateId(candidateId);
    validatePreferenceId(preferenceId);
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      Integer currentCandidateId = candidate.getCandidateId();
      if (currentCandidateId.equals(candidateId)) {
        List<Preference> preferences = candidate.getPreferences();
        boolean isPreferencePresent = preferences.stream().anyMatch(preference -> preference.getPreferenceId().equals(preferenceId));
        if (!isPreferencePresent) {
          throw new NonExistentPreferenceException(MESSAGE_FOR_NON_EXISTENT_PREFERENCE_RESPONSE + preferenceId);
        }
        preferences.removeIf(preference -> preference.getPreferenceId().equals(preferenceId));
        return;
      }
    }
    throw new NonExistentCandidateException(MESSAGE_FOR_NON_EXISTENT_CANDIDATE_RESPONSE + candidateId);
  }

  public static void validateElectionEventId(Long inputId) {
    if ((inputId == null) || (inputId <= 0)) {
      throw new InvalidElectionIdException(MESSAGE_FOR_INVALID_ELECTION_ID_RESPONSE);
    }
  }

  public static void validateTitle(String title) {
    if ((title == null) || (title.isBlank()) || (title.length() < TITLE_MIN_LENGTH) || (title.length() > TITLE_MAX_LENGTH)) {
      throw new InvalidTitleException(MESSAGE_FOR_INVALID_TITLE_RESPONSE);
    }
  }

  public static void validateCandidateId(Integer candidateIdInput) {
    if ((candidateIdInput == null) || (candidateIdInput < CANDIDATE_ID_MIN_VALUE) || (candidateIdInput > CANDIDATE_ID_MAX_VALUE)) {
      throw new InvalidCandidateIdException(MESSAGE_FOR_INVALID_CANDIDATE_ID_RESPONSE);
    }
  }

  public static void validatePreferenceId(Integer preferenceIdInput) {
    if ((preferenceIdInput == null) || (preferenceIdInput < PREFERENCE_ID_MIN_VALUE) || (preferenceIdInput > PREFERENCE_ID_MAX_VALUE)) {
      throw new InvalidPreferenceIdException(MESSAGE_FOR_INVALID_PREFERENCE_ID_RESPONSE);
    }
  }

  private static void validateTitleIsUnique(ElectionRepository electionRepository, String title) {
    final boolean electionIsPresentByTitle = electionRepository.findElectionByTitle(title).isPresent();
    if (electionIsPresentByTitle) {
      throw new InvalidTitleException(MESSAGE_FOR_ALREADY_EXISTING_TITLE_RESPONSE);
    }
  }

  private static void validateDescription(String description) {
    if ((description == null) || (description.isBlank()) || (description.length() < DESCRIPTION_MIN_LENGTH)
        || (description.length() > DESCRIPTION_MAX_LENGTH)) {
      throw new InvalidDescriptionException(MESSAGE_FOR_INVALID_DESCRIPTION_RESPONSE);
    }
  }

  private static void validateElectionEventTiming(ElectionEventCreatorDTO electionEventCreatorDTO) {
    final LocalDateTime startTime = electionEventCreatorDTO.getStartTime();
    final LocalDateTime endTime = electionEventCreatorDTO.getEndTime();
    if ((startTime == null) || (endTime == null)) {
      throw new InvalidElectionEventTimingException(MESSAGE_FOR_REQUIRED_ELECTION_EVENT_TIMING_PARAMETERS_RESPONSE);
    }
    if (startTime.isBefore(LocalDateTime.now())) {
      throw new InvalidElectionEventTimingException(MESSAGE_FOR_INVALID_STARTING_TIME_FOR_ELECTION_EVENT_RESPONSE);
    }
    if (endTime.isBefore(startTime)) {
      throw new InvalidElectionEventTimingException(MESSAGE_FOR_INVALID_ELECTION_EVENT_TIMING_RESPONSE);
    }
  }

  private static void validateCandidateIdIsUnique(Election election, Integer candidateIdInput) {
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      Integer candidateId = candidate.getCandidateId();
      if (candidateId.equals(candidateIdInput)) {
        throw new InvalidCandidateIdException(MESSAGE_FOR_REPEATED_CANDIDATE_ID_RESPONSE);
      }
    }
  }

  private static void validateCandidateNameIsValidAndUnique(Election election, String candidateNameInput) {
    if ((candidateNameInput == null) || candidateNameInput.isBlank()) {
      throw new InvalidCandidateNameException(MESSAGE_FOR_INVALID_CANDIDATE_NAME_RESPONSE);
    }
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      String candidateName = candidate.getCandidateName();
      if (candidateName.equals(candidateNameInput)) {
        throw new InvalidCandidateNameException(MESSAGE_FOR_REPEATED_CANDIDATE_NAME_RESPONSE);
      }
    }
  }

  private static void validatePreferenceIdIsUnique(Candidate candidate, Integer preferenceIdInput) {
    final List<Preference> preferences = candidate.getPreferences();
    for (Preference preference : preferences) {
      Integer preferenceId = preference.getPreferenceId();
      if (preferenceId.equals(preferenceIdInput)) {
        throw new InvalidPreferenceIdException(MESSAGE_FOR_REPEATED_PREFERENCE_ID_RESPONSE);
      }
    }
  }

  private static void validatePreferenceNameIsValidAndUnique(Candidate candidate, String preferenceNameInput) {
    if ((preferenceNameInput == null) || preferenceNameInput.isBlank()) {
      throw new InvalidPreferenceNameException(MESSAGE_FOR_INVALID_PREFERENCE_NAME_RESPONSE);
    }
    final List<Preference> preferences = candidate.getPreferences();
    for (Preference preference : preferences) {
      String preferenceName = preference.getPreferenceName();
      if (preferenceName.equals(preferenceNameInput)) {
        throw new InvalidPreferenceNameException(MESSAGE_FOR_REPEATED_PREFERENCE_NAME_RESPONSE);
      }
    }
  }

  private static void validateElectorPersonalInputData(Elector elector, VoteCreatorDTO voteCreatorDTO) {
    final String electorId = elector.getElectorId();
    final String electorIdInput = voteCreatorDTO.getVotedElectorId();
    ElectorUtility.validateEGN(electorIdInput);
    if (!electorId.equals(electorIdInput)) {
      throw new InvalidEgnException(ElectorUtility.MESSAGE_FOR_INVALID_EGN_EXCEPTION);
    }
    final String documentId = elector.getDocumentId();
    final String documentIdInput = voteCreatorDTO.getVotedElectorDocumentId();
    ElectorUtility.validateDocumentId(documentIdInput);
    if (!documentId.equals(documentIdInput)) {
      throw new InvalidDocumentIdException(ElectorUtility.MESSAGE_FOR_INVALID_DOCUMENT_ID_EXCEPTION);
    }
  }

  private static void validatePreferenceIdOnVoting(Integer preferenceIdInput) {
    if (preferenceIdInput == null) {
      return;
    }
    if ((preferenceIdInput < PREFERENCE_ID_MIN_VALUE) || (preferenceIdInput > PREFERENCE_ID_MAX_VALUE)) {
      throw new InvalidPreferenceIdException(MESSAGE_FOR_INVALID_PREFERENCE_ID_RESPONSE);
    }
  }

  private static void putElectionItems(Election election, ElectionEventCreatorDTO electionEventCreatorDTO, ObjectNode electionItems) {
    electionItems.put("id", election.getId());
    electionItems.put("title", electionEventCreatorDTO.getTitle());
    electionItems.put("description", electionEventCreatorDTO.getDescription());
    electionItems.put("startTime", electionEventCreatorDTO.getStartTime().toString());
    electionItems.put("endTime", electionEventCreatorDTO.getEndTime().toString());
    electionItems.put("status", election.getStatus().getValue());
    electionItems.put("candidates", election.getCandidates().size());
    electionItems.put("votes", election.getVotes().size());
  }

  private static void putCandidateItems(Candidate candidate, CandidateCreatorDTO candidateCreatorDTO, ObjectNode candidateItems) {
    candidateItems.put("candidateId", candidateCreatorDTO.getCandidateId());
    candidateItems.put("candidateName", candidateCreatorDTO.getCandidateName());
    candidateItems.put("preferences", candidate.getPreferences().size());
    candidateItems.put("electionId", candidateCreatorDTO.getElectionId());
    final Status status = candidate.getElection().getStatus();
    if ((status == Status.INACTIVE) || (status == Status.COMPLETED)) {
      candidateItems.put("votesCount", candidate.getVotesCount());
    }
  }

  private static void putPreferenceItems(Preference preference, PreferenceCreatorDTO preferenceCreatorDTO, ObjectNode preferenceItems) {
    preferenceItems.put("preferenceId", preferenceCreatorDTO.getPreferenceId());
    preferenceItems.put("preferenceName", preferenceCreatorDTO.getPreferenceName());
    preferenceItems.put("candidateId", preferenceCreatorDTO.getCandidateId());
    preferenceItems.put("electionId", preferenceCreatorDTO.getElectionId());
    final Status status = preference.getCandidate().getElection().getStatus();
    if ((status == Status.INACTIVE) || (status == Status.COMPLETED)) {
      preferenceItems.put("preferenceVotesCount", preference.getPreferenceVotesCount());
    }
  }

  //@formatter:off
  private static void putElectionPercentageResults(Election election, ObjectNode electionResultsItems) {
    final long allVotes = election.getVotes().size();
    final List<Candidate> candidates = election.getCandidates();
    for (Candidate candidate : candidates) {
      String currentCandidateName = candidate.getCandidateName();
      long votesForCurrentCandidate = candidate.getVotesCount();
      double percentageForCurrentCandidate = BigDecimal
          .valueOf(votesForCurrentCandidate)
          .multiply(BigDecimal.valueOf(MAX_POSSIBLE_PERCENTAGE))
          .divide(BigDecimal.valueOf(allVotes), DIGITS_AFTER_THE_FLOATING_POINT, RoundingMode.HALF_UP)
          .doubleValue();
      electionResultsItems.put(currentCandidateName, percentageForCurrentCandidate);
    }
  }
  //@formatter:on
}
