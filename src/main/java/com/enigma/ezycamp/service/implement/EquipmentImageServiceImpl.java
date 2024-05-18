package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.GuideImage;
import com.enigma.ezycamp.repository.EquipmentImageRepository;
import com.enigma.ezycamp.repository.GuideImageRepository;
import com.enigma.ezycamp.service.EquipmentImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class EquipmentImageServiceImpl implements EquipmentImageService {
    private final EquipmentImageRepository imageRepository;
    private final Path IMAGE_PATH;

    @Autowired
    public EquipmentImageServiceImpl(EquipmentImageRepository imageRepository, @Value("${ezycamp.multipart.path-location.equipment-image}") String imagePath) {
        this.imageRepository = imageRepository;
        this.IMAGE_PATH = Paths.get(imagePath);
    }

    @PostConstruct
    private void createDir() {
        if(!Files.exists(IMAGE_PATH)){
            try {
                Files.createDirectory(IMAGE_PATH);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal membuat direktori gambar");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EquipmentImage addImage(Equipment equipment, MultipartFile image) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(image.getContentType())) throw new ConstraintViolationException("Format file gambar tidak valid", null);
            String name = System.currentTimeMillis()+"_"+image.getOriginalFilename();
            Path imagePath = IMAGE_PATH.resolve(name);
            Files.copy(image.getInputStream(), imagePath);
            EquipmentImage imageSave = EquipmentImage.builder().name(name).path(imagePath.toString())
                    .originalName(image.getOriginalFilename()).size(image.getSize())
                    .equipment(equipment).url("/api/equipments/images/"+name).build();
            return imageRepository.saveAndFlush(imageSave);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menyimpan gambar");
        }
    }


    @Transactional(readOnly = true)
    @Override
    public Resource getByName(String name) {
        try {
            EquipmentImage image = imageRepository.findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar peralatan tidak ditemukan"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar peralatan tidak ditemukan");
            return new UrlResource(filePath.toUri());
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(EquipmentImage image) {
        try {
            imageRepository.findById(image.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar peralatan tidak ditemukan"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar peralatan tidak ditemukan");
            Files.delete(filePath);
            imageRepository.delete(image);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
