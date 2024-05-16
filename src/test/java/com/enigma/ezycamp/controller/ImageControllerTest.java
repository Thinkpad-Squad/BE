package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.service.EquipmentImageService;
import com.enigma.ezycamp.service.GuideImageService;
import com.enigma.ezycamp.service.LocationImageService;
import com.enigma.ezycamp.service.OrderGuaranteeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {
    @MockBean
    private EquipmentImageService equipmentImageService;
    @MockBean
    private GuideImageService guideImageService;
    @MockBean
    private LocationImageService locationImageService;
    @MockBean
    private OrderGuaranteeService orderGuaranteeService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void downloadEquipmentImage() throws Exception {
        String name = "1715876695407_image.jpg";
        Resource expectedResource = new UrlResource(Paths.get("/Users/Lenovo/OneDrive/Gambar/EzyCamp/EquipmentImage/"+name).toUri());
        when(equipmentImageService.getByName(anyString())).thenReturn(expectedResource);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipments/images/"+name).content(name)).andExpect(status().isOk());
    }

    @Test
    void downloadGuideImage() throws Exception {
        String name = "1715656231104_image.jpg";
        Resource expectedResource = new UrlResource(Paths.get("/Users/Lenovo/OneDrive/Gambar/EzyCamp/GuideImage/"+name).toUri());
        when(guideImageService.getByName(anyString())).thenReturn(expectedResource);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/guides/images/"+name).content(name)).andExpect(status().isOk());
    }

    @Test
    void downloadLocationImage() throws Exception {
        String name = "1714976722233_download.jpg";
        Resource expectedResource = new UrlResource(Paths.get("/Users/Lenovo/OneDrive/Gambar/EzyCamp/LocationImage/"+name).toUri());
        when(locationImageService.getByName(anyString())).thenReturn(expectedResource);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations/images/"+name).content(name)).andExpect(status().isOk());
    }

    @Test
    void downloadGuaranteeImage() throws Exception {
        String name = "1715829995660_nyelam1.jpg";
        Resource expectedResource = new UrlResource(Paths.get("/Users/Lenovo/OneDrive/Gambar/EzyCamp/OrderGuaranteeImage/"+name).toUri());
        when(orderGuaranteeService.getByName(anyString())).thenReturn(expectedResource);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/guaranteeImages/"+name).content(name)).andExpect(status().isOk());
    }
}
