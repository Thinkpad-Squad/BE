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
@Table(name = "m_review_guide")
public class ReviewGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "rate")
    private Float rate;
    @Column(name = "comment")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id")
    @JsonBackReference
    private Guide guide;
}
