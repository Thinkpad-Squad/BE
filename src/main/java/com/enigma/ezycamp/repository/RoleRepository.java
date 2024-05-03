package com.enigma.ezycamp.repository;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole userRole);
}
