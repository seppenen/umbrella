package org.spring.authservice.enums;

import lombok.Getter;

@Getter
public enum TokenEnum {
    ISSUER("auth"),
    ACCESS_TOKEN_TYPE("2"),
    REFRESH_TOKEN_TYPE("1"),
    REFRESH_TOKEN_EXPIRE_TIME(1000 * 60 * 240),
    ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 2),

    //TODO: Store secret key in a azure vault
    SECRET("357638792F423F4428472B4B6250655368566D597133743677397A2443264629");

    private final Object value;

    TokenEnum(Object value) {
        this.value = value;
    }

    public Integer getAsInteger() {
        if (this.value instanceof Integer) {
            return (Integer) value;
        }
        throw new IllegalArgumentException("Cannot cast enum value to Integer");
    }

    public String getAsString() {
        if (this.value instanceof String) {
            return (String) value;
        }
        throw new IllegalArgumentException("Cannot cast enum value to String");
    }

}
