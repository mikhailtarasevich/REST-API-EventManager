package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.service.ContractService;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class ContractControllerTest {

    @InjectMocks
    private ContractController contractController;

    @Mock
    private ContractService contractService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(contractController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showPendingContracts_returnsListOfPendingContracts() throws Exception {

        ContractResponse contract1 = ContractResponse.builder()
                .withId(1)
                .withStatus(Status.PENDING)
                .build();
        ContractResponse contract2 = ContractResponse.builder()
                .withId(2)
                .withStatus(Status.PENDING)
                .build();
        List<ContractResponse> pendingContracts = List.of(contract1, contract2);

        when(contractService.findPendingContracts()).thenReturn(pendingContracts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contract/pending"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"status\":\"PENDING\"},{\"id\":2,\"status\":\"PENDING\"}]"));

        verify(contractService, times(1)).findPendingContracts();
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showRejectedContracts_returnsListOfRejectedContracts() throws Exception {

        ContractResponse contract1 = ContractResponse.builder()
                .withId(1)
                .withStatus(Status.REJECTED)
                .build();
        ContractResponse contract2 = ContractResponse.builder()
                .withId(2)
                .withStatus(Status.REJECTED)
                .build();
        List<ContractResponse> rejectedContracts = List.of(contract1, contract2);

        when(contractService.findRejectedContracts()).thenReturn(rejectedContracts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contract/rejected"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"status\":\"REJECTED\"},{\"id\":2,\"status\":\"REJECTED\"}]"));

        verify(contractService, times(1)).findRejectedContracts();
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void showAcceptedContracts_returnsListOfAcceptedContracts() throws Exception {

        ContractResponse contract1 = ContractResponse.builder()
                .withId(1)
                .withStatus(Status.ACCEPTED)
                .build();
        ContractResponse contract2 = ContractResponse.builder()
                .withId(2)
                .withStatus(Status.ACCEPTED)
                .build();
        List<ContractResponse> acceptedContracts = List.of(contract1, contract2);

        when(contractService.findAcceptedContracts()).thenReturn(acceptedContracts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contract/accepted"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"status\":\"ACCEPTED\"},{\"id\":2,\"status\":\"ACCEPTED\"}]"));

        verify(contractService, times(1)).findAcceptedContracts();
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void createNewContract_returnsCreatedContract() throws Exception {

        String userName = "testUser";

        ContractResponse contractResponse = ContractResponse.builder()
                .withId(1)
                .withStatus(Status.PENDING)
                .build();

        when(contractService.createContract(userName)).thenReturn(contractResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/contract/")
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"status\":\"PENDING\"}"));

        verify(contractService, times(1)).createContract(userName);
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void hasMangerAcceptedContract_returnsAcceptedMessage() throws Exception {

        String userName = "testUser";

        when(contractService.hasUserAcceptedContract(userName)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contract/manager/valid")
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().string("Manger with email = " + userName + " has accepted contract"));

        verify(contractService, times(1)).hasUserAcceptedContract(userName);
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_EVENT_CREATOR")
    void hasMangerAcceptedContract_returnsNotAcceptedMessage() throws Exception {

        String userName = "testUser";

        when(contractService.hasUserAcceptedContract(userName)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/contract/manager/valid")
                        .principal(new UsernamePasswordAuthenticationToken(userName, "")))
                .andExpect(status().isOk())
                .andExpect(content().string("Manger with email = " + userName + " has no accepted contract"));

        verify(contractService, times(1)).hasUserAcceptedContract(userName);
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void acceptContract_returnsAcceptedMessage() throws Exception {

        int contractId = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/contract/accept/{id}", contractId))
                .andExpect(status().isOk())
                .andExpect(content().string("Contract with id = " + contractId + " has been accepted by Administrator"));

        verify(contractService, times(1)).setContractStatusAccepted(contractId);
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void rejectContract_returnsRejectedMessage() throws Exception {

        int contractId = 1;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/contract/reject/{id}", contractId))
                .andExpect(status().isOk())
                .andExpect(content().string("Contract with id = " + contractId + " has been rejected by Administrator"));

        verify(contractService, times(1)).setContractStatusRejected(contractId);
        verifyNoMoreInteractions(contractService);
    }

    @Test
    @WithMockUser(authorities = "PRIVILEGE_APP_ADMIN")
    void deleteContract_returnsDeletedMessage() throws Exception {

        int contractId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/contract/{id}", contractId))
                .andExpect(status().isOk())
                .andExpect(content().string("Contract with id = " + contractId + " has been deleted by Administrator"));

        verify(contractService, times(1)).deleteContractById(contractId);
        verifyNoMoreInteractions(contractService);
    }

}
