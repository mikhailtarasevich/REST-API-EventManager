package com.mikhail.tarasevich.eventmanager.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {

    private String message;

    private LocalDateTime timestamp;

}
