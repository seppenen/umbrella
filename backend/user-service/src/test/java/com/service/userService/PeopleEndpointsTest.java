package com.service.userService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userService.dto.UserResponseDto;
import com.service.userService.service.impl.LoggerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PeopleEndpointsTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserServiceEndpoints peopleEndpoints;
    @MockBean
    private UserFacade userFacade;
    @MockBean
    private LoggerServiceImpl loggerService;


    @Test
    public void getAllUsersTest() throws Exception {

        // Setup: create a mock user.
        UserResponseDto mockUser = new UserResponseDto();
        mockUser.setId(1L);
        mockUser.setUsername("test_username");
        mockUser.setEmail("test_email");

        // Setup: create a list with mock user.
        List<UserResponseDto> mockUsers = Collections.singletonList(mockUser);

        // Setup: set expectation for userService.getAllUsers.
        when(userFacade.getAllUsers()).thenReturn(mockUsers);

        // Execute: call the tested method.
        mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk());

        // Assert: check expected response.
        List<UserResponseDto> responseUsers = userFacade.getAllUsers();
        assertThat(responseUsers).hasSize(1);
        assertThat(responseUsers.get(0).getId()).isEqualTo(mockUser.getId());
        assertThat(responseUsers.get(0).getUsername()).isEqualTo(mockUser.getUsername());
        assertThat(responseUsers.get(0).getEmail()).isEqualTo(mockUser.getEmail());
    }




    @Test
    public void registerUser_validUser_userRegistered() throws Exception {

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername("test");

        when(userFacade.registerUser(any())).thenReturn(userResponseDto);

        String expectedResponse = asJsonString(userResponseDto);

        mvc.perform(post("/register")
                        .content(asJsonString(userResponseDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    public static <T> String asJsonString(final T obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(obj);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
