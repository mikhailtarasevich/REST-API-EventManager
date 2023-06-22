package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.UserEventParticipationService;
import com.mikhail.tarasevich.eventmanager.service.exception.DataBaseException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserEventParticipationNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserEventParticipationMapper;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserEventParticipationServiceImpl implements UserEventParticipationService {

    private final UserEventParticipationRepository uepRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final UserEventParticipationMapper mapper;

    @Autowired
    public UserEventParticipationServiceImpl(UserEventParticipationRepository uepRepository,
                                             UserRepository userRepository,
                                             EventRepository eventRepository,
                                             UserEventParticipationMapper mapper) {
        this.uepRepository = uepRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEventParticipationResponse> findPendingUserEventParticipationsByEventId(String email, int eventId) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getEvents().stream().anyMatch(e -> e.getId() == eventId)) {
            return uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.PENDING).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried get information about event' participants which not relate to manager");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEventParticipationResponse> findRejectedUserEventParticipationsByEventId(String email, int eventId) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getEvents().stream().anyMatch(e -> e.getId() == eventId)) {
            return uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.REJECTED).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried get information about event' participants which not relate to manager");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEventParticipationResponse> findAcceptedUserEventParticipationsByEventId(String email, int eventId) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getEvents().stream().anyMatch(e -> e.getId() == eventId)) {
            return uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.ACCEPTED).stream()
                    .map(mapper::toResponse)
                    .collect(Collectors.toList());
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried get information about event' participants which not relate to the manager");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEventParticipationResponse> findAllUserEventParticipationsByUserEmail(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        return uepRepository.findUserEventParticipationsByUserIdOrderById(user.getId()).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserEventParticipationResponse createUserEventParticipation(String email, UserEventParticipationRequest request) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("There is no event with id = " + request.getEventId() + " in DB"));


        UserEventParticipation entity = mapper.toEntity(request);

        entity.setId(0);
        entity.setUser(user);
        entity.setEvent(event);
        entity.setStatus(Status.PENDING);

        try {
            return mapper.toResponse(uepRepository.save(entity));
        } catch (RuntimeException e) {
            throw new DataBaseException("Bad request. Constraint in DB in table user_event_participations. Message: " +
                    e.getMessage());
        }
    }

    @Override
    public void setUserEventParticipationsStatusRejected(String email, int id) {

        UserEventParticipation userEventParticipation = uepRepository.findById(id)
                .orElseThrow(() -> new UserEventParticipationNotFoundException("There is no user event participation request with id = " + id + " in DB"));

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getEvents().stream().anyMatch(e -> e.getId() == userEventParticipation.getEvent().getId())) {
            userEventParticipation.setStatus(Status.REJECTED);
            uepRepository.save(userEventParticipation);
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried change user event participation request status which not relate to the manager");
        }
    }

    @Override
    public void setUserEventParticipationsStatusAccepted(String email, int id) {

        UserEventParticipation userEventParticipation = uepRepository.findById(id)
                .orElseThrow(() -> new UserEventParticipationNotFoundException("There is no user event participation request with id = " + id + " in DB"));

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        if (user.getEvents().stream().anyMatch(e -> e.getId() == userEventParticipation.getEvent().getId())) {
            userEventParticipation.setStatus(Status.ACCEPTED);
            uepRepository.save(userEventParticipation);
        } else {
            throw new IncorrectRequestDataException("Manager with email " + email +
                    " tried change user event participation request status which not relate to the manager");
        }
    }

    @Override
    public void deleteUserEventParticipationsById(String email, int id) {

        UserEventParticipation userEventParticipation = uepRepository.findById(id)
                .orElseThrow(() -> new UserEventParticipationNotFoundException("There is no user event participation request with id = " + id + " in DB"));

        if (userEventParticipation.getUser().getEmail().equals(email)) {
            uepRepository.deleteById(id);
        } else {
            throw new IncorrectRequestDataException("Participant with email " + email +
                    " tried delete user event participation request which not relate to this participant");
        }
    }

    @Override
    public void deleteUserEventParticipationsByIdForAdmin(int id) {

        uepRepository.deleteById(id);
    }

}
