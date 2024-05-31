package org.spring.authservice;

import org.spring.authservice.entity.TokenResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.REFRESH_TOKEN;

public abstract class BaseController {

    protected Mono<ResponseEntity<Void>> buildResponseWithHeaders(TokenResponseEntity tokenResponseEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(REFRESH_TOKEN, tokenResponseEntity.refreshToken());
        headers.add(ACCESS_TOKEN, tokenResponseEntity.accessToken());
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }

}
