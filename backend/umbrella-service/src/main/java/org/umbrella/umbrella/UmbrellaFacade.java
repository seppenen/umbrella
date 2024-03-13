package org.umbrella.umbrella;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.umbrella.umbrella.client.UserServiceClient;
import org.umbrella.umbrella.dto.UserRequestDto;
import org.umbrella.umbrella.dto.UserResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UmbrellaFacade {

    private final UserServiceClient userServiceClient;
    private final ObjectMapper mapper;


    public UmbrellaFacade(UserServiceClient userServiceClient, ObjectMapper mapper) {
        this.userServiceClient = userServiceClient;
        this.mapper = mapper;
    }

    /**
     * Retrieves the list of users.
     *
     * @return the list of users as a List of UserResponseDto objects
     */
    public List<UserResponseDto> getUsersFromUserService() {
        List<UserRequestDto> users = userServiceClient.getUsers();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserResponseDto convertToDto(UserRequestDto userRequestDto) {
        return mapper.convertValue(userRequestDto, UserResponseDto.class);
    }
}
