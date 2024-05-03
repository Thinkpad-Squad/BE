package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.ImageIdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageIdCardRepository extends JpaRepository<ImageIdCard, String> {
}
