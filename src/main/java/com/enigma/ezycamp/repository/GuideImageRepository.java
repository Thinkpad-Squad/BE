package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.GuideImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideImageRepository extends JpaRepository<GuideImage, String> {
}
