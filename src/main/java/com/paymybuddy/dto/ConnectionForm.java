package com.paymybuddy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectionForm {

    @NotNull
    private Integer receiverUserId;
}