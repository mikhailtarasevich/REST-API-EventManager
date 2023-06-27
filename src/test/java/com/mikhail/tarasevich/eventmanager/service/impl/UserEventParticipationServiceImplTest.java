package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.exception.DataBaseException;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserEventParticipationNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserEventParticipationMapper;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEventParticipationServiceImplTest {

    @InjectMocks
    private UserEventParticipationServiceImpl uepService;

    @Mock
    private UserEventParticipationRepository uepRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserEventParticipationMapper mapper;

    @Test
    void findPendingUserEventParticipationsByEventId_validInput_returnsUserEventParticipationResponses() {

        String email = "manager@example.com";
        int eventId = 1;

        User user = User.builder().withEmail(email).withEvents(List.of(Event.builder().withId(eventId).build())).build();
        Event event = Event.builder().withId(eventId).build();
        UserEventParticipation uep1 = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.PENDING)
                .build();
        UserEventParticipation uep2 = UserEventParticipation.builder()
                .withId(2)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.PENDING)
                .build();

        List<UserEventParticipation> ueps = List.of(uep1, uep2);
        List<UserEventParticipationResponse> expectedResponses = List.of(
                UserEventParticipationResponse.builder().withId(1).build(),
                UserEventParticipationResponse.builder().withId(2).build()
        );

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.PENDING)).thenReturn(ueps);
        when(mapper.toResponse(uep1)).thenReturn(expectedResponses.get(0));
        when(mapper.toResponse(uep2)).thenReturn(expectedResponses.get(1));

        List<UserEventParticipationResponse> responses = uepService.findPendingUserEventParticipationsByEventId(email, eventId);

        assertEquals(expectedResponses, responses);
    }

    @Test
    void findPendingUserEventParticipationsByEventId_userNotRelatedToEvent_throwsIncorrectRequestDataException() {

        String email = "manager@example.com";
        int eventId = 1;

        User user = User.builder().withEmail(email).withEvents(List.of(Event.builder().withId(2).build())).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.findPendingUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findPendingUserEventParticipationsByEventId_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        int eventId = 1;

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.findPendingUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findRejectedUserEventParticipationsByEventId_validInput_returnsUserEventParticipationResponses() {

        String email = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder().withId(eventId).build();
        User user = User.builder().withEmail(email).withEvents(List.of(event)).build();
        UserEventParticipation uep1 = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.REJECTED)
                .build();
        UserEventParticipation uep2 = UserEventParticipation.builder()
                .withId(2)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.REJECTED)
                .build();

        List<UserEventParticipation> ueps = List.of(uep1, uep2);
        List<UserEventParticipationResponse> expectedResponses = List.of(
                UserEventParticipationResponse.builder().withId(1).build(),
                UserEventParticipationResponse.builder().withId(2).build()
        );

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.REJECTED)).thenReturn(ueps);
        when(mapper.toResponse(uep1)).thenReturn(expectedResponses.get(0));
        when(mapper.toResponse(uep2)).thenReturn(expectedResponses.get(1));

        List<UserEventParticipationResponse> responses = uepService.findRejectedUserEventParticipationsByEventId(email, eventId);

        assertEquals(expectedResponses, responses);
    }

    @Test
    void findRejectedUserEventParticipationsByEventId_userNotRelatedToEvent_throwsIncorrectRequestDataException() {

        String email = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder().withId(2).build();
        User user = User.builder().withEmail(email).withEvents(List.of(event)).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.findRejectedUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findRejectedUserEventParticipationsByEventId_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        int eventId = 1;

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.findRejectedUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findAcceptedUserEventParticipationsByEventId_validInput_returnsUserEventParticipationResponses() {

        String email = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder().withId(eventId).build();
        User user = User.builder().withEmail(email).withEvents(List.of(event)).build();
        UserEventParticipation uep1 = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.ACCEPTED)
                .build();
        UserEventParticipation uep2 = UserEventParticipation.builder()
                .withId(2)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.ACCEPTED)
                .build();

        List<UserEventParticipation> ueps = List.of(uep1, uep2);
        List<UserEventParticipationResponse> expectedResponses = List.of(
                UserEventParticipationResponse.builder().withId(1).build(),
                UserEventParticipationResponse.builder().withId(2).build()
        );

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(eventId, Status.ACCEPTED)).thenReturn(ueps);
        when(mapper.toResponse(uep1)).thenReturn(expectedResponses.get(0));
        when(mapper.toResponse(uep2)).thenReturn(expectedResponses.get(1));

        List<UserEventParticipationResponse> responses = uepService.findAcceptedUserEventParticipationsByEventId(email, eventId);

        assertEquals(expectedResponses, responses);
    }

    @Test
    void findAcceptedUserEventParticipationsByEventId_userNotRelatedToEvent_throwsIncorrectRequestDataException() {

        String email = "manager@example.com";
        int eventId = 1;

        Event event = Event.builder().withId(2).build();
        User user = User.builder().withEmail(email).withEvents(List.of(event)).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.findAcceptedUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findAcceptedUserEventParticipationsByEventId_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        int eventId = 1;

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.findAcceptedUserEventParticipationsByEventId(email, eventId));
    }

    @Test
    void findAllUserEventParticipationsByUserId_validInput_returnsUserEventParticipationResponses() {

        String email = "user@example.com";
        int userId = 1;

        User user = User.builder().withId(userId).withEmail(email).build();
        UserEventParticipation uep1 = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(Event.builder().withId(1).build())
                .build();
        UserEventParticipation uep2 = UserEventParticipation.builder()
                .withId(2)
                .withUser(user)
                .withEvent(Event.builder().withId(2).build())
                .build();

        List<UserEventParticipation> ueps = List.of(uep1, uep2);
        List<UserEventParticipationResponse> expectedResponses = List.of(
                UserEventParticipationResponse.builder().withId(1).build(),
                UserEventParticipationResponse.builder().withId(2).build()
        );

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(uepRepository.findUserEventParticipationsByUserIdOrderById(userId)).thenReturn(ueps);
        when(mapper.toResponse(uep1)).thenReturn(expectedResponses.get(0));
        when(mapper.toResponse(uep2)).thenReturn(expectedResponses.get(1));

        List<UserEventParticipationResponse> responses = uepService.findAllUserEventParticipationsByUserEmail(email);

        assertEquals(expectedResponses, responses);
    }

    @Test
    void findAllUserEventParticipationsByUserId_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.findAllUserEventParticipationsByUserEmail(email));
    }

    @Test
    void createUserEventParticipation_validInput_returnsUserEventParticipationResponse() {

        String email = "user@example.com";
        int userId = 1;
        int eventId = 1;

        User user = User.builder().withId(userId).withEmail(email).build();
        Event event = Event.builder().withId(eventId).build();
        UserEventParticipationRequest request = UserEventParticipationRequest.builder().withEventId(eventId).build();

        UserEventParticipation entity = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.PENDING)
                .build();

        UserEventParticipationResponse expectedResponse = UserEventParticipationResponse.builder()
                .withId(1)
                .build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(uepRepository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(expectedResponse);

        UserEventParticipationResponse response = uepService.createUserEventParticipation(email, request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void createUserEventParticipation_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        UserEventParticipationRequest request = UserEventParticipationRequest.builder().withEventId(1).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.createUserEventParticipation(email, request));
    }

    @Test
    void createUserEventParticipation_eventNotFound_throwsEventNotFoundException() {

        String email = "user@example.com";
        int userId = 1;
        int eventId = 1;

        User user = User.builder().withId(userId).withEmail(email).build();
        UserEventParticipationRequest request = UserEventParticipationRequest.builder().withEventId(eventId).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> uepService.createUserEventParticipation(email, request));
    }

    @Test
    void createUserEventParticipation_validInput_expectedDBException() {

        String email = "user@example.com";
        int userId = 1;
        int eventId = 1;

        User user = User.builder().withId(userId).withEmail(email).build();
        Event event = Event.builder().withId(eventId).build();
        UserEventParticipationRequest request = UserEventParticipationRequest.builder().withEventId(eventId).build();

        UserEventParticipation entity = UserEventParticipation.builder()
                .withId(1)
                .withUser(user)
                .withEvent(event)
                .withStatus(Status.PENDING)
                .build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(mapper.toEntity(request)).thenReturn(entity);
        doThrow(DataBaseException.class).when(uepRepository).save(entity);

        assertThrows(DataBaseException.class, () -> uepService.createUserEventParticipation(email, request));
    }

    @Test
    void setUserEventParticipationsStatusRejected_validInput_updatesUserEventParticipationStatus() {

        String email = "manager@example.com";
        int id = 1;

        Event event = Event.builder().withId(5).build();
        User manager = User.builder().withEmail(email).withEvents(List.of(event)).build();
        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withEvent(event).withStatus(Status.PENDING).build();
        UserEventParticipation uepRejected = UserEventParticipation.builder().withId(id).withEvent(event).withStatus(Status.REJECTED).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(manager));
        when(uepRepository.save(uepRejected)).thenReturn(uepRejected);

        uepService.setUserEventParticipationsStatusRejected(email, id);

        assertEquals(Status.REJECTED, uep.getStatus());
        verify(uepRepository, times(1)).save(uepRejected);
    }

    @Test
    void setUserEventParticipationsStatusRejected_userNotRelatedToEvent_throwsIncorrectRequestDataException() {

        String email = "manager@example.com";
        int id = 1;

        Event event1 = Event.builder().withId(1).build();
        Event event2 = Event.builder().withId(2).build();
        User manager = User.builder().withEmail(email).withEvents(List.of(event1)).build();
        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withEvent(event2).withStatus(Status.PENDING).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(manager));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.setUserEventParticipationsStatusRejected(email, id));
    }

    @Test
    void setUserEventParticipationsStatusRejected_userEventParticipationNotFound_throwsUserEventParticipationNotFoundException() {

        String email = "manager@example.com";
        int id = 1;

        when(uepRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserEventParticipationNotFoundException.class,
                () -> uepService.setUserEventParticipationsStatusRejected(email, id));
    }

    @Test
    void setUserEventParticipationsStatusRejected_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        int id = 1;

        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withStatus(Status.PENDING).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.setUserEventParticipationsStatusRejected(email, id));
    }

    @Test
    void setUserEventParticipationsStatusAccepted_validInput_updatesUserEventParticipationStatus() {

        String email = "manager@example.com";
        int id = 1;

        Event event = Event.builder().withId(5).build();
        User manager = User.builder().withEmail(email).withEvents(List.of(event)).build();
        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withEvent(event).withStatus(Status.PENDING).build();
        UserEventParticipation uepAccepted = UserEventParticipation.builder().withId(id).withEvent(event).withStatus(Status.ACCEPTED).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(manager));
        when(uepRepository.save(uepAccepted)).thenReturn(uepAccepted);

        uepService.setUserEventParticipationsStatusAccepted(email, id);

        assertEquals(Status.ACCEPTED, uep.getStatus());
        verify(uepRepository, times(1)).save(uepAccepted);
    }

    @Test
    void setUserEventParticipationsStatusAccepted_userNotRelatedToEvent_throwsIncorrectRequestDataException() {

        String email = "manager@example.com";
        int id = 1;

        Event event1 = Event.builder().withId(1).build();
        Event event2 = Event.builder().withId(2).build();
        User manager = User.builder().withEmail(email).withEvents(List.of(event1)).build();
        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withEvent(event2).withStatus(Status.PENDING).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(manager));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.setUserEventParticipationsStatusAccepted(email, id));
    }

    @Test
    void setUserEventParticipationsStatusAccepted_userEventParticipationNotFound_throwsUserEventParticipationNotFoundException() {

        String email = "manager@example.com";
        int id = 1;

        when(uepRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserEventParticipationNotFoundException.class,
                () -> uepService.setUserEventParticipationsStatusAccepted(email, id));
    }

    @Test
    void setUserEventParticipationsStatusAccepted_userNotFound_throwsUserNotFoundException() {

        String email = "nonexisting@example.com";
        int id = 1;

        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withStatus(Status.PENDING).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> uepService.setUserEventParticipationsStatusAccepted(email, id));
    }

    @Test
    void deleteUserEventParticipationsById_validInput_deletesUserEventParticipation() {

        String email = "participant@example.com";
        int id = 1;

        User participant = User.builder().withEmail(email).build();
        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withUser(participant).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));

        uepService.deleteUserEventParticipationsById(email, id);

        verify(uepRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUserEventParticipationsById_userNotOwnerOfEventParticipation_throwsIncorrectRequestDataException() {

        String email = "participant@example.com";
        int id = 1;

        UserEventParticipation uep = UserEventParticipation.builder().withId(id).withUser(User.builder().withEmail("other@example.com").build()).build();

        when(uepRepository.findById(id)).thenReturn(Optional.of(uep));

        assertThrows(IncorrectRequestDataException.class,
                () -> uepService.deleteUserEventParticipationsById(email, id));
    }

    @Test
    void deleteUserEventParticipationsById_userEventParticipationNotFound_throwsUserEventParticipationNotFoundException() {

        String email = "participant@example.com";
        int id = 1;

        when(uepRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserEventParticipationNotFoundException.class,
                () -> uepService.deleteUserEventParticipationsById(email, id));
    }

    @Test
    void deleteUserEventParticipationsByIdForAdmin_validInput_deletesUserEventParticipation() {

        int id = 1;

        doNothing().when(uepRepository).deleteById(id);

        uepService.deleteUserEventParticipationsByIdForAdmin(id);

        verify(uepRepository, times(1)).deleteById(id);
    }

}
