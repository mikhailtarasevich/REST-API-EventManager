package com.mikhail.tarasevich.eventmanager.service;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;

import java.util.List;

public interface UserEventParticipationService {

    List<UserEventParticipationResponse> findPendingUserEventParticipationsByEventId(String email, int eventId);

    List<UserEventParticipationResponse> findRejectedUserEventParticipationsByEventId(String email, int eventId);

    List<UserEventParticipationResponse> findAcceptedUserEventParticipationsByEventId(String email, int eventId);

    List<UserEventParticipationResponse> findAllUserEventParticipationsByUserEmail(String email);

    UserEventParticipationResponse createUserEventParticipation (String email, UserEventParticipationRequest request);

    void setUserEventParticipationsStatusRejected(String email, int id);

    void setUserEventParticipationsStatusAccepted(String email, int id);

    void deleteUserEventParticipationsById(String email, int id);

    void deleteUserEventParticipationsByIdForAdmin(int id);

}
