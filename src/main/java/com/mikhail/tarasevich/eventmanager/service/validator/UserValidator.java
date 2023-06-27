package com.mikhail.tarasevich.eventmanager.service.validator;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;

public interface UserValidator {

    void validateEmail(UserRequest request);

    void validatePassword(UserRequest request);

}
