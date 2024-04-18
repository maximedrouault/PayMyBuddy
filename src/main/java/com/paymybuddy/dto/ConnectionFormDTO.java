package com.paymybuddy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for ConnectionForm. Used to transfer connection form data within the application.
 * It is used to add a connection between two users.
 */
@Data
public class ConnectionFormDTO {

    @Min(1)
    @NotNull
    private Integer receiverUserId;
}