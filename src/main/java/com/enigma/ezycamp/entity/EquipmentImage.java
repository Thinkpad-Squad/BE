package com.enigma.ezycamp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_equipment_image")
public class EquipmentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "path")
    private String path;
    @Column(name = "url")
    private String url;
    @Column(name = "original_name")
    private String originalName;
    @Column(name = "size")
    private Long size;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    @JsonBackReference
    private Equipment equipment;
}
