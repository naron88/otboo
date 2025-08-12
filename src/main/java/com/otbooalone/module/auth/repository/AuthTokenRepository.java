package com.otbooalone.module.auth.repository;

import com.otbooalone.module.auth.entity.AuthToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {

  Optional<AuthToken> findByUserId(UUID userId);

  Optional<AuthToken> findByUserIdAndRefreshToken(UUID userId, String refreshToken);

  Optional<AuthToken> findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);

  void deleteByUserId(UUID userId);
}
