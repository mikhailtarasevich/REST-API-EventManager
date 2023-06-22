package com.mikhail.tarasevich.eventmanager.service;

import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;

import java.util.List;

public interface ContractService {

    List<ContractResponse> findPendingContracts();

    List<ContractResponse> findRejectedContracts();

    List<ContractResponse> findAcceptedContracts();

    ContractResponse createContract(String email);

    void setContractStatusRejected(int id);

    void setContractStatusAccepted(int id);

    boolean hasUserAcceptedContract(String email);

    void deleteContractById(int id);

}
