package com.mikhail.tarasevich.eventmanager.dto;

import com.mikhail.tarasevich.eventmanager.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEventParticipationResponse {

    private int id;

    private UserResponse user;

    private EventResponse event;

    private Status status;

    private String fio;

    private int age;

    private int covidPassportNumber;

}
