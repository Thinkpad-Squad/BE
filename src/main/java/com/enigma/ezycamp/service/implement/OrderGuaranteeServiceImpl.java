package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.entity.OrderGuaranteeImage;
import com.enigma.ezycamp.repository.OrderGuaranteeImageRepository;
import com.enigma.ezycamp.service.OrderGuaranteeService;
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
public class OrderGuaranteeServiceImpl implements OrderGuaranteeService {
    private final OrderGuaranteeImageRepository guaranteeImageRepository;
    private final Path IMAGE_PATH;

    @Autowired
    public OrderGuaranteeServiceImpl(OrderGuaranteeImageRepository guaranteeImageRepository, @Value("${ezycamp.multipart.path-location.order-guarantee-image}") String imagePath) {
        this.guaranteeImageRepository = guaranteeImageRepository;
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
    public OrderGuaranteeImage addGuarantee(MultipartFile image) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(image.getContentType())) throw new ConstraintViolationException("Format file gambar tidak valid", null);
            String name = System.currentTimeMillis()+"_"+image.getOriginalFilename();
            Path imagePath = IMAGE_PATH.resolve(name);
            Files.copy(image.getInputStream(), imagePath);
            OrderGuaranteeImage imageSave = OrderGuaranteeImage.builder().name(name).path(imagePath.toString())
                    .originalName(image.getOriginalFilename()).size(image.getSize())
                    .url("/api/orders/guaranteeImages/"+name).build();
            return guaranteeImageRepository.saveAndFlush(imageSave);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menyimpan gambar");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Resource getByName(String name) {
        try {
            OrderGuaranteeImage image = guaranteeImageRepository.findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar jaminan tidak ditemukan"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar jaminan tidak ditemukan");
            return new UrlResource(filePath.toUri());
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
