package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.ContractResponse;
import com.mikhail.tarasevich.eventmanager.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contract")
@Api(tags = "Контроллер управления контрактами с менджерами мероприятий")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    List<ContractResponse> showPendingContracts() {

        return contractService.findPendingContracts();
    }

    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    List<ContractResponse> showRejectedContracts() {

        return contractService.findRejectedContracts();
    }

    @GetMapping("/accepted")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    List<ContractResponse> showAcceptedContracts() {

        return contractService.findAcceptedContracts();
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    ContractResponse createNewContract(@ApiIgnore Principal user) {

        return contractService.createContract(user.getName());
    }

    @GetMapping("/manager/valid")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    ResponseEntity<String> hasMangerAcceptedContract(@ApiIgnore Principal user) {

        if (contractService.hasUserAcceptedContract(user.getName())) {
            return ResponseEntity.status(HttpStatus.OK).body("Manger with email = " + user.getName() + " has accepted contract");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Manger with email = " + user.getName() + " has no accepted contract");
        }
    }

    @PatchMapping("/accept/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    ResponseEntity<String> acceptContract(@ApiParam(value = "ID контракта в базе данных", example = "1", required = true) @PathVariable("id") int id) {

        contractService.setContractStatusAccepted(id);

        return ResponseEntity.status(HttpStatus.OK).body("Contract with id = " + id + " has been accepted by Administrator");
    }

    @PatchMapping("/reject/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    ResponseEntity<String> rejectContract(@ApiParam(value = "ID контракта в базе данных", example = "1", required = true) @PathVariable("id") int id) {

        contractService.setContractStatusRejected(id);

        return ResponseEntity.status(HttpStatus.OK).body("Contract with id = " + id + " has been rejected by Administrator");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    ResponseEntity<String> deleteContract(@ApiParam(value = "ID контракта в базе данных", example = "1", required = true) @PathVariable("id") int id) {

        contractService.deleteContractById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Contract with id = " + id + " has been deleted by Administrator");
    }

}
