package org.spring.authservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.authservice.service.ILoggerService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class LoggerService implements ILoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

    public LogErrorBuilder getErrorBuilder() {
        return new LogErrorBuilder();
    }

    public LogInfoBuilder getInfoBuilder() {
        return new LogInfoBuilder();
    }

    public static class LogErrorBuilder {

        private Throwable error;
        private HttpStatusCode httpStatusCode;

        public LogErrorBuilder withError(Throwable error) {
            this.error = error;
            return this;
        }

        public LogErrorBuilder withStatusCode(HttpStatusCode httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public void log() {
            LOGGER.error("Exception: {}, HTTP Status: {}", error.toString(), httpStatusCode.value(), error);
        }
    }

    public static class LogInfoBuilder {
        private String message;
        private HttpStatusCode httpStatusCode;
        private String data;

        public LogInfoBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public LogInfoBuilder withStatusCode(HttpStatusCode httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public LogInfoBuilder withData(String data) {
            this.data = data;
            return this;
        }

        public void log() {
            if (httpStatusCode != null && data != null) {
                LOGGER.info("{}, Status: {}, {}", message, httpStatusCode.value(), data);
            } else {
                LOGGER.info("{}", message);
            }
        }
    }
}
