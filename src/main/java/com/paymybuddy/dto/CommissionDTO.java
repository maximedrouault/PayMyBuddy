package com.paymybuddy.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for Commission.
 * This class is used to transfer data related to commissions.
 * It is used to encapsulate commission data and send it in JSON format.
 */
@Data
@Builder
public class CommissionDTO {

    private int transactionId;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal commissionAmount;
}