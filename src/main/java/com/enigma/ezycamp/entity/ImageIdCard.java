package com.enigma.ezycamp.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "m_image_id_card")
public class ImageIdCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "size", nullable = false, columnDefinition = "BIGINT CHECK (size>=0)")
    private Long size;
    @Column(name = "content_type", nullable = false)
    private String contentType;
}
