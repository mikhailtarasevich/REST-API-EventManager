package com.mikhail.tarasevich.eventmanager.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Класс для аутентификации пользователя в системе")
public class LoginRequest {

    @Email(message = "Incorrect email format")
    @ApiModelProperty(value = "Email пользователя", example = "admin@example.com", required = true)
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @ApiModelProperty(value = "Password пользователя", example = "1111", required = true)
    private String password;

}
