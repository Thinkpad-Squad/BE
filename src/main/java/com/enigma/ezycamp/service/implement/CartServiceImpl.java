package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.repository.CartRepository;
import com.enigma.ezycamp.service.CartService;
import com.enigma.ezycamp.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final EquipmentService equipmentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Cart addCart(Customer customer, ChangeCartRequest request) {
        Equipment equipment = equipmentService.getEquipmentById(request.getEquipmentId());
        Cart cart = Cart.builder().customer(customer).quantity(request.getQuantity()).equipment(equipment).build();
        return cartRepository.saveAndFlush(cart);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Cart updateCart(Cart cart, Integer quantity) {
        cart.setQuantity(quantity);
        return cartRepository.saveAndFlush(cart);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }
}
