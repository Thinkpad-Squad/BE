package com.enigma.ezycamp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @OneToMany(mappedBy = "guide")
    @JsonManagedReference
    private List<GuideImage> images;
    @Column(name = "price")
    private Long price;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;
}
