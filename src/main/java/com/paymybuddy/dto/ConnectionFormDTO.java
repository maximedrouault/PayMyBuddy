package com.paymybuddy.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ConnectionFormDTO {

    @Min(1)
    private Integer receiverUserId;
}