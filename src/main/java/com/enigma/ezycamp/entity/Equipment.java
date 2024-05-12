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
@Table(name = "m_equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Long price;
    @Column(name = "stock")
    private Integer stock;
    @OneToMany(mappedBy = "equipment")
    @JsonManagedReference
    private List<EquipmentImage> images;
    @Column(name = "is_enable")
    private Boolean isEnable;
}
