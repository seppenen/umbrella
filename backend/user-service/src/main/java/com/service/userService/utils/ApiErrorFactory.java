package com.service.userService.utils;

import com.service.userService.entity.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApiErrorFactory {
    /**
     * Creates an instance of ApiErrorResponse with the specified parameters.
     *
     * @param status  The HttpStatus of the error response.
     * @param message The error message.
     * @param detail  The detailed description of the error.
     * @return An instance of ApiErrorResponse.
     */
    public ApiErrorResponse create(HttpStatus status, String message, String detail) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus(status);
        errorResponse.setErrorCode(status.value());
        errorResponse.setMessage(message);
        errorResponse.setDetail(detail);
        errorResponse.setTimeStamp(LocalDateTime.now());
        return errorResponse;
    }
}

