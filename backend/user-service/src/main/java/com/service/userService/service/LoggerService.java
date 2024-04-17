package com.service.userService.service;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

    public void logError(Throwable e, HttpStatus httpStatus) {
        LOGGER.error("Exception: {}, HTTP Status: {}", e.toString(), httpStatus.getReasonPhrase().toUpperCase(), e);
    }

    public <T> void logInfo(String message, HttpStatus status, T data) {
        LOGGER.info("{}, Status: {}, {}", message, status, data);
    }
}
