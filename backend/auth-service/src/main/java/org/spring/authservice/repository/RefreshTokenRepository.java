package org.spring.authservice.repository;

import org.spring.authservice.entity.AuthenticationTokenData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<AuthenticationTokenData, String> {

    List<? extends AuthenticationTokenData> findByEmailAndTokenNot(String email, String token);

    Optional<AuthenticationTokenData> findByToken(String token);
}
