package com.mikhail.tarasevich.eventmanager.service.mapper;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;

public interface UserEventParticipationMapper {

    UserEventParticipationResponse toResponse (UserEventParticipation entity);

    UserEventParticipation toEntity (UserEventParticipationRequest request);

}
