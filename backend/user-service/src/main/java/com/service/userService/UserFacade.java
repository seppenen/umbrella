package com.service.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userService.dto.UserLoginDto;
import com.service.userService.dto.UserLoginResponseDto;
import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.entity.UserEntity;
import com.service.userService.service.UserServiceInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFacade {

    private final UserServiceInterface userService;
    private final ObjectMapper mapper;

    public UserFacade(UserServiceInterface userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public UserLoginResponseDto login(UserLoginDto userLoginDto) {
        UserEntity userEntity = userService.loginUser(convert(userLoginDto, UserEntity.class));
        UserLoginResponseDto userLoginResponseDto = convert(userEntity, UserLoginResponseDto.class);
        userLoginResponseDto.setId(userLoginResponseDto.getId());
        userLoginResponseDto.setEmail(userEntity.getEmail());
        return userLoginResponseDto;
    }

    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = userService.registerUser(convert(userRequestDto, UserEntity.class));
        return convert(userEntity, UserResponseDto.class);
    }

    public UserResponseDto getUser(Long id) {
        UserEntity userEntity = userService.getUser(id);
        return convert(userEntity, UserResponseDto.class);
    }

    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return users.stream().map(user -> convert(user, UserResponseDto.class)).toList();
    }

    private <T, U> U convert(T source, Class<U> dest) {
        return mapper.convertValue(source, dest);
    }
}
