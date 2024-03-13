package org.umbrella.umbrella.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
