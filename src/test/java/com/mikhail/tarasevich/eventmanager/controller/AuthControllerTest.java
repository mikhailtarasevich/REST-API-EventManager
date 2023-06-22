package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.LoginRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.security.JWTUtil;
import com.mikhail.tarasevich.eventmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void performParticipantRegistration_inputValidRequest_expectedJWTToken() throws Exception {

        UserRequest userRequest = UserRequest.builder()
                .withEmail("john.doe@example.com")
                .withPassword("1111")
                .withConfirmPassword("1111")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .withId(1)
                .withEmail(userRequest.getEmail())
                .build();

        String token = "test-token";

        when(userService.createParticipant(userRequest)).thenReturn(userResponse);
        when(jwtUtil.generateToken(userResponse.getEmail())).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration/participant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"1111\",\"confirmPassword\":\"1111\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"test-token\"}"));

        verify(userService, times(1)).createParticipant(userRequest);
        verify(jwtUtil, times(1)).generateToken(userResponse.getEmail());
        verifyNoMoreInteractions(userService, jwtUtil);
    }

    @Test
    void performManagerRegistration_inputValidRequest_expectedJWTToken() throws Exception {

        UserRequest userRequest = UserRequest.builder()
                .withEmail("john.doe@example.com")
                .withPassword("1111")
                .withConfirmPassword("1111")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .withId(1)
                .withEmail(userRequest.getEmail())
                .build();

        String token = "test-token";

        when(userService.createManager(userRequest)).thenReturn(userResponse);
        when(jwtUtil.generateToken(userResponse.getEmail())).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/registration/manager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"1111\",\"confirmPassword\":\"1111\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"test-token\"}"));

        verify(userService, times(1)).createManager(userRequest);
        verify(jwtUtil, times(1)).generateToken(userResponse.getEmail());
        verifyNoMoreInteractions(userService, jwtUtil);
    }

    @Test
    void performLogin_validLoginRequest_returnsLoginResponse() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .withEmail("john.doe@example.com")
                .withPassword("1111")
                .build();

        String token = "test-token";

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        when(jwtUtil.generateToken(loginRequest.getEmail())).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"1111\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"token\":\"test-token\"}"));

        verify(authenticationManager, times(1)).authenticate(authInputToken);
        verify(jwtUtil, times(1)).generateToken(loginRequest.getEmail());
        verifyNoMoreInteractions(authenticationManager, userService, jwtUtil);
    }

    @Test
    void performLogin_authenticationFailure_throwsAuthenticationDataException() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .withEmail("john.doe@example.com")
                .withPassword("1111")
                .build();

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        when(authenticationManager.authenticate(authInputToken))
                .thenThrow(new BadCredentialsException("Incorrect credentials"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"1111\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Incorrect credentials. Try login again."));

        verify(authenticationManager, times(1)).authenticate(authInputToken);
        verifyNoMoreInteractions(authenticationManager);
    }

}
