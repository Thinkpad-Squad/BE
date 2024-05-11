package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewOrderRequest;
import com.enigma.ezycamp.dto.request.SearchOrderRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateStatusRequest;
import com.enigma.ezycamp.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Order addOrder(NewOrderRequest request);
    Page<Order> findAllOrder(SearchOrderRequest request);
    Page<Order> findByCustomerId(SearchRequest request);
    Page<Order> findByGuideId(SearchRequest request);
    void updateStatus(UpdateStatusRequest request);
}