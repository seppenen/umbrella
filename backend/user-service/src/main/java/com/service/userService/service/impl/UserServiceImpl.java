package com.service.userService.service.impl;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2024
 * Version: 1.0
 */

import com.service.userService.entity.UserEntity;
import com.service.userService.repository.UserRepository;
import com.service.userService.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public static final String CREATED_MESSAGE = "User created successfully";
    private static final String USER_NOT_FOUND_MESSAGE = "User %s not found";

    @Override
    public UserEntity save(UserEntity userEntity) {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserEntity persistedUser = userRepository.save(userEntity);
            logger.info("{} with ID: {}.", CREATED_MESSAGE, persistedUser.getEmail());
            return persistedUser;
        } catch (Exception e) {
            throw new RuntimeException(e);
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



