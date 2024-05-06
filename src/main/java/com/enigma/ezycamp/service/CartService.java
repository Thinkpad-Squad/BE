package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;

public interface CartService {
    Cart addCart(Customer customer, ChangeCartRequest request);
    Cart updateCart(Cart cart, Integer quantity);
    void deleteCart(Cart cart);
}
