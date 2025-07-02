package com.core.electionsystem.elector.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.core.electionsystem.elector.model.properties.ElectorBearerToken;

import jakarta.transaction.Transactional;

@Repository
public interface ElectorBearerTokenRepository extends JpaRepository<ElectorBearerToken, Long> {

  static final String SELECT_TOKENS_FROM_ELECTOR_BEARER_TOKEN_WHERE = "SELECT t FROM ElectorBearerToken t WHERE ";
  static final String TOKEN_OWNER_HAS_ID_AND = "t.elector.electorId = ?1 AND ";

  @Query(SELECT_TOKENS_FROM_ELECTOR_BEARER_TOKEN_WHERE + "t.electorBearerTokenValue = ?1")
  Optional<ElectorBearerToken> findByTokenItself(String tokenValue);

  @Query(SELECT_TOKENS_FROM_ELECTOR_BEARER_TOKEN_WHERE + TOKEN_OWNER_HAS_ID_AND + "(t.isExpired = FALSE AND t.isRevoked = FALSE)")
  List<ElectorBearerToken> findAllValidTokensByElector(String electorId);

  @Modifying
  @Transactional
  @Query("DELETE FROM ElectorBearerToken t WHERE " + TOKEN_OWNER_HAS_ID_AND + "(t.isExpired = TRUE OR t.isRevoked = TRUE)")
  void deleteAllInvalidTokensByElector(String electorId);
}
