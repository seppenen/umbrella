package org.umbrella.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.umbrella.apigateway.entity.ApiErrorResponse;
import reactor.core.publisher.Mono;

import java.util.Date;

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
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return writeResponse(response, errorResponseJson);
    }

    private ApiErrorResponse createApiErrorResponse(ServerHttpResponse response, String message) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus(response.getStatusCode().toString());
        errorResponse.setDetail("Error while processing the request");
        errorResponse.setErrorCode(response.getStatusCode().value());
        errorResponse.setMessage(message);
        errorResponse.setTimeStamp(new Date());
        return errorResponse;
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, String responseBody) {
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }
}

