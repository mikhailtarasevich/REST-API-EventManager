package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.ContractRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.exception.ContractNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.ContractMapper;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContractMapper mapper;

    @Test
    void findPendingContracts_expectedContractResponseList() {

        Contract contract1 = Contract.builder().withId(1).build();
        Contract contract2 = Contract.builder().withId(2).build();

        List<Contract> contracts = List.of(contract1, contract2);

        when(contractRepository.findContractByStatusOrderById(Status.PENDING)).thenReturn(contracts);

        ContractResponse ContractResponse1 = ContractResponse.builder().withId(1).build();
        ContractResponse ContractResponse2 = ContractResponse.builder().withId(2).build();

        when(mapper.toResponse(contract1)).thenReturn(ContractResponse1);
        when(mapper.toResponse(contract2)).thenReturn(ContractResponse2);

        List<ContractResponse> contractResponses = List.of(ContractResponse1, ContractResponse2);

        List<ContractResponse> result = contractService.findPendingContracts();

        assertEquals(contractResponses.size(), result.size());
        assertEquals(ContractResponse1, result.get(0));
        assertEquals(ContractResponse2, result.get(1));

        verify(contractRepository, times(1)).findContractByStatusOrderById(Status.PENDING);
        verify(mapper, times(1)).toResponse(contract1);
        verify(mapper, times(1)).toResponse(contract2);
    }

    @Test
    void findRejectedContracts_expectedContractResponseList() {

        Contract contract1 = Contract.builder().withId(1).build();
        Contract contract2 = Contract.builder().withId(2).build();

        List<Contract> contracts = List.of(contract1, contract2);

        when(contractRepository.findContractByStatusOrderById(Status.REJECTED)).thenReturn(contracts);

        ContractResponse ContractResponse1 = ContractResponse.builder().withId(1).build();
        ContractResponse ContractResponse2 = ContractResponse.builder().withId(2).build();

        when(mapper.toResponse(contract1)).thenReturn(ContractResponse1);
        when(mapper.toResponse(contract2)).thenReturn(ContractResponse2);

        List<ContractResponse> contractResponses = List.of(ContractResponse1, ContractResponse2);

        List<ContractResponse> result = contractService.findRejectedContracts();

        assertEquals(contractResponses.size(), result.size());
        assertEquals(ContractResponse1, result.get(0));
        assertEquals(ContractResponse2, result.get(1));

        verify(contractRepository, times(1)).findContractByStatusOrderById(Status.REJECTED);
        verify(mapper, times(1)).toResponse(contract1);
        verify(mapper, times(1)).toResponse(contract2);
    }

    @Test
    void findAcceptedContracts_expectedContractResponseList() {

        Contract contract1 = Contract.builder().withId(1).build();
        Contract contract2 = Contract.builder().withId(2).build();

        List<Contract> contracts = List.of(contract1, contract2);

        when(contractRepository.findContractByStatusOrderById(Status.ACCEPTED)).thenReturn(contracts);

        ContractResponse ContractResponse1 = ContractResponse.builder().withId(1).build();
        ContractResponse ContractResponse2 = ContractResponse.builder().withId(2).build();

        when(mapper.toResponse(contract1)).thenReturn(ContractResponse1);
        when(mapper.toResponse(contract2)).thenReturn(ContractResponse2);

        List<ContractResponse> contractResponses = List.of(ContractResponse1, ContractResponse2);

        List<ContractResponse> result = contractService.findAcceptedContracts();

        assertEquals(contractResponses.size(), result.size());
        assertEquals(ContractResponse1, result.get(0));
        assertEquals(ContractResponse2, result.get(1));

        verify(contractRepository, times(1)).findContractByStatusOrderById(Status.ACCEPTED);
        verify(mapper, times(1)).toResponse(contract1);
        verify(mapper, times(1)).toResponse(contract2);
    }

    @Test
    void createContract_validEmail_returnsContractResponse() {

        String email = "test@example.com";
        User user = User.builder().withId(1).withEmail(email).build();
        Contract contract = Contract.builder().withId(0).withUser(user).withStatus(Status.PENDING).build();
        ContractResponse expectedResponse = ContractResponse.builder().withId(1).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(contractRepository.save(contract)).thenReturn(contract);
        when(mapper.toResponse(contract)).thenReturn(expectedResponse);

        ContractResponse result = contractService.createContract(email);

        assertEquals(expectedResponse, result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(contractRepository, times(1)).save(contract);
        verify(mapper, times(1)).toResponse(contract);
    }

    @Test
    void createContract_invalidEmail_throwsUserNotFoundException() {

        String email = "test@example.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> contractService.createContract(email));
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(contractRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void setContractStatusRejected_existingContract_updatesStatus() {

        int contractId = 1;
        Contract existingContract = Contract.builder().withId(contractId).withStatus(Status.PENDING).build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(existingContract));

        contractService.setContractStatusRejected(contractId);

        assertEquals(Status.REJECTED, existingContract.getStatus());
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, times(1)).save(existingContract);
    }

    @Test
    void setContractStatusRejected_nonExistingContract_throwsContractNotFoundException() {

        int contractId = 1;

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, () -> contractService.setContractStatusRejected(contractId));
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void setContractStatusAccepted_existingContract_updatesStatus() {

        int contractId = 1;
        Contract existingContract = Contract.builder().withId(contractId).withStatus(Status.PENDING).build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(existingContract));

        contractService.setContractStatusAccepted(contractId);

        assertEquals(Status.ACCEPTED, existingContract.getStatus());
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, times(1)).save(existingContract);
    }

    @Test
    void setContractStatusAccepted_nonExistingContract_throwsContractNotFoundException() {

        int contractId = 1;

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        assertThrows(ContractNotFoundException.class, () -> contractService.setContractStatusAccepted(contractId));
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, never()).save(any());
    }

    @Test
    void hasUserAcceptedContract_existingUserWithAcceptedContract_returnsTrue() {

        String email = "test@example.com";
        User existingUser = User.builder().withId(1).withEmail(email).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(existingUser));
        when(contractRepository.findContractByUserIdAndStatusOrderById(existingUser.getId(), Status.ACCEPTED))
                .thenReturn(List.of(new Contract()));

        boolean result = contractService.hasUserAcceptedContract(email);

        assertTrue(result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(contractRepository, times(1)).findContractByUserIdAndStatusOrderById(existingUser.getId(), Status.ACCEPTED);
    }

    @Test
    void hasUserAcceptedContract_existingUserWithoutAcceptedContract_returnsFalse() {

        String email = "test@example.com";
        User existingUser = User.builder().withId(1).withEmail(email).build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(existingUser));
        when(contractRepository.findContractByUserIdAndStatusOrderById(existingUser.getId(), Status.ACCEPTED))
                .thenReturn(Collections.emptyList());

        boolean result = contractService.hasUserAcceptedContract(email);

        assertFalse(result);
        verify(userRepository, times(1)).findUserByEmail(email);
        verify(contractRepository, times(1)).findContractByUserIdAndStatusOrderById(existingUser.getId(), Status.ACCEPTED);
    }

    @Test
    void hasUserAcceptedContract_nonExistingUser_throwsUserNotFoundException() {

        String email = "test@example.com";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> contractService.hasUserAcceptedContract(email));
    }

    @Test
    void deleteContractById_existingContract_deletesContract() {

        int contractId = 1;

        contractService.deleteContractById(contractId);

        verify(contractRepository, times(1)).deleteById(contractId);
    }

}
