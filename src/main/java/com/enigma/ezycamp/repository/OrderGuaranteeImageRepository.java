package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.OrderGuaranteeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderGuaranteeImageRepository extends JpaRepository<OrderGuaranteeImage,String > {
    @Query(value = "select * from t_order_guarantee_image where name = :name", nativeQuery = true)
    Optional<OrderGuaranteeImage> findByName(@Param("name") String name);
}
