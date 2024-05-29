package com.service.userService.service;

import org.springframework.http.HttpStatus;

public interface LoggerService {
    void logError(Throwable e, HttpStatus httpStatus);

    <T> void logInfo(String message, HttpStatus status, T data);
}
