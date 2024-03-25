package com.paymybuddy.model;

import jakarta.persistence.*;
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
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int connectionId;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiver;
}