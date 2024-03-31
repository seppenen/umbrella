package org.spring.authservice.ExceptionHandlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * DelegatedAuthenticationEntryPoint class is an implementation of the AuthenticationEntryPoint interface.
 * It provides a way to handle authentication failures in a delegated manner by using a HandlerExceptionResolver to resolve the exception and return an appropriate response.
 */
@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;


    public DelegatedAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver
    ) {
        this.resolver = resolver;
    }

    /**
     * This method is called when an authentication failure occurs. It delegates the handling of the exception to a HandlerExceptionResolver
     * to resolve the exception and return an appropriate response. It takes in the HttpServletRequest, HttpServletResponse, and
     * AuthenticationException as parameters to handle the authentication failure.
     *
     * @param request The HttpServletRequest object representing the request made by the user.
     * @param response The HttpServletResponse object representing the response to be sent back to the user.
     * @param authException The AuthenticationException object representing the exception that occurred during authentication.
     * @throws IOException if an I/O error occurs while handling the authentication failure.
     * @throws ServletException if a servlet-specific error occurs while handling the authentication failure.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        resolver.resolveException(request, response, null, authException);
    }
}
