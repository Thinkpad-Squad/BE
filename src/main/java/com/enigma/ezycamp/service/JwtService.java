package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.response.JwtClaims;
import com.enigma.ezycamp.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    Boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
