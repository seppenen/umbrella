package com.service.userService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "Users")
public class UserEntity extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String address;

}
