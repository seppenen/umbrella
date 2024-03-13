package org.umbrella.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApiErrorResponse {
    private HttpStatus status;
    private int errorCode;
    private String message;
    private String detail;
    private LocalDateTime timeStamp;

}
