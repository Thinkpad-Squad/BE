package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.GuideImage;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.LocationImageRepository;
import com.enigma.ezycamp.service.LocationImageService;
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
public class LocationImageServiceImpl implements LocationImageService {
    private final LocationImageRepository locationImageRepository;
    private final Path directoryPath;
    @Autowired
    public LocationImageServiceImpl(LocationImageRepository locationImageRepository, @Value("${ezycamp.multipart.path-location.location-image}") String directoryPath) {
        this.locationImageRepository = locationImageRepository;
        this.directoryPath = Paths.get(directoryPath);
    }

    @PostConstruct
    public void initDirectory(){
        if (!Files.exists(directoryPath)){
            try {
                Files.createDirectory(directoryPath);
            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LocationImage addImageLocation(Location location, MultipartFile locationImage) {
        try {
            if(!List.of("image/jpeg", "image/jpg", "image/png").contains(locationImage.getContentType()))
                throw new ConstraintViolationException("File terupload harus bertipe gambar",null);
            String imageName = System.currentTimeMillis()+"_"+locationImage.getOriginalFilename();
            Path imagePath = directoryPath.resolve(imageName);
            Files.copy(locationImage.getInputStream(),imagePath);
            LocationImage imageSave = LocationImage.builder().location(location).name(imageName)
                    .path(imagePath.toString()).size(locationImage.getSize())
                    .originalName(locationImage.getOriginalFilename())
                    .url("/api/locations/images/"+imageName).build();
            return locationImageRepository.saveAndFlush(imageSave);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Resource getByName(String name) {
        try {
            LocationImage image = locationImageRepository.findByName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar lokasi tidak ditemukan"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar lokasi tidak ditemukan");
            return new UrlResource(filePath.toUri());
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(LocationImage locationImage) {
        try {
            locationImageRepository.findById(locationImage.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar lokasi tidak ditemukan"));
            Path filePath = Paths.get(locationImage.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar lokasi tidak ditemukan");
            Files.delete(filePath);
            locationImageRepository.delete(locationImage);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
