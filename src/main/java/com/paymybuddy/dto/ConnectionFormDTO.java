package com.paymybuddy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectionFormDTO {

    @NotNull
    @Min(1)
    private Integer receiverUserId;
}