package org.spring.authservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.authservice.service.LoggerService;
import org.springframework.stereotype.Service;

@Service
public class LoggerServiceImpl implements LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerServiceImpl.class);

    public LogErrorBuilder getErrorBuilder() {
        return new LogErrorBuilder();
    }

    public LogInfoBuilder getInfoBuilder() {
        return new LogInfoBuilder();
    }

    public static class LogErrorBuilder {

        private Throwable error;
        private String httpStatusCode;
        private String message;

        public LogErrorBuilder withError(Throwable error) {
            this.error = error;
            return this;
        }

        public LogErrorBuilder withStatusCode(String httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public LogErrorBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public void log() {
            if (httpStatusCode != null && message != null) {
                LOGGER.info("{}, Status: {}", message, httpStatusCode);
            } else {
                LOGGER.info("{}", message);
            }
        }
    }

    public static class LogInfoBuilder {
        private String message;
        private String httpStatusCode;
        private String data;

        public LogInfoBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogInfoBuilder withStatusCode(String httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public LogInfoBuilder withData(String data) {
            this.data = data;
            return this;
        }

        public void log() {
            if (httpStatusCode != null && data != null) {
                LOGGER.info("{}, Status: {}, {}", message, httpStatusCode, data);
            } else {
                LOGGER.info("{}", message);
            }
        }
    }
}
