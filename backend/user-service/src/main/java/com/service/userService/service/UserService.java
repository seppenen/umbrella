package com.service.userService.service;

import com.service.userService.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity save(UserEntity userEntity);

    UserEntity getUser(Long id);

    List<UserEntity> getAllUsers();

}
