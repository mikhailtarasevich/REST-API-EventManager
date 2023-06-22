package com.mikhail.tarasevich.eventmanager.service.mapper.impl;

import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserEventParticipationMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEventParticipationMapperImpl implements UserEventParticipationMapper {

    @Override
    public UserEventParticipationResponse toResponse(UserEventParticipation entity) {

        return UserEventParticipationResponse.builder()
                .withId(entity.getId())
                .withUser(UserResponse.builder()
                        .withId(entity.getUser() == null ? 0 : entity.getUser().getId())
                        .withEmail(entity.getUser() == null ? "no user" : entity.getUser().getEmail())
                        .build())
                .withEvent(EventResponse.builder()
                        .withId(entity.getEvent().getId())
                        .withUser(UserResponse.builder()
                                .withId(entity.getEvent().getUser().getId())
                                .withEmail(entity.getEvent().getUser().getEmail())
                                .build())
                        .withName(entity.getEvent().getName())
                        .withDescription(entity.getEvent().getDescription())
                        .build())
                .withStatus(entity.getStatus())
                .withFio(entity.getFio())
                .withAge(entity.getAge())
                .withCovidPassportNumber(entity.getCovidPassportNumber())
                .build();
    }

    @Override
    public UserEventParticipation toEntity(UserEventParticipationRequest request) {

        return UserEventParticipation.builder()
                .withId(request.getId())
                .withUser(User.builder().withId(request.getUserId()).build())
                .withEvent(Event.builder().withId(request.getEventId()).build())
                .withStatus(request.getStatus())
                .withFio(request.getFio())
                .withAge(request.getAge())
                .withCovidPassportNumber(request.getCovidPassportNumber())
                .build();
    }

}
