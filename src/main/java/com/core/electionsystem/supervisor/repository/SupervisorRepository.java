package com.core.electionsystem.supervisor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.core.electionsystem.supervisor.model.Supervisor;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

  static final String SELECT_ALL_FROM_SUPERVISOR_WHERE = "SELECT s FROM Supervisor s WHERE ";
  static final String SELECT_ID_FULL_NAME_EMAIL_AND_PHONE_NUMBER_FROM_SUPERVISOR = "SELECT s.id, s.firstName, s.surname, s.supervisorEmail, s.supervisorPhone FROM Supervisor s";
  static final String ORDER_BY_ID_ASC = " ORDER BY s.id ASC";

  @Query(SELECT_ALL_FROM_SUPERVISOR_WHERE + "s.supervisorEmail = ?1")
  Optional<Supervisor> findSupervisorByEmail(String supervisorEmail);

  @Query(SELECT_ALL_FROM_SUPERVISOR_WHERE + "s.supervisorPhone = ?1")
  Optional<Supervisor> findSupervisorByPhoneNumber(String supervisorPhone);

  @Query(SELECT_ID_FULL_NAME_EMAIL_AND_PHONE_NUMBER_FROM_SUPERVISOR + ORDER_BY_ID_ASC)
  List<String> findAllSupervisorsById(Pageable pageable);
}
