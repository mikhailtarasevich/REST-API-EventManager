package com.mikhail.tarasevich.eventmanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequest {

    @ApiModelProperty(value = "ID пользователя", example = "0", notes = "ID используется в запросах на изменение данных о событии")
    private int id;

    @NotEmpty(message = "Event's name should not be empty")
    @ApiModelProperty(value = "Название события", example = "Concert")
    private String name;

    @NotEmpty(message = "Event's description should not be empty")
    @ApiModelProperty(value = "Описание события", example = "Music concert")
    private String description;

    @NotNull(message = "Event's price should not be empty")
    @Min(value = 0, message = "Event's price should be positive")
    @ApiModelProperty(value = "Цена посещения события", example = "100")
    private int price;

}
