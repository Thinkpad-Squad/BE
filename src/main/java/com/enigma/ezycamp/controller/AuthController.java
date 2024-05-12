package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.LoginRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.RegisterRequest;
import com.enigma.ezycamp.dto.response.LoginResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<RegisterResponse>> registerCustomer(@RequestBody RegisterRequest request){
        WebResponse<RegisterResponse> register = WebResponse.<RegisterResponse>builder().statusCode(HttpStatus.CREATED.value())
                .message("Sukses mendaftar customer").data(authService.registerCustomer(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/registerGuide", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<RegisterResponse>> registerGuide(@RequestPart(name = "guide") String jsonGuide,
                                                                       @RequestPart(name = "images")List<MultipartFile> images){
        log.info("Received request with jsonGuide: {}", jsonGuide);
        log.info("Received request with images: {}", images);
        WebResponse<RegisterResponse> response;
        try {
            RegisterGuideRequest request = objectMapper.readValue(jsonGuide, new TypeReference<RegisterGuideRequest>() {});
            request.setImages(images);
            RegisterResponse guide = authService.registerGuide(request);
            response = WebResponse.<RegisterResponse>builder()
                    .statusCode(HttpStatus.CREATED.value()).message("Sukses mendaftar pemandu")
                    .data(guide).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            response = WebResponse.<RegisterResponse>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal mendaftar pemandu").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<?>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        WebResponse<LoginResponse> response = WebResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil login")
                .data(loginResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateToken() {
        boolean valid = this.authService.validateToken();
        WebResponse response;
        if (valid) {
            response = WebResponse.builder().statusCode(HttpStatus.OK.value()).message("Berhasil memvalidasi token").build();
            return ResponseEntity.ok(response);
        } else {
            response = WebResponse.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).message("Token JWT invalid").build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
