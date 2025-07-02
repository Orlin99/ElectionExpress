package com.core.electionsystem.elector.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.Sex;

@Repository
public interface ElectorRepository extends JpaRepository<Elector, String> {

  static final String SELECT_ALL_FROM_ELECTOR_WHERE = "SELECT e FROM Elector e WHERE ";
  static final String SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE = "SELECT e.electorId, e.electorCredentials.email, e.electorCredentials.phoneNumber FROM Elector e WHERE ";
  static final String ORDER_BY_EGN_ASC = " ORDER BY e.electorId ASC";

  @Query(SELECT_ALL_FROM_ELECTOR_WHERE + "e.electorId = ?1")
  Optional<Elector> findElectorById(String electorId);

  @Query(SELECT_ALL_FROM_ELECTOR_WHERE + "e.documentId = ?1")
  Optional<Elector> findElectorByDocumentId(String documentId);

  @Query(SELECT_ALL_FROM_ELECTOR_WHERE + "e.electorCredentials.email = ?1")
  Optional<Elector> findElectorByEmail(String email);

  @Query(SELECT_ALL_FROM_ELECTOR_WHERE + "e.electorCredentials.phoneNumber = ?1")
  Optional<Elector> findElectorByPhoneNumber(String phoneNumber);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorName.surname = ?1" + ORDER_BY_EGN_ASC)
  List<String> findBySurname(String surname, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorName.middleName = ?1 AND e.electorName.surname = ?2" + ORDER_BY_EGN_ASC)
  List<String> findByMiddleNameAndSurname(String middleName, String surname, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorName.firstName = ?1" + ORDER_BY_EGN_ASC)
  List<String> findByFirstName(String firstName, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorName.firstName = ?1 AND e.electorName.middleName = ?2" + ORDER_BY_EGN_ASC)
  List<String> findByFirstNameAndMiddleName(String firstName, String middleName, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorName.firstName = ?1 AND e.electorName.surname = ?2" + ORDER_BY_EGN_ASC)
  List<String> findByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE
      + "e.electorName.firstName = ?1 AND e.electorName.middleName = ?2 AND e.electorName.surname = ?3" + ORDER_BY_EGN_ASC)
  List<String> findByFullName(String firstName, String middleName, String surname, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorResidence.region = ?1" + ORDER_BY_EGN_ASC)
  List<String> findByRegion(String region, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorResidence.region = ?1 AND e.electorResidence.locality = ?2"
      + ORDER_BY_EGN_ASC)
  List<String> findByRegionAndLocality(String region, String locality, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.electorResidence.region = ?1 AND e.electorResidence.municipality = ?2"
      + ORDER_BY_EGN_ASC)
  List<String> findByRegionAndMunicipality(String region, String municipality, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE
      + "e.electorResidence.region = ?1 AND e.electorResidence.municipality = ?2 AND e.electorResidence.locality = ?3" + ORDER_BY_EGN_ASC)
  List<String> findByMainResidenceProperties(String region, String municipality, String locality, Pageable pageable);

  @Query(SELECT_ID_EMAIL_AND_PHONE_NUMBER_FROM_ELECTOR_WHERE + "e.dateOfBirth = ?1" + ORDER_BY_EGN_ASC)
  List<String> findByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);

  @Query("SELECT COUNT(e) FROM Elector e WHERE e.sex = ?1")
  Integer getElectorsCountBySex(Sex sex);
}
