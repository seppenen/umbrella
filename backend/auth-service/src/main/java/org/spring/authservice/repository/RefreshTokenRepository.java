package org.spring.authservice.repository;

import org.spring.authservice.entity.TokenStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<TokenStateEntity, String> {

    List<? extends TokenStateEntity> findByEmailAndTokenNot(String email, String token);

    Optional<TokenStateEntity> findByToken(String token);
}
