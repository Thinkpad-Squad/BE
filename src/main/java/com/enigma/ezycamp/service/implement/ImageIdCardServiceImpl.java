package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.repository.ImageIdCardRepository;
import com.enigma.ezycamp.service.ImageIdCardService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageIdCardServiceImpl implements ImageIdCardService {
    private final ImageIdCardRepository imageRepository;
    private final Path IMAGE_PATH;

    @Autowired
    public ImageIdCardServiceImpl(ImageIdCardRepository imageRepository, @Value("ezycamp.multipart.path-location.card-image") String imagePath) {
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

    @Override
    public ImageIdCard addImage(MultipartFile imageCard) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(imageCard.getContentType())) throw new ConstraintViolationException("Format file gambar tidak valid", null);
            String name = System.currentTimeMillis()+"_"+imageCard.getOriginalFilename();
            Path imagePath = IMAGE_PATH.resolve(name);
            Files.copy(imageCard.getInputStream(),imagePath);
            ImageIdCard imageSave = ImageIdCard.builder().name(name).path(imagePath.toString())
                    .size(imageCard.getSize()).contentType(imageCard.getContentType()).build();
            return imageRepository.saveAndFlush(imageSave);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menyimpan gambar");
        }
    }
}
