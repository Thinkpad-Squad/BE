package com.enigma.ezycamp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String id;
    @Column(name = "status")
    private String status;
    @Column(name = "redirect_url")
    private String url;
    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    private Order order;
}
