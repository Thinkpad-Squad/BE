package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateEquipmentRequest;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.service.EquipmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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
    void findEquipmentById() throws Exception {
        String id = "1";
        Equipment equipment = Equipment.builder().id(id).build();
        when(equipmentService.getEquipmentById(anyString())).thenReturn(equipment);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipments/"+id).content(id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<Equipment> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Equipment>>() {});
                    assertEquals(200, response.getStatusCode());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findAllEquipment() throws Exception {
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<Equipment> equipments = List.of(Equipment.builder().build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<Equipment> equipmentPage = new PageImpl<>(equipments,page, equipments.size());
        when(equipmentService.getAllEquipment(any(SearchRequest.class))).thenReturn(equipmentPage);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipments").content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<List<Equipment>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<Equipment>>>() {});
                    assertEquals(200, response.getStatusCode());
                });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateEquipment() throws Exception{
        String jsonEq = "{\"name\": \"name\", \"description\": \"description\", \"price\": 1, \"stock\": 1 }";
        MockMultipartFile file = new MockMultipartFile("images", "image.jpg", "image/jpg", "image".getBytes());
        MockMultipartFile equipment = new MockMultipartFile("equipment", "equipment", "text/plain", jsonEq.getBytes());
        Equipment equipmentNew = Equipment.builder().name("name").price(1L).build();
        when(equipmentService.addEquipment(any(NewEquipmentRequest.class))).thenReturn(equipmentNew);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,"/api/equipments").file(file).file(equipment).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {WebResponse<Equipment> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Equipment>>() {});
            assertEquals(200, response.getStatusCode());
        });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void disableEquipment() throws  Exception{
        String id = "1";
        doNothing().when(equipmentService).disableEquipmentById(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/equipments/"+id).content(id)).andExpect(status().isOk()).andDo(result -> {WebResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse>() {});
            assertEquals(200, response.getStatusCode());
        });
    }
}
