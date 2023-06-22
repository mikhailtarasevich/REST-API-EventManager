package com.mikhail.tarasevich.eventmanager.service;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> findAllMangerEvents(String email);

    EventResponse findEventById(int id);

    EventResponse createNewEvent(String email, EventRequest request);

    void deleteEventById(String email, int id);

    void deleteEventByIdForAdmin(int id);

}
