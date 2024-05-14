package org.spring.authservice.service;

import org.spring.authservice.service.impl.LoggerService;

public interface ILoggerService {
    LoggerService.LogErrorBuilder getErrorBuilder();
    LoggerService.LogInfoBuilder getInfoBuilder();
}
