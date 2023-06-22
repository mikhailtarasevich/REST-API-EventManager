package com.mikhail.tarasevich.eventmanager.service.mapper.impl;

import com.mikhail.tarasevich.eventmanager.dto.ContractRequest;
import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.service.mapper.ContractMapper;
import org.springframework.stereotype.Component;

@Component
public class ContractMapperImpl implements ContractMapper {

    @Override
    public ContractResponse toResponse(Contract entity) {

        return ContractResponse.builder()
                .withId(entity.getId())
                .withUser(UserResponse.builder()
                        .withId(entity.getUser().getId())
                        .withEmail(entity.getUser().getEmail())
                        .build())
                .withStatus(entity.getStatus())
                .build();
    }

    @Override
    public Contract toEntity(ContractRequest request) {

        return Contract.builder()
                .withId(request.getId())
                .withUser(User.builder().withId(request.getUserId()).build())
                .withStatus(request.getStatus())
                .build();
    }

}
