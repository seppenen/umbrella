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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.spring.authservice.utility.TokenUtility.ACCESS_TOKEN;


@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class AuthController extends BaseController {
    private static final String AUTH_SUCCESS_STATUS = "auth";

    private final JwtService jwtService;
    private final AuthFacade authFacade;

    /**
     * Performs authorization and returns a Flux emitting a Map of response data if access token is valid.
     *
     * @return a Flux emitting a Map of response data
     */
    @PostMapping("/authorize")
    public Mono<Map<String, Object>> authorize() {
        Map<String, Object> response = Collections.singletonMap(AUTH_SUCCESS_STATUS, true);
        return sendMono(response);
    }


    @PostMapping("/authenticate")
    //TODO: implement redis cache for access token
    public Mono<Map<String, Boolean>> authenticate(@RequestBody UserCredentialDto userRequestDto, ServerWebExchange exchange) {
        Map<String, Boolean> authSuccessResponse = Collections.singletonMap(AUTH_SUCCESS_STATUS, true);
        return authFacade.processTokensIfAuthenticatedUser(userRequestDto)
                .flatMap(tokens -> buildResponseWithCookie(tokens, exchange))
                .then(sendMono(authSuccessResponse));
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


