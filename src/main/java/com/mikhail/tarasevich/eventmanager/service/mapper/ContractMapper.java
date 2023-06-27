package com.mikhail.tarasevich.eventmanager.service.mapper;

import com.mikhail.tarasevich.eventmanager.dto.ContractRequest;
import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.entity.Contract;

public interface ContractMapper {

    ContractResponse toResponse (Contract entity);

    Contract toEntity (ContractRequest request);

}
