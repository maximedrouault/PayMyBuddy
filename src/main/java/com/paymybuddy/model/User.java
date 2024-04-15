package com.paymybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(length = 100, nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String password;

    @Column(length = 5, nullable = false)
    @NotBlank
    private String role;

    @Column(length = 50, nullable = false)
    @NotBlank
    private String name;

    @Column(length = 100, nullable = false)
    @NotBlank
    private String bankAccountNumber;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false, orphanRemoval = true)
    @JoinColumn(name = "wallet_id")
    @NotNull
    private Wallet wallet;
}