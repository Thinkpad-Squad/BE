package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateEquipmentRequest;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.EquipmentRepository;
import com.enigma.ezycamp.service.EquipmentImageService;
import com.enigma.ezycamp.service.EquipmentService;
import com.enigma.ezycamp.util.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final ValidationUtil validationUtil;
    private final EquipmentImageService equipmentImageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Equipment addEquipment(NewEquipmentRequest request) {
        validationUtil.validate(request);
        if (request.getImages().isEmpty()) throw new ConstraintViolationException("Gambar peralatan tidak boleh kosong", null);
        Equipment equipment = Equipment.builder().price(request.getPrice()).stock(request.getStock())
                .name(request.getName()).description(request.getDescription()).isEnable(true).build();
        List<EquipmentImage> images = new ArrayList<>();
        for (MultipartFile image:request.getImages()){
            EquipmentImage imageAdded = equipmentImageService.addImage(equipment, image);
            images.add(imageAdded);
        }
        equipment.setImages(images);
        return equipment;
    }

    @Transactional(readOnly = true)
    @Override
    public Equipment getEquipmentById(String id) {
        return equipmentRepository.findEquipmentById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Peralatan tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Equipment> getAllEquipment(SearchRequest request) {
        if(request.getPage()<1) request.setPage(1);
        if(request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getParam() == null) return equipmentRepository.findAllEquipment(pageable);
        else return equipmentRepository.findEquipmentByName("%"+request.getParam()+"%", pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Equipment updateEquipment(UpdateEquipmentRequest request) {
        validationUtil.validate(request);
        Equipment equipment = getEquipmentById(request.getId());
        equipment.setName(request.getName());
        equipment.setDescription(request.getDescription());
        equipment.setPrice(request.getPrice());
        equipment.setStock(request.getStock());
        if (request.getImages()!=null){
            List<EquipmentImage> imageOlds = equipment.getImages();
            for (EquipmentImage imageOld:imageOlds){
                equipmentImageService.delete(imageOld);
            }
            List<EquipmentImage> imageList = new ArrayList<>();
            for (MultipartFile image: request.getImages()){
                EquipmentImage imageNew = equipmentImageService.addImage(equipment,image);
                imageList.add(imageNew);
            }
            equipment.setImages(imageList);
        }
        return equipmentRepository.saveAndFlush(equipment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableEquipmentById(String id) {
        Equipment equipment = getEquipmentById(id);
        equipment.setIsEnable(false);
        equipmentRepository.saveAndFlush(equipment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStock(String id, Integer stock) {
        Equipment equipment = getEquipmentById(id);
        equipment.setStock(stock);
        equipmentRepository.saveAndFlush(equipment);
    }
}
