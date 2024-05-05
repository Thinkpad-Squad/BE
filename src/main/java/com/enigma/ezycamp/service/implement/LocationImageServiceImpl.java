package com.enigma.ezycamp.service.implement;

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
                    .url(imagePath.toString()).size(locationImage.getSize())
                    .originalName(locationImage.getOriginalFilename()).build();
            return locationImageRepository.saveAndFlush(imageSave);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
