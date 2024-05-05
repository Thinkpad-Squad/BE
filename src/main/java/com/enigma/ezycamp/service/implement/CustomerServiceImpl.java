package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.repository.CustomerRepository;
import com.enigma.ezycamp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getCustomerById(String id) {
        return customerRepository.findByIdCustomer(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllCustomer(SearchRequest request) {
        return null;
    }

    @Override
    public Customer updateCustomer(UpdateCustomerRequest customer) {
        return null;
    }

    @Override
    public Customer disableById(String id) {
        return null;
    }
}
