package org.spring.authservice.repository;

import org.spring.authservice.entity.TokenStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<TokenStateEntity, Long> {

    List<? extends TokenStateEntity> findByEmailAndIdNot(String email, Long id);
}
