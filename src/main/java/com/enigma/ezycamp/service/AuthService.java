package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.LoginRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.RegisterRequest;
import com.enigma.ezycamp.dto.response.LoginResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(RegisterRequest request);
    RegisterResponse registerGuide(RegisterGuideRequest request);
    LoginResponse login(LoginRequest request);
    boolean validateToken();
}
