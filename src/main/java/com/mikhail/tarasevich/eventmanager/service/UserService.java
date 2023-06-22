package com.mikhail.tarasevich.eventmanager.service;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findUserById(int id);

    UserResponse findUserByEmail(String email);

    UserResponse createManager(UserRequest request);

    UserResponse createParticipant(UserRequest request);

    List<UserResponse> findAdmins();

    List<UserResponse> findManagers();

    List<UserResponse> findParticipants();

    void deleteUserById(int id);

}
