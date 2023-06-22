package com.mikhail.tarasevich.eventmanager.dto;

import com.mikhail.tarasevich.eventmanager.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractRequest {

    private int id;

    @NotEmpty(message = "User ID should not be empty")
    private int userId;

    private Status status;

}
