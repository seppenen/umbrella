
package com.service.userService;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */

import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.service.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserServiceEndpoints {
    private final UserServiceInterface userService;

    public UserServiceEndpoints(UserServiceInterface userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param userRequestDto The request body containing user information
     * @return ResponseEntity<UserResponseDto> The response entity with the created user information
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity<UserResponseDto> The response entity with the retrieved user information.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users from the system.
     *
     * @return A list of UserResponseDto objects representing the user details.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}
