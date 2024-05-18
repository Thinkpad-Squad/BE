package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.EquipmentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentImageRepository extends JpaRepository<EquipmentImage, String> {
    @Query(value = "select * from m_equipment_image where name = :name", nativeQuery = true)
    Optional<EquipmentImage> findByName(@Param("name") String name);
}
