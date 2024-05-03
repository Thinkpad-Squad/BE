package com.enigma.ezycamp.service;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole userRole);
}
