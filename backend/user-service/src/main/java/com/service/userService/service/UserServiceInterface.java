package com.service.userService.service;

import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;

import java.util.List;

public interface UserServiceInterface {

    UserResponseDto registerUser(UserRequestDto userRequestDto);

    UserResponseDto getUser(Long id);

    List<UserResponseDto> getAllUsers();
}
