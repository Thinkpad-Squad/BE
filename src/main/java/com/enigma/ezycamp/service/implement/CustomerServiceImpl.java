package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.CustomerRepository;
import com.enigma.ezycamp.service.CartService;
import com.enigma.ezycamp.service.CustomerService;
import com.enigma.ezycamp.service.EquipmentService;
import com.enigma.ezycamp.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final CartService cartService;

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
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsernameCustomer(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllCustomer(SearchRequest request) {
        if(request.getPage()<1) request.setPage(1);
        if(request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getParam()==null) return customerRepository.findAllCustomer(pageable);
        else return customerRepository.findByNameCustomer("%"+request.getParam()+"%", pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer updateCustomer(UpdateCustomerRequest request) {
        validationUtil.validate(request);
        Customer customer = getCustomerById(request.getId());
        UserAccount account = customer.getUserAccount();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        account.setUsername(request.getUsername());
        customer.setUserAccount(account);
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableById(String id) {
        Customer customer = getCustomerById(id);
        UserAccount account = customer.getUserAccount();
        account.setIsEnable(false);
        customer.setUserAccount(account);
        customerRepository.saveAndFlush(customer);
        if(customer.getCarts() != null || !customer.getCarts().isEmpty()){
            for (int i = 0; i < customer.getCarts().size(); i++) {
                cartService.deleteCart(customer.getCarts().get(i));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Cart> updateCart(String customerId, ChangeCartRequest request) {
        validationUtil.validate(request);
        Customer customer = getCustomerById(customerId);
        if(customer.getCarts() == null || customer.getCarts().isEmpty()){
            Cart cart = cartService.addCart(customer, request);
            return List.of(cart);
        } else {
            List<Cart> carts = customer.getCarts();
            for (int i = 0; i<customer.getCarts().size(); i++){
                if(carts.get(i).getEquipment().getId().equals(request.getEquipmentId())){
                    Cart cart = carts.get(i);
                    if(request.getQuantity() == 0) {
                        carts.remove(cart);
                        cartService.deleteCart(cart);
                    }
                    else {
                        Cart cartUpdate = cartService.updateCart(cart, request.getQuantity());
                        carts.set(i, cartUpdate);
                    }
                    return carts;
                }
            }
            if(request.getQuantity() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keranjang tidak bisa dibuat dengan kuantitas 0");
            Cart cart = cartService.addCart(customer, request);
            carts.add(cart);
            return carts;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cart> getAllCart(String customerId) {
        return cartService.getCart(customerId);
    }
}
