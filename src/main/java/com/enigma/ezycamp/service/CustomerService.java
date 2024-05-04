package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchCustomerRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomerById(String id);
//    Page<Customer> getAllCustomer(SearchCustomerRequest request);
//    Customer updateCustomer(UpdateCustomerRequest customer);
//    Customer disableById(String id);
}
