package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.ContractRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.ContractService;
import com.mikhail.tarasevich.eventmanager.service.exception.ContractNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.ContractMapper;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    private final UserRepository userRepository;

    private final ContractMapper mapper;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
                               UserRepository userRepository,
                               ContractMapper mapper) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> findPendingContracts() {

        return contractRepository.findContractByStatusOrderById(Status.PENDING).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> findRejectedContracts() {

        return contractRepository.findContractByStatusOrderById(Status.REJECTED).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> findAcceptedContracts() {

        return contractRepository.findContractByStatusOrderById(Status.ACCEPTED).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContractResponse createContract(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        return mapper.toResponse(contractRepository.save(Contract.builder()
                .withId(0)
                .withUser(user)
                .withStatus(Status.PENDING)
                .build()));
    }

    @Override
    public void setContractStatusRejected(int id) {

        Contract contract = contractRepository.findById(id).orElseThrow(() ->
                new ContractNotFoundException("There is no contract with id = " + id + " in DB"));

        contract.setStatus(Status.REJECTED);

        contractRepository.save(contract);
    }

    @Override
    public void setContractStatusAccepted(int id) {

        Contract contract = contractRepository.findById(id).orElseThrow(() ->
                new ContractNotFoundException("There is no contract with id = " + id + " in DB"));

        contract.setStatus(Status.ACCEPTED);

        contractRepository.save(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserAcceptedContract(String email) {

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        return !contractRepository.findContractByUserIdAndStatusOrderById(user.getId(), Status.ACCEPTED).isEmpty();
    }

    @Override
    public void deleteContractById(int id) {

        contractRepository.deleteById(id);
    }

}
