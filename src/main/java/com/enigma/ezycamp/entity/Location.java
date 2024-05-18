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
@Table(name = "m_location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "recommended_activity")
    private String recommendedActivity;
    @Column(name = "safety_tips")
    private String safetyTips;
    @Column(name = "nearest_store_address")
    private String nearestStoreAddress;
    @OneToMany(mappedBy = "location")
    @JsonManagedReference
    private List<LocationImage> images;
}
