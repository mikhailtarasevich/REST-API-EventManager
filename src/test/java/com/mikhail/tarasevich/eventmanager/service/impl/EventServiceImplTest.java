package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.exception.AuthorizationException;
import com.mikhail.tarasevich.eventmanager.service.exception.DataBaseException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.EventMapper;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventParticipationRepository userEventParticipationRepository;

    @Mock
    private EventMapper mapper;

    @Test
    void findAllManagerEvents_existingUserWithEmail_returnsEventResponseList() {

        String email = "test@example.com";
        User existingUser = User.builder().withId(1).withEmail(email).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(existingUser));

        Event event1 = Event.builder().withId(1).build();
        Event event2 = Event.builder().withId(2).build();
        List<Event> events = List.of(event1, event2);

        when(eventRepository.findEventsByUserIdOrderById(existingUser.getId())).thenReturn(events);

        EventResponse eventResponse1 = EventResponse.builder().withId(1).build();
        EventResponse eventResponse2 = EventResponse.builder().withId(2).build();
        List<EventResponse> expectedEventResponses = List.of(eventResponse1, eventResponse2);

        when(mapper.toResponse(event1)).thenReturn(eventResponse1);
        when(mapper.toResponse(event2)).thenReturn(eventResponse2);

        List<EventResponse> result = eventService.findAllMangerEvents(email);

        assertEquals(expectedEventResponses.size(), result.size());
        assertEquals(expectedEventResponses, result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(eventRepository, times(1)).findEventsByUserIdOrderById(existingUser.getId());
        verify(mapper, times(1)).toResponse(event1);
        verify(mapper, times(1)).toResponse(event2);
    }

    @Test
    void findAllManagerEvents_nonExistingUser_throwsUserNotFoundException() {

        String email = "test@example.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> eventService.findAllMangerEvents(email));
    }

    @Test
    void findEventById_existingEvent_returnsEventResponse() {

        int eventId = 1;

        Event existingEvent = Event.builder().withId(eventId).build();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        EventResponse expectedEventResponse = EventResponse.builder().withId(eventId).build();
        when(mapper.toResponse(existingEvent)).thenReturn(expectedEventResponse);

        EventResponse result = eventService.findEventById(eventId);

        assertEquals(expectedEventResponse, result);
        verify(eventRepository, times(1)).findById(eventId);
        verify(mapper, times(1)).toResponse(existingEvent);
    }

    @Test
    void findEventById_nonExistingEvent_throwsEventNotFoundException() {

        int eventId = 1;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.findEventById(eventId));
    }

    @Test
    void createNewEvent_validInput_returnsEventResponse() {

        String managerEmail = "manager@example.com";
        EventRequest eventRequest = EventRequest.builder().withName("Test Event")
                .withDescription("Test Event")
                .withPrice(100)
                .build();
        Event event = Event.builder().withName("Test Event")
                .withDescription("Test Event")
                .withPrice(100)
                .build();

        User existingUser = User.builder().withEmail(managerEmail)
                .withContracts(List.of(Contract.builder().withStatus(Status.ACCEPTED).build()))
                .build();
        when(userRepository.findUserByEmail(managerEmail)).thenReturn(Optional.of(existingUser));

        when(mapper.toEntity(eventRequest)).thenReturn(event);

        event.setUser(existingUser);
        when(eventRepository.save(event)).thenReturn(event);

        EventResponse eventResponse = EventResponse.builder()
                .withId(1)
                .withName("Test Event")
                .withDescription("Test Event")
                .withPrice(100)
                .build();
        when(mapper.toResponse(event)).thenReturn(eventResponse);

        EventResponse result = eventService.createNewEvent(managerEmail, eventRequest);

        assertEquals(eventResponse, result);
        verify(userRepository, times(1)).findUserByEmail(managerEmail);
        verify(eventRepository, times(1)).save(event);
        verify(mapper, times(1)).toEntity(eventRequest);
        verify(mapper, times(1)).toResponse(event);
    }

    @Test
    void createNewEvent_noApprovedContract_throwsAuthorizationException() {

        String managerEmail = "manager@example.com";
        EventRequest eventRequest = EventRequest.builder().withName("Test Event").build();

        User existingUser = User.builder().withEmail(managerEmail).withContracts(Collections.emptyList()).build();
        when(userRepository.findUserByEmail(managerEmail)).thenReturn(Optional.of(existingUser));

        assertThrows(AuthorizationException.class, () -> eventService.createNewEvent(managerEmail, eventRequest));
    }

    @Test
    void createNewEvent_invalidUser_throwsUserNotFoundException() {

        String managerEmail = "manager@example.com";
        EventRequest eventRequest = EventRequest.builder().withName("Test Event").build();

        when(userRepository.findUserByEmail(managerEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> eventService.createNewEvent(managerEmail, eventRequest));
        verify(userRepository, times(1)).findUserByEmail(managerEmail);
        verify(eventRepository, never()).save(any());
        verify(mapper, never()).toEntity(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void createNewEvent_validInput_expectedDBException() {

        String managerEmail = "manager@example.com";
        EventRequest eventRequest = EventRequest.builder().withName("Test Event")
                .withDescription("Test Event")
                .withPrice(100)
                .build();
        Event event = Event.builder().withName("Test Event")
                .withDescription("Test Event")
                .withPrice(100)
                .build();

        User existingUser = User.builder().withEmail(managerEmail)
                .withContracts(List.of(Contract.builder().withStatus(Status.ACCEPTED).build()))
                .build();
        when(userRepository.findUserByEmail(managerEmail)).thenReturn(Optional.of(existingUser));

        when(mapper.toEntity(eventRequest)).thenReturn(event);

        event.setUser(existingUser);
        doThrow(DataBaseException.class).when(eventRepository).save(event);

        assertThrows(DataBaseException.class, () -> eventService.createNewEvent(managerEmail, eventRequest));

        verify(userRepository, times(1)).findUserByEmail(managerEmail);
        verify(eventRepository, times(1)).save(event);
        verify(mapper, times(1)).toEntity(eventRequest);
    }

    @Test
    void deleteEventById_managerOwnsEvent_eventDeleted() {

        String managerEmail = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder()
                .withId(eventId)
                .withName("Test Event")
                .withUser(User.builder().withEmail(managerEmail).build())
                .build();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEventById(managerEmail, eventId);

        verify(userEventParticipationRepository, times(1)).deleteUserEventParticipationsByEventId(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void deleteEventById_managerDoesNotOwnEvent_incorrectRequestDataExceptionThrown() {

        String managerEmail = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder()
                .withId(eventId)
                .withName("Test Event")
                .withUser(User.builder().withEmail("anothermanager@example.com").build())
                .build();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(IncorrectRequestDataException.class, () -> {
            eventService.deleteEventById(managerEmail, eventId);
        });

        verify(userEventParticipationRepository, never()).deleteUserEventParticipationsByEventId(eventId);
        verify(eventRepository, never()).deleteById(eventId);
    }

    @Test
    void deleteEventById_eventNotFound_eventNotFoundExceptionThrown() {

        String managerEmail = "manager@example.com";
        int eventId = 1;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> {
            eventService.deleteEventById(managerEmail, eventId);
        });

        verify(userEventParticipationRepository, never()).deleteUserEventParticipationsByEventId(eventId);
        verify(eventRepository, never()).deleteById(eventId);
    }

    @Test
    void deleteEventByIdForAdmin_validId_eventDeleted() {

        int eventId = 1;

        eventService.deleteEventByIdForAdmin(eventId);

        verify(userEventParticipationRepository, times(1)).deleteUserEventParticipationsByEventId(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

}