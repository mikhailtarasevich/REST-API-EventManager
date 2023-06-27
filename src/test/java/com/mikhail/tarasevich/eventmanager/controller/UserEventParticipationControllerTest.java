package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.service.UserEventParticipationService;
import com.mikhail.tarasevich.eventmanager.util.Status;
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
class UserEventParticipationControllerTest {

    @InjectMocks
    private UserEventParticipationController uepController;

    @Mock
    private UserEventParticipationService uepService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(uepController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void showAllPendingUserEventParticipationsForManager_returnUserEventParticipationResponseList() throws Exception {

        int eventId = 1;
        String userName = "manager@example.com";

        UserEventParticipationResponse uepResponse1 = UserEventParticipationResponse.builder()
                .withId(1)
                .withFio("Ivanov")
                .withStatus(Status.PENDING)
                .build();

        UserEventParticipationResponse uepResponse2 = UserEventParticipationResponse.builder()
                .withId(2)
                .withFio("Petrov")
                .withStatus(Status.PENDING)
                .build();

        List<UserEventParticipationResponse> responses = List.of(uepResponse1, uepResponse2);

        when(uepService.findPendingUserEventParticipationsByEventId(userName, eventId)).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/participation/pending")
                        .param("eventId", "1")
                .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"fio\":\"Ivanov\",\"status\": \"PENDING\"}, {\"id\":2,\"fio\":\"Petrov\",\"status\": \"PENDING\"}]"));

        verify(uepService, times(1)).findPendingUserEventParticipationsByEventId(userName, eventId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void showAllRejectedUserEventParticipationsForManager_returnUserEventParticipationResponseList() throws Exception {

        int eventId = 1;
        String userName = "manager@example.com";

        UserEventParticipationResponse uepResponse1 = UserEventParticipationResponse.builder()
                .withId(1)
                .withFio("Ivanov")
                .withStatus(Status.REJECTED)
                .build();

        UserEventParticipationResponse uepResponse2 = UserEventParticipationResponse.builder()
                .withId(2)
                .withFio("Petrov")
                .withStatus(Status.REJECTED)
                .build();

        List<UserEventParticipationResponse> responses = List.of(uepResponse1, uepResponse2);

        when(uepService.findRejectedUserEventParticipationsByEventId(userName, eventId)).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/participation/rejected")
                        .param("eventId", "1")
                .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"fio\":\"Ivanov\",\"status\": \"REJECTED\"}, {\"id\":2,\"fio\":\"Petrov\",\"status\": \"REJECTED\"}]"));

        verify(uepService, times(1)).findRejectedUserEventParticipationsByEventId(userName, eventId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void showAllAcceptedUserEventParticipationsForManager_returnUserEventParticipationResponseList() throws Exception {

        int eventId = 1;
        String userName = "manager@example.com";

        UserEventParticipationResponse uepResponse1 = UserEventParticipationResponse.builder()
                .withId(1)
                .withFio("Ivanov")
                .withStatus(Status.ACCEPTED)
                .build();

        UserEventParticipationResponse uepResponse2 = UserEventParticipationResponse.builder()
                .withId(2)
                .withFio("Petrov")
                .withStatus(Status.ACCEPTED)
                .build();

        List<UserEventParticipationResponse> responses = List.of(uepResponse1, uepResponse2);

        when(uepService.findAcceptedUserEventParticipationsByEventId(userName, eventId)).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/participation/accepted")
                        .param("eventId", "1")
                .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"fio\":\"Ivanov\",\"status\": \"ACCEPTED\"}, {\"id\":2,\"fio\":\"Petrov\",\"status\": \"ACCEPTED\"}]"));

        verify(uepService, times(1)).findAcceptedUserEventParticipationsByEventId(userName, eventId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_PARTICIPANT")
    void showAllUserEventParticipationsForParticipant_returnUserEventParticipationResponseList() throws Exception {

        String userName = "manager@example.com";

        UserEventParticipationResponse uepResponse1 = UserEventParticipationResponse.builder()
                .withId(1)
                .withFio("Ivanov")
                .withStatus(Status.REJECTED)
                .build();

        UserEventParticipationResponse uepResponse2 = UserEventParticipationResponse.builder()
                .withId(2)
                .withFio("Petrov")
                .withStatus(Status.ACCEPTED)
                .build();

        List<UserEventParticipationResponse> responses = List.of(uepResponse1, uepResponse2);

        when(uepService.findAllUserEventParticipationsByUserEmail(userName)).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/participation/")
                .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"fio\":\"Ivanov\",\"status\": \"REJECTED\"}, {\"id\":2,\"fio\":\"Petrov\",\"status\": \"ACCEPTED\"}]"));

        verify(uepService, times(1)).findAllUserEventParticipationsByUserEmail(userName);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_PARTICIPANT")
    void createNewUserEventParticipation_returnUserEventParticipationResponse() throws Exception {

        String userName = "participant@example.com";

        UserEventParticipationRequest request = UserEventParticipationRequest.builder()
                .withId(1)
                .withUserId(1)
                .withEventId(1)
                .withStatus(Status.PENDING)
                .withFio("Ivanov Ivan Ivanovich")
                .withAge(22)
                .withCovidPassportNumber(11111111)
                .build();

        UserEventParticipationResponse response = UserEventParticipationResponse.builder()
                .withId(1)
                .withFio("Ivanov Ivan Ivanovich")
                .withStatus(Status.PENDING)
                .build();

        when(uepService.createUserEventParticipation(userName, request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/event/participation/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"userId\": 1, \"eventId\": 1, \"status\": \"PENDING\", \"fio\": \"Ivanov Ivan Ivanovich\", \"age\": 22, \"covidPassportNumber\": 11111111 }")
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"fio\": \"Ivanov Ivan Ivanovich\", \"status\": \"PENDING\"}"));

        verify(uepService, times(1)).createUserEventParticipation(userName, request);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void acceptParticipationRequest_returnsAcceptedResponse() throws Exception {

        String userName = "creator@example.com";
        int requestId = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/event/participation/accept/{id}", requestId)
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().string("Participation Request with id = " + requestId + " has been accepted"));

        verify(uepService, times(1)).setUserEventParticipationsStatusAccepted(userName, requestId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void rejectParticipationRequest_returnsRejectedResponse() throws Exception {

        String userName = "creator@example.com";
        int requestId = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/event/participation/reject/{id}", requestId)
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().string("Participation Request with id = " + requestId + " has been rejected"));

        verify(uepService, times(1)).setUserEventParticipationsStatusRejected(userName, requestId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_PARTICIPANT")
    void deleteUserEventParticipation_returnsDeletedResponse() throws Exception {

        String userName = "participant@example.com";
        int requestId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/event/participation/participant")
                        .param("id", String.valueOf(requestId))
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().string("Participation request with id = " + requestId + " has been deleted"));

        verify(uepService, times(1)).deleteUserEventParticipationsById(userName, requestId);
        verifyNoMoreInteractions(uepService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void deleteUserEventParticipationAdmin_returnsDeletedResponse() throws Exception {

        int requestId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/event/participation/admin")
                        .param("id", String.valueOf(requestId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Participation request with id = " + requestId + " has been deleted by Admin"));

        verify(uepService, times(1)).deleteUserEventParticipationsByIdForAdmin(requestId);
        verifyNoMoreInteractions(uepService);
    }

}
