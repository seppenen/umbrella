package org.umbrella.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.umbrella.entity.ApiErrorResponse;

import java.io.IOException;
import java.util.Date;

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
        errorResponse.setTimeStamp(new Date());
        return errorResponse;
    }

    public void writeResponseError(HttpServletResponse response,
                                    AuthenticationException authException) throws IOException {
        ApiErrorResponse apiErrorResponse = create(
                HttpStatus.UNAUTHORIZED,
                authException.getMessage(),
                null);
        String jsonErrorResponse = new ObjectMapper().writeValueAsString(apiErrorResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonErrorResponse);
    }
}

