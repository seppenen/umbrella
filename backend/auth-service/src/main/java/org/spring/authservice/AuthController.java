package org.spring.authservice;

import lombok.RequiredArgsConstructor;
import org.spring.authservice.dto.UserCredentialDto;
import org.spring.authservice.enums.TokenEnum;
import org.spring.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;
import static org.spring.authservice.utility.TokenUtility.TOKEN_VALID;


@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class AuthController extends BaseController {
    private static final String STATUS = "status";

    private final JwtService jwtService;

    private final AuthFacade authFacade;


    @PostMapping("/authorize")
    public Mono<Map<String, Object>> validateToken() {
        return Mono.just(Map.of(STATUS, "success", TOKEN_VALID, true));
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<Void>> authenticate(@RequestBody UserCredentialDto userRequestDto) {
        return authFacade.obtainTokensIfAuthenticated(userRequestDto)
                .flatMap(this::buildResponseWithHeaders);
    }

    @PostMapping("/access-token")
    public Mono<ResponseEntity<Void>> getAccessToken() {

        //TODO: to implement
        return jwtService.generateToken(TokenEnum.ACCESS_TOKEN_EXPIRE_TIME.getAsInteger(), TokenEnum.ACCESS_TOKEN_TYPE.getAsString(), null)
                .map(accessToken -> ResponseEntity.ok()
                        .header(ACCESS_TOKEN, accessToken)
                        .build());
    }


}


