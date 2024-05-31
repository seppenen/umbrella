package org.spring.authservice.repository;

import org.spring.authservice.entity.AccessTokenData;
import org.springframework.data.keyvalue.repository.KeyValueRepository;


public interface AccessTokenRepository extends KeyValueRepository<AccessTokenData, Long> {


}
