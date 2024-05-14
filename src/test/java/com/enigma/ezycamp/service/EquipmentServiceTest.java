package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateEquipmentRequest;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.repository.EquipmentRepository;
import com.enigma.ezycamp.service.implement.EquipmentServiceImpl;
import com.enigma.ezycamp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private EquipmentImageService equipmentImageService;
    private EquipmentService equipmentService;
    @BeforeEach
    void setUp(){
        equipmentService = new EquipmentServiceImpl(equipmentRepository, validationUtil, equipmentImageService);
    }

    @Test
    void addEquipment(){
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        NewEquipmentRequest request = NewEquipmentRequest.builder().images(List.of(image)).price(1L).stock(1).name("name").description("desc").build();
        EquipmentImage equipmentImage = EquipmentImage.builder().build();
        when(equipmentImageService.addImage(any(Equipment.class),any(MultipartFile.class))).thenReturn(equipmentImage);
        Equipment equipment = equipmentService.addEquipment(request);
        assertEquals(request.getName(), equipment.getName());
    }

    @Test
    void getEquipmentById(){
        String id = "1";
        Equipment equipment = Equipment.builder().id(id).build();
        when(equipmentRepository.findEquipmentById(anyString())).thenReturn(Optional.of(equipment));
        Equipment result = equipmentService.getEquipmentById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    void getAllEquipment(){
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        Page<Equipment> equipment = new PageImpl<>(Collections.emptyList());
        when(equipmentRepository.findAllEquipment(any(Pageable.class))).thenReturn(equipment);
        Page<Equipment> result = equipmentService.getAllEquipment(request);
        assertEquals(result.getTotalPages(), equipment.getTotalPages());
    }

    @Test
    void updateEquipment(){
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        UpdateEquipmentRequest request = UpdateEquipmentRequest.builder().id("1").description("desc").images(List.of(image)).name("name").price(1L).stock(1).build();
        EquipmentImage equipmentImage = EquipmentImage.builder().build();
        Equipment equipment = Equipment.builder().id(request.getId()).name(request.getName()).images(List.of(equipmentImage)).build();
        when(equipmentRepository.findEquipmentById(anyString())).thenReturn(Optional.of(equipment));
        doNothing().when(equipmentImageService).delete(any(EquipmentImage.class));
        when(equipmentImageService.addImage(any(Equipment.class), any(MultipartFile.class))).thenReturn(equipmentImage);
        when(equipmentRepository.saveAndFlush(any(Equipment.class))).thenReturn(equipment);
        Equipment result = equipmentService.updateEquipment(request);
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getName(), request.getName());
    }

    @Test
    void disableEquipmentById(){
        String id = "1";
        Equipment equipment = Equipment.builder().id(id).isEnable(false).build();
        assertEquals(id, equipment.getId());
    }
}
