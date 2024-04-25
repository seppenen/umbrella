
package com.service.userService;

/**
 * Author: Aleksandr Seppenen
 * Date: 05-04-2023
 * Version: 1.0
 */

import com.service.userService.dto.UserLoginDto;
import com.service.userService.dto.UserLoginResponseDto;
import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.service.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class UserServiceEndpoints {
    private final UserFacade userFacade;

    public UserServiceEndpoints(UserServiceInterface userService, UserFacade userFacade) {

        this.userFacade = userFacade;
    }



    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userFacade.registerUser(userRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

 @PostMapping("/login")
    public Mono<UserLoginResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
     UserLoginResponseDto user = userFacade.login(userLoginDto);
        return Mono.just(user);
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userFacade.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}
