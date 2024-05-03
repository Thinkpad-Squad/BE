package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, String> {
}
