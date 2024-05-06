package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomerById(String id);
    Page<Customer> getAllCustomer(SearchRequest request);
    Customer updateCustomer(UpdateCustomerRequest customer);
    void disableById(String id);
    Customer updateCart(String customerId, ChangeCartRequest request);
}
