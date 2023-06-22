package com.mikhail.tarasevich.eventmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventResponse {

    private int id;

    private UserResponse user;

    private String name;

    private String description;

    private int price;

}
