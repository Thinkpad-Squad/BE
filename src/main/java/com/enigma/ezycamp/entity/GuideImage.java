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
@Table(name = "m_guide_image")
public class GuideImage {
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
    @JoinColumn(name = "guide_id", referencedColumnName = "id")
    @JsonBackReference
    private Guide guide;
}
