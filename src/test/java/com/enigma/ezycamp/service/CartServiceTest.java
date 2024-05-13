package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.repository.CartRepository;
import com.enigma.ezycamp.service.implement.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private EquipmentService equipmentService;
    private CartService cartService;
    @BeforeEach
    void setUp(){
        cartService = new CartServiceImpl(cartRepository, equipmentService);
    }

    @Test
    void addCart(){
        Customer customer = Customer.builder().build();
        ChangeCartRequest request = ChangeCartRequest.builder().equipmentId("1").quantity(1).build();
        Equipment equipment = Equipment.builder().id(request.getEquipmentId()).stock(10).build();
        when(equipmentService.getEquipmentById(anyString())).thenReturn(equipment);
        Cart cart = Cart.builder().customer(customer).equipment(equipment).quantity(request.getQuantity()).build();
        when(cartRepository.saveAndFlush(any(Cart.class))).thenReturn(cart);
        Cart result = cartService.addCart(customer, request);
        assertEquals(result.getQuantity(), request.getQuantity());
    }

    @Test
    void updateCart(){
        Cart cart = Cart.builder().equipment(Equipment.builder().stock(10).build()).build();
        Integer quantity = 1;
        when(cartRepository.saveAndFlush(any(Cart.class))).thenReturn(cart);
        Cart result = cartService.updateCart(cart, quantity);
        assertEquals(result.getQuantity(), quantity);
    }
}
