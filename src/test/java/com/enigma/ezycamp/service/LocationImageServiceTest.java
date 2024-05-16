package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.LocationImageRepository;
import com.enigma.ezycamp.repository.LocationImageRepository;
import com.enigma.ezycamp.service.implement.LocationImageServiceImpl;
import com.enigma.ezycamp.service.implement.LocationImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LocationImageServiceTest {
    @Mock
    private LocationImageRepository imageRepository;
    @Mock
    private Path IMAGE_PATH;
    private LocationImageService locationImageService;
    @BeforeEach
    void setUp(){
        locationImageService = new LocationImageServiceImpl(imageRepository, "/Users/Lenovo/OneDrive/Gambar/EzyCamp/LocationImage");
    }

    @Test
    void addImage(){
        Location location = Location.builder().build();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_image.jpg";
        when(IMAGE_PATH.resolve(Mockito.anyString())).thenReturn(Path.of("/Users/Lenovo/OneDrive/Gambar/EzyCamp/LocationImage/timeNow_image.jpg"));
        Path imagePath = IMAGE_PATH.resolve(imageName);
        LocationImage imageSave = LocationImage.builder().name(imageName).path(imagePath.toString())
                .originalName(image.getOriginalFilename()).size(image.getSize())
                .location(location).url("/api/locations/images/"+imageName).build();
        when(imageRepository.saveAndFlush(Mockito.any(LocationImage.class))).thenReturn(imageSave);
        locationImageService.addImageLocation(location, image);
        verify(imageRepository, times(1)).saveAndFlush(any(LocationImage.class));
    }

    @Test
    void getByName(){
        String name = "1714976722233_download.jpg";
        LocationImage image = LocationImage.builder().path("/Users/Lenovo/OneDrive/Gambar/EzyCamp/LocationImage/"+name).build();
        Resource expectedResource = null;
        try {
            expectedResource = new UrlResource(Paths.get(image.getPath()).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        when(imageRepository.findByName(anyString())).thenReturn(Optional.of(image));
        Resource result = locationImageService.getByName(name);
        assertEquals(result.getFilename(), expectedResource.getFilename());
    }
}
