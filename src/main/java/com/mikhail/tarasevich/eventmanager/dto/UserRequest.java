package com.mikhail.tarasevich.eventmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

    @ApiModelProperty(value = "ID пользователя", example = "1", notes = "ID используется в запросах на изменение данных о пользователе")
    private int id;

    @Email(message = "Incorrect email format")
    @ApiModelProperty(value = "Email пользователя", example = "john.doe@example.com", required = true)
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @ApiModelProperty(value = "Password пользователя", example = "1111", required = true)
    private String password;

    @NotEmpty(message = "Confirm password should not be empty")
    @ApiModelProperty(value = "Confirm password подтверждение  правильности пароля пользователя", example = "1111", required = true)
    private String confirmPassword;

}
