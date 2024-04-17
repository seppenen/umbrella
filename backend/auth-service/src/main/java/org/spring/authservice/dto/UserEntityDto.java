package org.spring.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntityDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String address;
}
