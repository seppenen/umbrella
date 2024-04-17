package com.service.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userService.dto.UserRequestDto;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.entity.UserEntity;
import com.service.userService.exceptions.UserRegistrationFailedException;
import com.service.userService.repository.UserRepository;
import com.service.userService.service.LoggerService;
import com.service.userService.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for UserService.
 *
 * This class contains unit tests for the methods in the UserService class. Dependencies such as ObjectMapper,
 * UserRepository, and LoggerService are mocked to isolate the tests and ensure they focus on the behavior of
 * UserService only.
 */
public class UserServiceTest {

    @MockBean
    private static UserFacade userFacade;

    @MockBean
    private static UserRepository userRepository;
    @MockBean
    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
         userRepository = Mockito.mock(UserRepository.class);
         mapper = Mockito.mock(ObjectMapper.class);
         userFacade = Mockito.mock(UserFacade.class);

    }

    /**
     * Test method for {@link UserService#getUser(Long)}.
     *
     * This method tests the functionality of the getUser(Long) method by providing a valid user ID.
     * It verifies that the correct UserResponseDto is received from the UserService.
     *
     * Steps performed in the test:
     * 1. Create a Long variable userId with a value of 1L representing the user ID.
     * 2. Create a new UserEntity instance expectedUserEntity and set its name to "test".
     * 3. Create a new UserResponseDto instance expectedResponseDto and set its name to "test".
     * 4. Configure the mock behavior for userService.getUser(userId) to return expectedResponseDto.
     * 5. Configure the mock behavior for mapper.convertValue(expectedUserEntity, UserResponseDto.class) to return expectedResponseDto.
     * 6. Call the userService.getUser(userId) method and store the result in actualResponseDto.
     * 7. Assert that the expectedResponseDto is equal to the actualResponseDto.
     *
     * @since 1.0
     */
    @Test
    void testGetUserValidId() {
        Long userId = 1L;
        UserEntity expectedUserEntity = new UserEntity();
        expectedUserEntity.setUsername("test");

        UserResponseDto expectedResponseDto = new UserResponseDto();
        expectedResponseDto.setUsername("test");

        when(userFacade.getUser(userId)).thenReturn(expectedResponseDto);
        when(mapper.convertValue(expectedUserEntity, UserResponseDto.class)).thenReturn(expectedResponseDto);
        UserResponseDto actualResponseDto = userFacade.getUser(userId);
        assertEquals(expectedResponseDto, actualResponseDto);
    }

    /**
     * Test method to validate the behavior of getting a user with an invalid ID.
     *
     * This method verifies that if an EntityNotFoundException is thrown when trying
     * to get a user with an invalid ID, the exception is properly propagated.
     *
     * @throws EntityNotFoundException if the user with the specified ID doesn't exist
     */
    @Test
    void testGetUserInvalidId() {
        Long userId = 1L;
        when(userFacade.getUser(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> userFacade.getUser(userId));
    }

    /*
     * Test class for UserService where we mocks dependencies
     * i.e ObjectMapper, UserRepository, LoggerService and
     * testing registerUser() method.
     */

    @Test
    void registerUserSuccessfulTest() {

        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        LoggerService loggerService = Mockito.mock(LoggerService.class);
        UserService userService = new UserService(userRepository, loggerService, passwordEncoder );

        UserEntity user = new UserEntity();
        UserRequestDto userRequestDto = new UserRequestDto();
        UserResponseDto userResponseDto = new UserResponseDto();

        Mockito.when(objectMapper.convertValue(userRequestDto, UserEntity.class)).thenReturn(user);
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        Mockito.when(objectMapper.convertValue(user, UserResponseDto.class)).thenReturn(userResponseDto);

        assertEquals(userResponseDto, userFacade.registerUser(userRequestDto));
    }

    @Test
    void registerUserFailedTest() {
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        LoggerService loggerService = Mockito.mock(LoggerService.class);
        UserService userService = new UserService(userRepository, loggerService, passwordEncoder );

        UserRequestDto userRequestDto = new UserRequestDto();

        Mockito.when(objectMapper.convertValue(userRequestDto, UserEntity.class)).thenThrow(IllegalStateException.class);

        assertThrows(UserRegistrationFailedException.class, () -> userFacade.registerUser(userRequestDto));
    }
}
