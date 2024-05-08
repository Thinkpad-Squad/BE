package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findAllByStatusIn(List<String> transactionStatus);
}
