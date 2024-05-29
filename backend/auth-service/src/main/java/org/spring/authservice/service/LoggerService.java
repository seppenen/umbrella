package org.spring.authservice.service;

import org.spring.authservice.service.impl.LoggerServiceImpl;

public interface LoggerService {
    LoggerServiceImpl.LogErrorBuilder getErrorBuilder();
    LoggerServiceImpl.LogInfoBuilder getInfoBuilder();
}
