package org.spring.authservice.repository;

import org.spring.authservice.entity.AccessTokenData;
import org.springframework.data.repository.CrudRepository;


public interface AccessTokenRepository extends CrudRepository<AccessTokenData, Long> {


}
