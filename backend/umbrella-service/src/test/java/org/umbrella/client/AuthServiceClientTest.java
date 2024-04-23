package org.umbrella.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.umbrella.service.JwtService;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthServiceClientTest {

    private static final String ACCESS_TOKEN = "access_token";

    @MockBean
    private WebTestClient webTestClient;

    @MockBean
    private AuthServiceClient authServiceClient;
    @MockBean
    private MockWebServer mockWebServer;

    @MockBean
    private WebClient webClient;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        this.authServiceClient = new AuthServiceClient(jwtService, webClient);
    }

    void setupMockServerResponse(int responseCode, String responseBody) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(responseCode)
                .setBody(responseBody);
        mockWebServer.enqueue(mockResponse);
    }

    @Test
    void validateToken_whenTokenIsValid_returnTrue() {
        Map<String, String> requestTokenData = this.authServiceClient.requestToken().block();

        setupMockServerResponse(200, "{ \"valid\": true }");

        assert requestTokenData != null;
        var result = this.authServiceClient.validateToken(requestTokenData.get(ACCESS_TOKEN));
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateToken_whenTokenIsInvalid_returnFalse() {
        setupMockServerResponse(401, "{ \"valid\": false }");
        var result = this.authServiceClient.validateToken("invalid_token");
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
