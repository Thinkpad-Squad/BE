package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.LocationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationImageRepository extends JpaRepository<LocationImage, String> {
}
