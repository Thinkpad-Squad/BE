package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Customer;

public interface CustomerService {
    void addCustomer(Customer customer);
    Customer getCustomerById(String id);
//    Page<Customer> getAllCustomer(SearchUserRequest request);
//    Customer updateCustomer(UpdateCustomerRequest customer);
//    Customer disableById(String id);
}
