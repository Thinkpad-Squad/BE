package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.entity.Role;
import com.enigma.ezycamp.repository.RoleRepository;
import com.enigma.ezycamp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole userRole) {
        return roleRepository.findByRole(userRole).orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(userRole).build()));
    }
}
