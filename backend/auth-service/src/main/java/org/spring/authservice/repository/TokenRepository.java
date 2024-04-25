package org.spring.authservice.repository;

import org.spring.authservice.entity.TokenState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenState, Long> {
}
