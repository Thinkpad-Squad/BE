package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.UserAccountRepository;
import com.enigma.ezycamp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByUserId(String id) {
        return userAccountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userAccountRepository.findByUsername(auth.getPrincipal().toString()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));
    }
}
