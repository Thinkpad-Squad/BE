package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.LocationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationImageRepository extends JpaRepository<LocationImage, String> {
    @Query(value = "select * from m_location_image where name = :name", nativeQuery = true)
    Optional<LocationImage> findByName(@Param("name") String name);
}
