package com.mikhail.tarasevich.eventmanager.dto;

import com.mikhail.tarasevich.eventmanager.util.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEventParticipationRequest {

    @ApiModelProperty(value = "ID зпроса на участие в мероприятии", example = "1", notes = "ID используется в запросах на изменение данных о событии")
    private int id;

    @ApiModelProperty(value = "ID пользователя", notes = "ID используется в запросах на изменение данных о событии")
    private int userId;

    @NotNull(message = "Event ID should not be empty")
    @ApiModelProperty(value = "ID мероприятия", example = "1")
    private int eventId;

    private Status status;

    @NotEmpty(message = "Fio should not be empty")
    @ApiModelProperty(value = "ФИО", example = "Ivanov Ivan Ivanovich")
    private String fio;

    @NotNull(message = "Age should not be empty")
    @Min(value = 0, message = "Age should be positive")
    @Max(value = 150, message = "Age should be less than 150")
    @ApiModelProperty(value = "Возраст", example = "22")
    private int age;

    @NotNull(message = "Covid passport number should not be empty")
    @ApiModelProperty(value = "Цифровой номер ковид-паспорта", example = "11111111")
    private int covidPassportNumber;

}
