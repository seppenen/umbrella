package org.spring.authservice.repository;

import org.spring.authservice.entity.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;


public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, Long> {


}
