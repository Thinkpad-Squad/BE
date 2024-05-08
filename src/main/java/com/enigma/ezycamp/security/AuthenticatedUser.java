package com.enigma.ezycamp.security;

import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.service.CustomerService;
import com.enigma.ezycamp.service.GuideService;
import com.enigma.ezycamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final GuideService guideService;
    private final CustomerService customerService;
    private final UserService userService;

    public boolean hasGuideId(String id) {
        Guide guide = guideService.getGuideById(id);
        UserAccount guideAccount = (UserAccount) userService.loadUserByUsername(guide.getUserAccount().getUsername());
        UserAccount account = userService.getByContext();
        if (!account.getId().equals(guideAccount.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, akses ditolak");
        return true;
    }

    public boolean hasCustomerId(String id) {
        Customer customer = customerService.getCustomerById(id);
        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customer.getUserAccount().getUsername());
        UserAccount account = userService.getByContext();
        if (!account.getId().equals(customerAccount.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, akses ditolak");
        return true;
    }

    public boolean hasUsername(String username) {
        Customer customer = customerService.getCustomerByUsername(username);
        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customer.getUserAccount().getUsername());
        UserAccount account = userService.getByContext();
        if (!account.getUsername().equals(customerAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden, akses ditolak");
        return true;
    }
}
