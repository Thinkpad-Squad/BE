package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.EquipmentImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EquipmentImageService {
    EquipmentImage addImage(Equipment equipment, MultipartFile image);
    void delete(EquipmentImage image);
}
