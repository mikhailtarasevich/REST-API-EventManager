package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.LoginRequest;
import com.mikhail.tarasevich.eventmanager.dto.LoginResponse;
import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.security.JWTUtil;
import com.mikhail.tarasevich.eventmanager.service.UserService;
import com.mikhail.tarasevich.eventmanager.service.exception.AuthenticationDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotValidDataException;
import com.mikhail.tarasevich.eventmanager.util.BindingResultValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "Контроллер для аутентификации пользователей в системе")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JWTUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration/manager")
    @ApiOperation(value = "Зарегестрировать нового менджера мероприятий", response = LoginResponse.class)
    public LoginResponse performMangerRegistration(
            @ApiParam(value = "Данные о новом пользователе", required = true) @RequestBody @Valid UserRequest userRequest,
            BindingResult bindingResult) {

        BindingResultValidator.checkErrorsInBindingResult(bindingResult, UserNotValidDataException.class);

        UserResponse userResponse = userService.createManager(userRequest);

        String token = jwtUtil.generateToken(userResponse.getEmail());

        return LoginResponse.builder().withToken(token).build();
    }

    @PostMapping("/registration/participant")
    @ApiOperation(value = "Зарегестрировать нового участника мероприятий", response = LoginResponse.class)
    public LoginResponse performParticipantRegistration(
            @ApiParam(value = "Данные о новом пользователе", required = true) @RequestBody @Valid UserRequest userRequest,
            BindingResult bindingResult) {

        BindingResultValidator.checkErrorsInBindingResult(bindingResult, UserNotValidDataException.class);

        UserResponse userResponse = userService.createParticipant(userRequest);

        String token = jwtUtil.generateToken(userResponse.getEmail());

        return LoginResponse.builder().withToken(token).build();
    }

    @PostMapping("/login")
    @ApiOperation(value = "Пройти аутентификацию и авторизацию в системе с указанием email и password", response = LoginResponse.class)
    public LoginResponse performLogin(
            @ApiParam(value = "Данные о пользователе (email, password)", required = true) @RequestBody @Valid LoginRequest request,
            BindingResult bindingResult) {

        BindingResultValidator.checkErrorsInBindingResult(bindingResult, UserNotValidDataException.class);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new AuthenticationDataException("Incorrect credentials. Try login again.");
        }

        String token = jwtUtil.generateToken(request.getEmail());

        return LoginResponse.builder().withToken(token).build();
    }

}
