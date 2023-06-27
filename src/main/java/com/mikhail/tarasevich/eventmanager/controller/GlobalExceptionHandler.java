package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.service.exception.AuthenticationDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.AuthorizationException;
import com.mikhail.tarasevich.eventmanager.service.exception.ContractNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.DataBaseException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotValidDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserEventParticipationNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotValidDataException;
import com.mikhail.tarasevich.eventmanager.util.ErrorResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userNotValidDataException(UserNotValidDataException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> contractNotFoundException(ContractNotFoundException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> eventNotFoundException(EventNotFoundException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> eventNotValidDataException(EventNotValidDataException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> incorrectRequestDataException(IncorrectRequestDataException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> authenticationDataException(AuthenticationDataException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> authorizationException(AuthorizationException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> userEventParticipationNotFoundException(UserEventParticipationNotFoundException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage("User has no authority for this endpoint. " + exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> emptyResultDataAccessException(EmptyResultDataAccessException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage("Your request did nothing. " + exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> dataBaseException(DataBaseException exception) {

        return new ResponseEntity<>(ErrorResponse.builder()
                .withMessage(exception.getMessage())
                .withTimestamp(LocalDateTime.now())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

}
