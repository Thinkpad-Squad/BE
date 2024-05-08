package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.OrderEquipmentRequest;
import com.enigma.ezycamp.entity.Order;
import com.enigma.ezycamp.entity.OrderEquipment;

public interface OrderEquipmentService {
    OrderEquipment addOrderEq(Order order, OrderEquipmentRequest request);
}
