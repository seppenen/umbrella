package org.spring.authservice.enums;

import lombok.Getter;

@Getter
public enum TokenEnum {

    SECRET("357638792F423F4428472B4B6250655368566D597133743677397A2443264629");

    private final String value;

    TokenEnum(String value) {
        this.value = value;
    }

}
