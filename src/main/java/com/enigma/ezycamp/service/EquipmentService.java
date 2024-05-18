package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateEquipmentRequest;
import com.enigma.ezycamp.entity.Equipment;
import org.springframework.data.domain.Page;

public interface EquipmentService {
    Equipment addEquipment(NewEquipmentRequest request);
    Equipment getEquipmentById(String id);
    Page<Equipment> getAllEquipment(SearchRequest request);
    Equipment updateEquipment(UpdateEquipmentRequest request);
    void disableEquipmentById(String id);
    void changeStock(String id, Integer stock);
}
