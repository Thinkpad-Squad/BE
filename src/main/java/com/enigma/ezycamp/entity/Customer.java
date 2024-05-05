package com.enigma.ezycamp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<Cart> carts;
}
