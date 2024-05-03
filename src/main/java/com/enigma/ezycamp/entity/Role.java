package com.enigma.ezycamp.entity;

import com.enigma.ezycamp.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
