package com.mikhail.tarasevich.eventmanager.service.mapper;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.User;

public interface UserMapper {

    UserResponse toResponse (User entity);

    User toEntity (UserRequest request);

}
