package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.EventService;
import com.mikhail.tarasevich.eventmanager.service.exception.AuthorizationException;
import com.mikhail.tarasevich.eventmanager.service.exception.DataBaseException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.EventMapper;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final UserEventParticipationRepository userEventParticipationRepository;

    private final EventMapper mapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            UserEventParticipationRepository userEventParticipationRepository,
                            EventMapper mapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userEventParticipationRepository = userEventParticipationRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> findAllMangerEvents(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        return eventRepository.findEventsByUserIdOrderById(user.getId()).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse findEventById(int id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("There is no event with id = " + id + " in DB"));

        return mapper.toResponse(event);
    }

    @Override
    public EventResponse createNewEvent(String email, EventRequest request) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getContracts().stream().noneMatch(c -> c.getStatus().equals(Status.ACCEPTED)))
            throw new AuthorizationException("Manager with email = " + email + " has no approved contract");

        Event event = mapper.toEntity(request);

        event.setUser(user);

        try {
            return mapper.toResponse(eventRepository.save(event));
        } catch (RuntimeException e) {
            throw new DataBaseException("Bad request. Constraint in DB in table events. Message: " + e.getMessage());
        }
    }

    @Override
    public void deleteEventById(String email, int id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("There is no event with id = " + id + " in DB"));

        if (event.getUser().getEmail().equals(email)) {
            userEventParticipationRepository.deleteUserEventParticipationsByEventId(id);
            eventRepository.deleteById(id);
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried delete event which not relate to the manager");
        }
    }

    @Override
    public void deleteEventByIdForAdmin(int id) {

        userEventParticipationRepository.deleteUserEventParticipationsByEventId(id);
        eventRepository.deleteById(id);
    }

}
