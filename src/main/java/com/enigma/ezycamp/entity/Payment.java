package com.enigma.ezycamp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "t_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "status")
    private String status;
    @Column(name = "redirect_url")
    private String url;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
