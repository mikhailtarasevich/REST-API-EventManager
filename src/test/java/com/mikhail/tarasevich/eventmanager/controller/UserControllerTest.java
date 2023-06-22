package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @WithMockUser
    void showUser_authenticatedUser_returnsUserResponse() throws Exception {

        int id = 1;
        String userEmail = "test@example.com";

        UserResponse userResponse = UserResponse.builder()
                .withId(id)
                .withEmail(userEmail)
                .build();

        when(userService.findUserByEmail(userEmail)).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/")
                        .principal(new UsernamePasswordAuthenticationToken(userEmail, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"email\":\"test@example.com\"}"));

        verify(userService, times(1)).findUserByEmail(userEmail);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showAllAdmins_authenticatedAdmin_returnsListOfAdmins() throws Exception {

        List<UserResponse> admins = Arrays.asList(
                UserResponse.builder().withId(1).withEmail("admin1@example.com").build(),
                UserResponse.builder().withId(2).withEmail("admin2@example.com").build()
        );
        when(userService.findAdmins()).thenReturn(admins);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/admins"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"email\":\"admin1@example.com\"},{\"id\":2,\"email\":\"admin2@example.com\"}]"));

        verify(userService, times(1)).findAdmins();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showAllManagers_authenticatedAdmin_returnsListOfManagers() throws Exception {

        List<UserResponse> managers = Arrays.asList(
                UserResponse.builder().withId(1).withEmail("manager1@example.com").build(),
                UserResponse.builder().withId(2).withEmail("manager2@example.com").build()
        );

        when(userService.findManagers()).thenReturn(managers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/managers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"email\":\"manager1@example.com\"},{\"id\":2,\"email\":\"manager2@example.com\"}]"));

        verify(userService, times(1)).findManagers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showAllParticipants_authenticatedAdmin_returnsListOfParticipants() throws Exception {

        List<UserResponse> participants = Arrays.asList(
                UserResponse.builder().withId(1).withEmail("participant1@example.com").build(),
                UserResponse.builder().withId(2).withEmail("participant2@example.com").build()
        );

        when(userService.findParticipants()).thenReturn(participants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/participants"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"email\":\"participant1@example.com\"},{\"id\":2,\"email\":\"participant2@example.com\"}]"));

        verify(userService, times(1)).findParticipants();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void deleteUser_authenticatedAdmin_returnsOkResponse() throws Exception {

        int userId = 8;
        doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/")
                        .param("id", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id = 8 has been deleted"));

        verify(userService, times(1)).deleteUserById(userId);
        verifyNoMoreInteractions(userService);
    }

}
