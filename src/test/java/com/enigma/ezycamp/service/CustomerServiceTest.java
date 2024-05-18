package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.CustomerRepository;
import com.enigma.ezycamp.service.implement.CustomerServiceImpl;
import com.enigma.ezycamp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private CartService cartService;
    @Mock
    private CustomerService customerService;
    @BeforeEach
    void setUp(){
        customerService = new CustomerServiceImpl(customerRepository,validationUtil,cartService);
    }

    @Test
    void addCustomer(){
        Customer customer = Customer.builder().id("tes id").name("cahyo").phone("088888").build();
        when(customerRepository.saveAndFlush(Mockito.any(Customer.class))).thenReturn(customer);
        Customer result = customerRepository.saveAndFlush(customer);
        assertEquals(result.getName(), customer.getName());
    }

    @Test
    void getCustomerById(){
        String id = "1";
        Customer customer = Customer.builder().id(id).build();
        when(customerRepository.findByIdCustomer(anyString())).thenReturn(Optional.of(customer));
        Customer result = customerService.getCustomerById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    void getCustomerByUsername(){
        String username = "username";
        Customer customer = Customer.builder().userAccount(UserAccount.builder().username(username).build()).build();
        when(customerRepository.findByUsernameCustomer(anyString())).thenReturn(Optional.of(customer));
        Customer result = customerService.getCustomerByUsername(username);
        assertEquals(result.getUserAccount().getUsername(), username);
    }

    @Test
    void getAllCustomer(){
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        Page<Customer> customers =new PageImpl<>(Collections.emptyList());
        when(customerRepository.findAllCustomer(any(Pageable.class))).thenReturn(customers);
        Page<Customer> result = customerService.getAllCustomer(request);
        assertEquals(customers.getTotalPages(), result.getTotalPages());
    }

    @Test
    void updateCustomer(){
        UpdateCustomerRequest request = UpdateCustomerRequest.builder().id("1").username("username").phone("0888").name("name").build();
        Customer customer = Customer.builder().id(request.getId()).name(request.getName()).phone(request.getPhone()).userAccount(UserAccount.builder().username(request.getUsername()).build()).build();
        when(customerRepository.findByIdCustomer(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);
        Customer result = customerService.updateCustomer(request);
        assertEquals(result.getName(), request.getName());
    }

    @Test
    void disableById(){
        String id = "1";
        Customer customer = Customer.builder().id(id).userAccount(UserAccount.builder().isEnable(false).build()).carts(Collections.emptyList()).build();
        when(customerRepository.findByIdCustomer(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);
        customerService.disableById(id);
        verify(customerRepository, times(1)).saveAndFlush(customer);
    }

    @Test
    void updateCart(){
        String customerId = "1";
        ChangeCartRequest request = ChangeCartRequest.builder().equipmentId("1").quantity(1).build();
        Customer customer = Customer.builder().id(customerId).build();
        when(customerRepository.findByIdCustomer(anyString())).thenReturn(Optional.of(customer));
        Cart cart = Cart.builder().id("1").quantity(request.getQuantity()).customer(customer).equipment(Equipment.builder().id(request.getEquipmentId()).build()).build();
        when(cartService.addCart(any(Customer.class), any(ChangeCartRequest.class))).thenReturn(cart);
        List<Cart> result = customerService.updateCart(customerId, request);
        assertEquals(result.get(0).getQuantity(), request.getQuantity());
    }
}
