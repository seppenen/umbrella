package com.service.userService;

import com.service.userService.dto.UserLoginDto;
import com.service.userService.dto.UserLoginResponseDto;
import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.entity.UserEntity;
import com.service.userService.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class UserFacade {

    private final UserService userService;
    private ReactiveAuthenticationManager authenticationManager;
    private final ModelMapper mapper;



    public Mono<UserLoginResponseDto> login(UserLoginDto userLoginDto) {
        Mono<Authentication> authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

        return authentication.flatMap(auth -> {
            if (auth.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                return Mono.just(new UserLoginResponseDto(userDetails.getUsername()));
            } else {
                return Mono.error(new UsernameNotFoundException("Invalid user request."));
            }
        });
    }

    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        UserEntity userEntityToPersist = convert(userRequestDto, UserEntity.class);
        UserEntity persistedUserEntity = userService.save(userEntityToPersist);
        return convert(persistedUserEntity, UserResponseDto.class);
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
        return mapper.map(source, dest);
    }
}
