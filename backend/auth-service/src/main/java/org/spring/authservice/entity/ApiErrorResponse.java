package org.spring.authservice.entity;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter

public class ApiErrorResponse {
    private String status;
    private String message;
    private int errorCode;
    private String detail;
    private Date timeStamp;
}
