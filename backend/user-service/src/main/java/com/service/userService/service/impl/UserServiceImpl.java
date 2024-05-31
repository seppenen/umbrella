package com.service.userService.service.impl;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2024
 * Version: 1.0
 */

import com.service.userService.entity.UserEntity;
import com.service.userService.exceptions.EntityPersistenceException;
import com.service.userService.repository.UserRepository;
import com.service.userService.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoggerServiceImpl loggerService;
    private final PasswordEncoder passwordEncoder;
    public static final String CREATED_MESSAGE = "User created successfully";
    private static final String USER_NOT_FOUND_MESSAGE = "User %s not found";
    private static final String PASSWORD_NOT_MATCH_MESSAGE = "Password does not match for user: %s";


    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity persistedUser = userRepository.save(userEntity);
            loggerService.logInfo(CREATED_MESSAGE, HttpStatus.OK, persistedUser);
            return persistedUser;
        } catch (Exception e) {
            throw new EntityPersistenceException(e);
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



