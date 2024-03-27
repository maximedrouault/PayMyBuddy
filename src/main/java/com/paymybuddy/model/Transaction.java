package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Check(constraints = "sender_user_id != receiver_user_id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int transactionId;

    @Column(nullable = false)
    @NotNull
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "time")
    @NotNull
    private LocalTime time;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.01")
    private double transactionAmount;

    @Column(nullable = false)
    @Min(0)
    private double commissionAmount;


    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    @NotNull
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    @NotNull
    private User receiver;
}