package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewOrderRequest;
import com.enigma.ezycamp.entity.Order;

public interface OrderService {
    Order addOrder(NewOrderRequest request);
}
