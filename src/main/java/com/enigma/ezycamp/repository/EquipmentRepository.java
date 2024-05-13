package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String > {
    @Query(value = "select * from m_equipment where id = :id and is_enable = true", nativeQuery = true)
    Optional<Equipment> findEquipmentById(@Param("id") String id);
    @Query(value = "select * from m_equipment where is_enable = true", nativeQuery = true)
    Page<Equipment> findAllEquipment(Pageable pageable);
    @Query(value = "select * from m_equipment where name ilike :name and is_enable = true", nativeQuery = true)
    Page<Equipment> findEquipmentByName(@Param("name") String name, Pageable pageable);
}
