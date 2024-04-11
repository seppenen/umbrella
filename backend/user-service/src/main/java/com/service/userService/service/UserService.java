package com.service.userService.service;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2024
 * Version: 1.0
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.entity.UserEntity;
import com.service.userService.exceptions.UserRegistrationFailedException;
import com.service.userService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final LoggerService loggerService;
    private final ObjectMapper mapper;
    public static final String CREATED_MESSAGE = "User created successfully";
    private static final String USER_NOT_FOUND_MESSAGE = "User with id %d not found";



    public UserService(
            UserRepository userRepository,
            LoggerService loggerService,
            ObjectMapper mapper
    ) {
        this.userRepository = userRepository;
        this.loggerService = loggerService;
        this.mapper = mapper;
    }


    /**
     * Registers a user in the system.
     *
     * @param userRequestDto The user details to be registered.
     * @return The response containing the registered user details.
     * @throws UserRegistrationFailedException If the user registration fails.
     */
    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        try {
            UserEntity userEntity = convertToEntity(userRequestDto);
            UserEntity persistedUser = userRepository.save(userEntity);
            loggerService.logInfo(CREATED_MESSAGE, HttpStatus.OK, persistedUser);
            return convertToDto(persistedUser);
        } catch (Exception e) {
            throw new UserRegistrationFailedException(e);
        }
    }

    /**
     * Retrieves the user details based on the given ID.
     *
     * @param id The ID of the user.
     * @return The response containing the user details.
     * @throws EntityNotFoundException If the user with the given ID is not found.
     */
    @Override
    public UserResponseDto getUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
        return convertToDto(user);
    }


    /**
     * Retrieves all users from the system.
     *
     * @return A list of UserResponseDto objects representing the user details.
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserResponseDto convertToDto(UserEntity user) {
        return mapper.convertValue(user, UserResponseDto.class);
    }

    private UserEntity convertToEntity(UserRequestDto userRequestDto) {
        return mapper.convertValue(userRequestDto, UserEntity.class);
    }
}
