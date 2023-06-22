package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventService eventService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void showAllMangerEvents_returnsListOfEvents() throws Exception {

        String userName = "testUser";

        EventResponse event1 = EventResponse.builder()
                .withId(1)
                .withName("Event 1")
                .build();
        EventResponse event2 = EventResponse.builder()
                .withId(2)
                .withName("Event 2")
                .build();

        List<EventResponse> events = List.of(event1, event2);

        when(eventService.findAllMangerEvents(userName)).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/")
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Event 1\"},{\"id\":2,\"name\":\"Event 2\"}]"));

        verify(eventService, times(1)).findAllMangerEvents(userName);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void createNewEvent_returnsCreatedEvent() throws Exception {
        String userName = "testUser";

        EventRequest eventRequest = EventRequest.builder()
                .withName("New Event")
                .withDescription("New Event")
                .withPrice(100)
                .build();

        EventResponse createdEvent = EventResponse.builder()
                .withId(1)
                .withName("New Event")
                .withDescription("New Event")
                .withPrice(100)
                .build();

        when(eventService.createNewEvent(userName, eventRequest)).thenReturn(createdEvent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/event/")
                        .principal(new UsernamePasswordAuthenticationToken(userName, ""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Event\",\"description\":\"New Event\",\"price\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"New Event\",\"description\":\"New Event\",\"price\":100}"));

        verify(eventService, times(1)).createNewEvent(userName, eventRequest);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void deleteEvent_validEventId_returnsOk() throws Exception {
        String userName = "testUser";
        int eventId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/event/manager")
                        .principal(new UsernamePasswordAuthenticationToken(userName, ""))
                        .param("id", String.valueOf(eventId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Event with id = " + eventId + " has been deleted"));

        verify(eventService, times(1)).deleteEventById(userName, eventId);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void deleteEventByAdmin_validEventId_returnsOk() throws Exception {
        int eventId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/event/admin")
                        .param("id", String.valueOf(eventId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Event with id = " + eventId + " has been deleted by Admin"));

        verify(eventService, times(1)).deleteEventByIdForAdmin(eventId);
        verifyNoMoreInteractions(eventService);
    }

}
