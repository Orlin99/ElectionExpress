package com.core.electionsystem.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;
import com.core.electionsystem.elector.model.Elector;
import com.core.electionsystem.elector.model.properties.ElectorBearerToken;
import com.core.electionsystem.elector.repository.ElectorBearerTokenRepository;
import com.core.electionsystem.exception.InvalidSecurityAlgorithmException;
import com.core.electionsystem.exception.UnknownAuthorityException;
import com.core.electionsystem.supervisor.model.Supervisor;
import com.core.electionsystem.supervisor.model.properties.SupervisorBearerToken;
import com.core.electionsystem.supervisor.repository.SupervisorBearerTokenRepository;
import com.core.electionsystem.utility.ElectionSystemUtility;
import com.core.electionsystem.utility.Role;
import com.core.electionsystem.utility.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final String secretKey;

  public JwtService() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(SecurityUtility.HMAC_SHA256_ALGORITHM);
      SecretKey generatedKey = keyGenerator.generateKey();
      secretKey = Base64.getEncoder().encodeToString(generatedKey.getEncoded());
    } catch (NoSuchAlgorithmException exception) {
      throw new InvalidSecurityAlgorithmException(SecurityUtility.MESSAGE_FOR_INVALID_SECURITY_ALGORITHM_EXCEPTION);
    }
  }

  // @formatter:off
  public String generateToken(String email, Role role) {
    Map<String, Object> additionalClaims = new HashMap<>();
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_ROLE_ATTRIBUTE, role.getValue());
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_2FA_VERIFIED_ATTRIBUTE, false);
    return Jwts
        .builder()
        .claims()
        .subject(email)
        .add(additionalClaims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + SecurityUtility.JSON_WEB_TOKEN_EXPIRATION_TIME))
        .and()
        .header()
        .type(SecurityUtility.HEADER_TYPE_JSON_WEB_TOKEN)
        .and()
        .signWith(getSecretKey())
        .compact();
  }

  public String generate2faToken(String email, Role role) {
    Map<String, Object> additionalClaims = new HashMap<>();
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_ROLE_ATTRIBUTE, role.getValue());
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_2FA_VERIFIED_ATTRIBUTE, true);
    return Jwts
        .builder()
        .claims()
        .subject(email)
        .add(additionalClaims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + SecurityUtility.JSON_WEB_TOKEN_EXPIRATION_TIME))
        .and()
        .header()
        .type(SecurityUtility.HEADER_TYPE_JSON_WEB_TOKEN)
        .and()
        .signWith(getSecretKey())
        .compact();
  }

  public String generateRecoveryToken(String email, Role role) {
    Map<String, Object> additionalClaims = new HashMap<>();
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_ROLE_ATTRIBUTE, role.getValue());
    additionalClaims.put(SecurityUtility.JSON_WEB_TOKEN_PURPOSE_ATTRIBUTE, SecurityUtility.JSON_WEB_TOKEN_RECOVERY_PURPOSE);
    return Jwts
        .builder()
        .claims()
        .subject(email)
        .add(additionalClaims)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + SecurityUtility.RECOVERY_JSON_WEB_TOKEN_EXPIRATION_TIME))
        .and()
        .header()
        .type(SecurityUtility.HEADER_TYPE_JSON_WEB_TOKEN)
        .and()
        .signWith(getSecretKey())
        .compact();
  }

  public SupervisorBearerToken buildSupervisorToken(Supervisor supervisor, String supervisorBearerTokenValue) {
    return new SupervisorBearerToken.Builder()
        .supervisorBearerTokenValue(supervisorBearerTokenValue)
        .tokenType(TokenType.BEARER)
        .isExpired(false)
        .isRevoked(false)
        .supervisor(supervisor)
        .build();
  }

  public ElectorBearerToken buildElectorToken(Elector elector, String electorBearerTokenValue) {
    return new ElectorBearerToken.Builder()
        .electorBearerTokenValue(electorBearerTokenValue)
        .tokenType(TokenType.BEARER)
        .isExpired(false)
        .isRevoked(false)
        .elector(elector)
        .build();
  }

  public boolean isTokenValidFromDatabase(SupervisorBearerTokenRepository supervisorTokenRepository, ElectorBearerTokenRepository electorTokenRepository, String jsonWebToken) {
    Boolean isSupervisorTokenValid = supervisorTokenRepository.findByTokenItself(jsonWebToken)
        .map(token -> !token.isExpired() && !token.isRevoked())
        .orElse(false);
    Boolean isElectorTokenValid = electorTokenRepository.findByTokenItself(jsonWebToken)
        .map(token -> !token.isExpired() && !token.isRevoked())
        .orElse(false);
    return isSupervisorTokenValid || isElectorTokenValid;
  }
  // @formatter:on

  public void revokeAllSupervisorTokens(SupervisorBearerTokenRepository supervisorTokenRepository, Supervisor supervisor) {
    final Long supervisorId = supervisor.getId();
    List<SupervisorBearerToken> validSupervisorTokens = supervisorTokenRepository.findAllValidTokensBySupervisor(supervisorId);
    if (validSupervisorTokens.isEmpty()) {
      return;
    }
    for (SupervisorBearerToken validToken : validSupervisorTokens) {
      validToken.setExpired(true);
      validToken.setRevoked(true);
    }
    supervisorTokenRepository.saveAll(validSupervisorTokens);
  }

  public void revokeAllElectorTokens(ElectorBearerTokenRepository electorTokenRepository, Elector elector) {
    final String electorId = elector.getElectorId();
    List<ElectorBearerToken> validElectorTokens = electorTokenRepository.findAllValidTokensByElector(electorId);
    if (validElectorTokens.isEmpty()) {
      return;
    }
    for (ElectorBearerToken validToken : validElectorTokens) {
      validToken.setExpired(true);
      validToken.setRevoked(true);
    }
    electorTokenRepository.saveAll(validElectorTokens);
  }

  // @formatter:off
  public Optional<Role> getRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(this::mapRoleToEnum)
        .findFirst();
  }
  // @formatter:on

  public boolean isTokenValid(String jsonWebToken, UserDetails userDetails) {
    final String username = extractUsername(jsonWebToken);
    return (username.equals(userDetails.getUsername()) && (!isTokenExpired(jsonWebToken)));
  }

  public String extractUsername(String jsonWebToken) {
    return extractClaim(jsonWebToken, Claims::getSubject);
  }

  public String extractRole(String jsonWebToken) {
    return extractClaim(jsonWebToken, claims -> claims.get(SecurityUtility.JSON_WEB_TOKEN_ROLE_ATTRIBUTE, String.class));
  }

  public Boolean extract2faVerifiedClaim(String jsonWebToken) {
    return extractClaim(jsonWebToken, claims -> claims.get(SecurityUtility.JSON_WEB_TOKEN_2FA_VERIFIED_ATTRIBUTE, Boolean.class));
  }

  private <T> T extractClaim(String jsonWebToken, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(jsonWebToken);
    return claimsResolver.apply(claims);
  }

  // @formatter:off
  private Claims extractAllClaims(String jsonWebToken) {
    return Jwts
        .parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(jsonWebToken)
        .getPayload();
  }
  // @formatter:on

  private SecretKey getSecretKey() {
    byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(secretKeyBytes);
  }

  private boolean isTokenExpired(String jsonWebToken) {
    return extractExpiration(jsonWebToken).before(new Date());
  }

  private Date extractExpiration(String jsonWebToken) {
    return extractClaim(jsonWebToken, Claims::getExpiration);
  }

  private Role mapRoleToEnum(String authority) {
    switch (authority) {
      case ElectionSystemUtility.SUPERVISOR_ROLE:
        return Role.SUPERVISOR;
      case ElectionSystemUtility.ELECTOR_ROLE:
        return Role.ELECTOR;
      default:
        throw new UnknownAuthorityException(ElectionSystemUtility.MESSAGE_FOR_UNKNOWN_AUTHORITY_EXCEPTION + authority);
    }
  }
}
