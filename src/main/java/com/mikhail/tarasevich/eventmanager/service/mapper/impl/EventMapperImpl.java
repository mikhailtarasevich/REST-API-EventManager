package com.mikhail.tarasevich.eventmanager.service.mapper.impl;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.service.mapper.EventMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventResponse toResponse(Event entity) {

        return EventResponse.builder()
                .withId(entity.getId())
                .withUser(UserResponse.builder()
                        .withId(entity.getUser().getId())
                        .withEmail(entity.getUser().getEmail())
                        .build())
                .withName(entity.getName())
                .withDescription(entity.getDescription())
                .withPrice(entity.getPrice())
                .build();
    }

    @Override
    public Event toEntity(EventRequest request) {

        return Event.builder()
                .withId(request.getId())
                .withUser(User.builder().withId(request.getId()).build())
                .withName(request.getName())
                .withDescription(request.getDescription())
                .withPrice(request.getPrice())
                .build();
    }

}
