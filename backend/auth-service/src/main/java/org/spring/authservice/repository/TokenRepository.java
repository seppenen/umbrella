package org.spring.authservice.repository;

import org.spring.authservice.entity.TokenStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenStateEntity, Long> {

    List<? extends TokenStateEntity> findByEmailAndIdNot(String email, Long id);

    Optional<TokenStateEntity> findByToken(String token);
}
