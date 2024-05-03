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
    @JoinColumn(name = "image_id")
    private Image imageId;
    @OneToOne
    @JoinColumn(name = "image_card_id")
    private ImageIdCard imageIdCard;
    @Column(name = "location")
    private String location;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
