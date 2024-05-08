package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.OrderGuaranteeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderGuaranteeImageRepository extends JpaRepository<OrderGuaranteeImage,String > {
}
