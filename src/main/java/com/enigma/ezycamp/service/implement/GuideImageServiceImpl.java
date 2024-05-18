package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.GuideImage;
import com.enigma.ezycamp.repository.GuideImageRepository;
import com.enigma.ezycamp.service.GuideImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
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
public class GuideImageServiceImpl implements GuideImageService {
    private final GuideImageRepository imageRepository;
    private final Path IMAGE_PATH;

    @Autowired
    public GuideImageServiceImpl(GuideImageRepository imageRepository, @Value("${ezycamp.multipart.path-location.guide-image}") String imagePath) {
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
    public GuideImage addImage(Guide guide, MultipartFile guideImage) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(guideImage.getContentType())) throw new ConstraintViolationException("Format file gambar tidak valid", null);
            String name = System.currentTimeMillis()+"_"+guideImage.getOriginalFilename();
            Path imagePath = IMAGE_PATH.resolve(name);
            Files.copy(guideImage.getInputStream(),imagePath);
            GuideImage imageSave = GuideImage.builder().name(name).path(imagePath.toString())
                    .originalName(guideImage.getOriginalFilename()).size(guideImage.getSize())
                    .guide(guide).url("/api/guides/images/"+name).build();
            return imageRepository.saveAndFlush(imageSave);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menyimpan gambar");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Resource getByName(String name) {
        try {
            GuideImage image = imageRepository.findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar pemandu tidak ditemukan"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar pemandu tidak ditemukan");
            return new UrlResource(filePath.toUri());
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
