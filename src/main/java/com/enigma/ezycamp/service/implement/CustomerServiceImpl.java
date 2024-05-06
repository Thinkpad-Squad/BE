package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateCartRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.CustomerRepository;
import com.enigma.ezycamp.service.CustomerService;
import com.enigma.ezycamp.service.EquipmentService;
import com.enigma.ezycamp.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
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
    private final EquipmentService equipmentService;
    private final ValidationUtil validationUtil;

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
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer updateCart(String customerId, UpdateCartRequest request) {
        validationUtil.validate(request);
        Customer customer = getCustomerById(customerId);
        Equipment equipment = equipmentService.getEquipmentById(request.getEquipmentId());
        if(customer.getCarts() == null || customer.getCarts().isEmpty()){
            List<Cart> carts = List.of(Cart.builder().customer(customer).quantity(request.getQuantity())
                    .equipment(equipment).build());
            customer.setCarts(carts);
            return customerRepository.saveAndFlush(customer);
        } else {
            List<Cart> carts = customer.getCarts();
            for (int i = 0; i<customer.getCarts().size(); i++){
                if(customer.getCarts().get(i).getEquipment().equals(equipment)){
                    Cart cart = carts.get(i);
                    if(request.getQuantity() == 0) carts.remove(cart);
                    else {
                        cart.setQuantity(request.getQuantity());
                        carts.set(i, cart);
                    }
                    customer.setCarts(carts);
                    return customerRepository.saveAndFlush(customer);
                }
            }
            if(request.getQuantity() == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keranjang tidak bisa dibuat dengan kuantitas 0");
            carts.add(Cart.builder().customer(customer).equipment(equipment)
                    .quantity(request.getQuantity()).build());
            customer.setCarts(carts);
            return customerRepository.saveAndFlush(customer);
        }
    }
}
