package com.mikhail.tarasevich.eventmanager.service.mapper.impl;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    public UserResponse toResponse (User entity) {

        return UserResponse.builder()
                .withId(entity.getId())
                .withEmail(entity.getEmail())
                .build();
    }

    public User toEntity (UserRequest request) {

        return User.builder()
                .withId(request.getId())
                .withEmail(request.getEmail())
                .withPassword(request.getPassword())
                .build();
    }

}
