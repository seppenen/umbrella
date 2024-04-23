package com.service.userService.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum HttpErrorEnums {

        RESPONSE_INVALID_JSON_EXCEPTION("Invalid JSON format"),
        RESPONSE_USER_REGISTRATION_FAILED_EXCEPTION("Failed to register user"),
        RESPONSE_USER_LOGIN_FAILED_EXCEPTION("Login or password is incorrect"),
        RESPONSE_ENTITY_NOT_FOUND_ERROR("Entity not found");

        private final String msg;

}

