package com.enigma.ezycamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageIdCardRepository extends JpaRepository<ImageIdCard, String> {
}
