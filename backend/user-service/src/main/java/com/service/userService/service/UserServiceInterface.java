package com.service.userService.service;

import com.service.userService.entity.UserEntity;

import java.util.List;

public interface UserServiceInterface {

    UserEntity loginUser(UserEntity userEntity);

    UserEntity registerUser(UserEntity userEntity);

    UserEntity getUser(Long id);

    List<UserEntity> getAllUsers();
}
