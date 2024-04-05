package org.spring.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.spring.authservice.entity.ApiErrorResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

@Service
public class ApiResponseErrorFactory {


    public Mono<Void> createErrorResponse(ServerHttpResponse response, String message) {
        ApiErrorResponse errorResponse = createApiErrorResponse(response, message);
        StringBuilder json = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json.append(objectMapper.writeValueAsString(errorResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while mapping ApiErrorResponse to JSON", e);
        }
        String errorResponseJson = json.toString();
        return writeResponse(response, errorResponseJson);
    }

    private ApiErrorResponse createApiErrorResponse(ServerHttpResponse response, String message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus(Objects.requireNonNull(response.getStatusCode()).toString());
        errorResponse.setError(response.getStatusCode().isError());
        errorResponse.setErrorCode(response.getStatusCode().value());
        errorResponse.setMessage(message);
        errorResponse.setTimeStamp(new Date());
        return errorResponse;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, String responseBody) {
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }
}

