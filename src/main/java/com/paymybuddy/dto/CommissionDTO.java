package com.paymybuddy.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class CommissionDTO {

    private int transactionId;
    private LocalDate date;
    private LocalTime time;
    private double commissionAmount;
}
