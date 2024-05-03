package com.enigma.ezycamp.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_guide")
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "guide_name")
    private String name;
    @Column(name = "guide_phone")
    private String phone;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
