package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.service.EquipmentService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipmentControllerTest {
    @MockBean
    private EquipmentService equipmentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addEquipment() throws Exception{
        String jsonEq = "{\"name\": \"name\", \"description\": \"description\", \"price\": 1, \"stock\": 1 }";
        MockMultipartFile file = new MockMultipartFile("images", "image.jpg", "image/jpg", "image".getBytes());
        MockMultipartFile equipment = new MockMultipartFile("equipment", "equipment", "text/plain", jsonEq.getBytes());
        Equipment equipmentNew = Equipment.builder().name("name").price(1L).build();
        when(equipmentService.addEquipment(any(NewEquipmentRequest.class))).thenReturn(equipmentNew);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/equipments").file(file).file(equipment).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
}
