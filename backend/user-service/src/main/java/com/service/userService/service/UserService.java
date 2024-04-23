package com.service.userService.service;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2024
 * Version: 1.0
 */

import com.service.userService.entity.UserEntity;
import com.service.userService.exceptions.UserRegistrationFailedException;
import com.service.userService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final LoggerService loggerService;
    private final PasswordEncoder passwordEncoder;
    public static final String CREATED_MESSAGE = "User created successfully";
    private static final String USER_NOT_FOUND_MESSAGE = "User %s not found";
    private static final String PASSWORD_NOT_MATCH_MESSAGE = "Password does not match for user: %s";


    public UserService(
            UserRepository userRepository,
            LoggerService loggerService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.loggerService = loggerService;

        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserEntity loginUser(UserEntity userEntity) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userEntity.getEmail());
        if(optionalUser.isEmpty()) {
            throw new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userEntity.getEmail()));
        }

        UserEntity foundUser = optionalUser.get();
        if(!passwordEncoder.matches(userEntity.getPassword(), foundUser.getPassword())) {
            throw new BadCredentialsException(String.format(PASSWORD_NOT_MATCH_MESSAGE, userEntity.getEmail()));
        }
        return foundUser;
    }

    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity persistedUser = userRepository.save(userEntity);
            loggerService.logInfo(CREATED_MESSAGE, HttpStatus.OK, persistedUser);
            return persistedUser;
        } catch (Exception e) {
            throw new UserRegistrationFailedException(e);
        }
    }


    @Override
    public UserEntity getUser(Long id) {
        return findEntityOrThrow(id).orElseThrow(() -> throwNotFound(id));
    }

    @Override
    public List<UserEntity> getAllUsers() {
       return userRepository.findAll();
    }

    private Optional<UserEntity> findEntityOrThrow(Long id) {
        return userRepository.findById(id);
    }
    private RuntimeException throwNotFound(Long id) {
        return new EntityNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id));
    }
    }



