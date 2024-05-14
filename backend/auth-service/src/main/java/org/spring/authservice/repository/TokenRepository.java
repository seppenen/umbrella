package org.spring.authservice.repository;

import org.spring.authservice.entity.TokenState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<TokenState, Long> {

    List<? extends TokenState> findByEmailAndIdNot(String email, Long id);
}
