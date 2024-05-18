package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomerById(String id);
    Customer getCustomerByUsername(String username);
    Page<Customer> getAllCustomer(SearchRequest request);
    Customer updateCustomer(UpdateCustomerRequest customer);
    void disableById(String id);
    List<Cart> updateCart(String customerId, ChangeCartRequest request);
    List<Cart> getAllCart(String customerId);
}
