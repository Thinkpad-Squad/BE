package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = "select * from m_cart where customer_id = :id", nativeQuery = true)
    List<Cart> getCartByCustomerId(@Param("id") String id);
}
