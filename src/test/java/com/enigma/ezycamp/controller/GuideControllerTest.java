package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.security.AuthenticatedUser;
import com.enigma.ezycamp.service.GuideService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GuideControllerTest {
    @MockBean
    private GuideService guideService;
    @MockBean
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void findGuideById() throws Exception {
        String id = "1";
        Guide guide = Guide.builder().id(id).build();
        when(guideService.getGuideById(anyString())).thenReturn(guide);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/guides/"+id).content(id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<Guide> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Guide>>() {});
                    assertEquals(200, response.getStatusCode());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findAllGuide() throws Exception {
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<Guide> guides = List.of(Guide.builder().build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<Guide> guidePage = new PageImpl<>(guides,page, guides.size());
        when(guideService.getAllGuide(any(SearchRequest.class))).thenReturn(guidePage);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/guides").content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<List<Guide>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<Guide>>>() {});
                    assertEquals(200, response.getStatusCode());
                });
    }

    @WithMockUser(roles = "GUIDE")
    @Test
    void updateGuide() throws Exception {
        when(authenticatedUser.hasGuideId(anyString())).thenReturn(true);
        UpdateGuideRequest request = UpdateGuideRequest.builder().id("1").name("name").phone("0888").price(1L).username("username").build();
        Guide guide = Guide.builder().id(request.getId()).name(request.getName()).build();
        when(guideService.updateGuide(any(UpdateGuideRequest.class))).thenReturn(guide);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/guides").contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<Guide> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Guide>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(request.getName(), response.getData().getName());
                });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void disableGuideById() throws Exception {
        String id = "1";
        doNothing().when(guideService).disableById(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/guides/"+id).content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<Guide> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Guide>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(response.getMessage(), "Berhasil menghapus data pemandu");
                });
    }
}
