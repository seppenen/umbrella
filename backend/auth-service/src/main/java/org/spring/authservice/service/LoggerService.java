package org.spring.authservice.service;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

    public void logError(Throwable e, HttpStatusCode httpStatusCode) {
        LOGGER.error("{}, HTTP Status: {}, stacktrace:{} {}", e, httpStatusCode.value(), System.lineSeparator(), e.getStackTrace());
    }

    public <T> void logInfo(String message, HttpStatusCode httpStatusCode, T data) {
        LOGGER.info("{}, Status: {}, {}", message, httpStatusCode.value(), data);
    }
}
