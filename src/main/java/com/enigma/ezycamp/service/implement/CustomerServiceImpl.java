package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.repository.CustomerRepository;
import com.enigma.ezycamp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }
}
