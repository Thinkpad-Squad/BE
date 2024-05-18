package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.LoginRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.RegisterRequest;
import com.enigma.ezycamp.dto.response.LoginResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @MockBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void registerCustomer() throws Exception{
        RegisterRequest request = RegisterRequest.builder().name("name").phone("0888").password("password").username("username").build();
        RegisterResponse registerResponse = RegisterResponse.builder().username("username").roles(List.of("CUSTOMER")).build();
        when(authService.registerCustomer(any(RegisterRequest.class))).thenReturn(registerResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<RegisterResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<RegisterResponse>>() {});
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Sukses mendaftar customer", response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void registerGuide() throws Exception{
        String jsonGuide = "{\"name\": \"name\", \"phone\": \"phone\", \"price\": 0, \"location\": \"location\", \"username\": \"username\", \"password\": \"password\" }";
        MockMultipartFile file = new MockMultipartFile(
                "images",
                "filename1.jpg",
                "image/jpg",
                "file1 content".getBytes()
        );
        MockMultipartFile metadata =
                new MockMultipartFile(
                        "guide",
                        "guide",
                        "text/plain", jsonGuide.getBytes());
        RegisterResponse registerResponse = RegisterResponse.builder().username("username").roles(List.of("GUIDE")).build();
        when(authService.registerGuide(any(RegisterGuideRequest.class))).thenReturn(registerResponse);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/auth/registerGuide")
                        .file(file)
                        .file(metadata).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void login() throws Exception{
        LoginRequest request = LoginRequest.builder().username("user").password("password").build();
        LoginResponse loginResponse = LoginResponse.builder().roles(List.of("ADMIN")).username("user").token("token").build();
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringRequest))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<LoginResponse>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Berhasil login", response.getMessage());
                } );
    }
}
