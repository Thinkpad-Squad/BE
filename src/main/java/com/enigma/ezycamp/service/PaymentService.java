package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Order;
import com.enigma.ezycamp.entity.Payment;

public interface PaymentService {
    Payment addPayment(Order order);
    Payment cashPayment(Order order);
    Payment findById(String id);
    void checkFailedAndUpdateStatus();
}
