package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateByGuideRequest;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.service.LocationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LocationControllerTest {
    @MockBean
    private LocationService locationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addLocation() throws Exception{
        String json = "{\n" +
                "  \"name\": \"name_43e6fa127e39\",\n" +
                "  \"description\": \"description_cca13cf69317\",\n" +
                "  \"recommendedActivity\": \"recommendedActivity_dd775e508252\",\n" +
                "  \"safetyTips\": \"safetyTips_9ef4f1ee5b8c\",\n" +
                "  \"nearestStoreAddress\": \"nearestStoreAddress_79936abc2d1f\"" +
                "}";
        MockMultipartFile file = new MockMultipartFile("images", "image.jpg", "image/jpg", "image".getBytes());
        MockMultipartFile location = new MockMultipartFile("location", "location", "text/plain", json.getBytes());
        Location locationNew = Location.builder().name("name").build();
        when(locationService.addLocation(any(NewLocationRequest.class))).thenReturn(locationNew);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/locations").file(file).file(location).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findLocationById() throws Exception{
        String id = "1";
        Location location = Location.builder().id(id).build();
        when(locationService.getById(anyString())).thenReturn(location);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations/"+id).content(id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<Location> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Location>>() {});
                    assertEquals(200, response.getStatusCode());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findAllLocation() throws Exception{
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<Location> locations = List.of(Location.builder().build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<Location> locationPage = new PageImpl<>(locations,page, locations.size());
        when(locationService.getAll(any(SearchRequest.class))).thenReturn(locationPage);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations").content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<List<Location>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<Location>>>() {});
                    assertEquals(200, response.getStatusCode());
                });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateLocation() throws Exception{
        String json = "{\n" +
                "  \"id\": \"id_9117d1a5def4\",\n" +
                "  \"name\": \"name_e02e094aadfd\",\n" +
                "  \"description\": \"description_0d28067b48ce\",\n" +
                "  \"recommendedActivity\": \"recommendedActivity_92663ed4a976\",\n" +
                "  \"safetyTips\": \"safetyTips_fc5f44286e15\",\n" +
                "  \"nearestStoreAddress\": \"nearestStoreAddress_9cbc9ed64fbe\"" +
                "}";
        MockMultipartFile file = new MockMultipartFile("images", "image.jpg", "image/jpg", "image".getBytes());
        MockMultipartFile location = new MockMultipartFile("location", "location", "text/plain", json.getBytes());
        Location locationNew = Location.builder().name("name").build();
        when(locationService.addLocation(any(NewLocationRequest.class))).thenReturn(locationNew);
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,"/api/locations").file(file).file(location).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(result -> {WebResponse<Location> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Location>>() {});
                    assertEquals(200, response.getStatusCode());
                });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateByGuide() throws Exception{
        UpdateByGuideRequest request = UpdateByGuideRequest.builder().id("id").safetyTips("safe").recommendationActivity("activity").build();
        Location location = Location.builder().id(request.getId()).recommendedActivity(request.getRecommendationActivity()).safetyTips(request.getSafetyTips()).build();
        when(locationService.updateByGuide(any(UpdateByGuideRequest.class))).thenReturn(location);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/locations/guide").contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<Location> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Location>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(request.getSafetyTips(), response.getData().getSafetyTips());
                });
    }
}
