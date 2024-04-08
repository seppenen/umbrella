package org.umbrella.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthServiceClientTest {



    @Autowired
    private WebTestClient webTestClient;

    private AuthServiceClient authServiceClient;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(mockWebServer.url("/").toString());
        this.authServiceClient = new AuthServiceClient(webClientBuilder);
    }

    @Test
    void validateToken_whenTokenIsValid_returnTrue() {
        //Request new token before running this test
        Map<String, String> response = webTestClient.get().uri("http://localhost/auth/api/v1/token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .returnResult().getResponseBody();

        assert response != null;
        String token =  response.get("access_token");

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody("{ \"valid\": true }"); // replace with expected server response
        mockWebServer.enqueue(mockResponse);

        var result = this.authServiceClient.validateToken(token);
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateToken_whenTokenIsInvalid_returnFalse() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(401)
                .setBody("{ \"valid\": false }");
        mockWebServer.enqueue(mockResponse);

        var result = this.authServiceClient.validateToken("invalid_token");
        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
