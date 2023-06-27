package com.mikhail.tarasevich.eventmanager.service.mapper;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;

public interface EventMapper {

    EventResponse toResponse (Event entity);

    Event toEntity (EventRequest request);

}
