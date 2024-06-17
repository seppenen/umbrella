package com.service.userService;

import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.entity.UserEntity;
import com.service.userService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @Mock
    private UserService userServiceMock;

    @Mock
    private ReactiveAuthenticationManager authManagerMock;

    @Mock
    private ModelMapper modelMapperMock;

    private UserFacade userFacade;

    // Create userFacade object before each test
    @BeforeEach
    void setUp() {
        userFacade = new UserFacade(userServiceMock, authManagerMock, modelMapperMock);
    }

    @Test
    void testRegisterUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userServiceMock.save(userEntity)).thenReturn(userEntity);
        when(modelMapperMock.map(userRequestDto, UserEntity.class)).thenReturn(userEntity);
        when(modelMapperMock.map(userEntity, UserResponseDto.class)).thenReturn(userResponseDto);

        userFacade.registerUser(userRequestDto);


        verify(userServiceMock, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testGetUser() {
        UserEntity userEntity = new UserEntity();
        UserResponseDto userResponseDto = new UserResponseDto();
        when(userServiceMock.getUser(1L)).thenReturn(userEntity);
        when(modelMapperMock.map(userEntity, UserResponseDto.class)).thenReturn(userResponseDto);

        userFacade.getUser(1L);

        verify(userServiceMock, times(1)).getUser(1L);
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> usersEntities = Stream.of(new UserEntity(), new UserEntity()).collect(Collectors.toList());
        List<UserResponseDto> usersDtos = Stream.of(new UserResponseDto(), new UserResponseDto()).collect(Collectors.toList());

        when(userServiceMock.getAllUsers()).thenReturn(usersEntities);

        for (int i = 0; i < usersEntities.size(); i++) {
            when(modelMapperMock.map(usersEntities.get(i), UserResponseDto.class)).thenReturn(usersDtos.get(i));
        }

        List<UserResponseDto> result = userFacade.getAllUsers();
        assertEquals(usersDtos, result);

        verify(userServiceMock, times(1)).getAllUsers();
    }

    // test for login() method is not provided due to its asynchronous nature
    // Consider using StepVerifier or similar library for testing
}
