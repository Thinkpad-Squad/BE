package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.repository.EquipmentRepository;
import com.enigma.ezycamp.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    @Override
    public Equipment getEquipmentById(String id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Peralatan tidak ditemukan"));
    }
}
