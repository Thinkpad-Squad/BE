package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.OrderEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEquipmentRepository extends JpaRepository<OrderEquipment, String> {
}
