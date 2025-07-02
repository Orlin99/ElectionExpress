package com.core.electionsystem.supervisor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;

import jakarta.transaction.Transactional;

@Repository
public interface SupervisorBearerTokenRepository extends JpaRepository<SupervisorBearerToken, Long> {

  static final String SELECT_TOKENS_FROM_SUPERVISOR_BEARER_TOKEN_WHERE = "SELECT t FROM SupervisorBearerToken t WHERE ";
  static final String TOKEN_OWNER_HAS_ID_AND = "t.supervisor.id = ?1 AND ";

  @Query(SELECT_TOKENS_FROM_SUPERVISOR_BEARER_TOKEN_WHERE + "t.supervisorBearerTokenValue = ?1")
  Optional<SupervisorBearerToken> findByTokenItself(String tokenValue);

  @Query(SELECT_TOKENS_FROM_SUPERVISOR_BEARER_TOKEN_WHERE + TOKEN_OWNER_HAS_ID_AND + "(t.isExpired = FALSE AND t.isRevoked = FALSE)")
  List<SupervisorBearerToken> findAllValidTokensBySupervisor(Long supervisorId);

  @Modifying
  @Transactional
  @Query("DELETE FROM SupervisorBearerToken t WHERE " + TOKEN_OWNER_HAS_ID_AND + "(t.isExpired = TRUE OR t.isRevoked = TRUE)")
  void deleteAllInvalidTokensBySupervisor(Long supervisorId);
}
