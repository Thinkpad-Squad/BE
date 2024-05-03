package com.enigma.ezycamp.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "customer_name")
    private String name;
    @Column(name = "customer_phone")
    private String phone;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
