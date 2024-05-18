package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.repository.EquipmentImageRepository;
import com.enigma.ezycamp.service.implement.EquipmentImageServiceImpl;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EquipmentImageServiceTest {
    @Mock
    private EquipmentImageRepository imageRepository;
    @Mock
    private Path IMAGE_PATH;
    private EquipmentImageService equipmentImageService;
    @BeforeEach
    void setUp(){
        equipmentImageService = new EquipmentImageServiceImpl(imageRepository, "/Users/Lenovo/OneDrive/Gambar/EzyCamp/EquipmentImage");
    }

    @Test
    void addImage(){
        Equipment equipment = Equipment.builder().build();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_image.jpg";
        when(IMAGE_PATH.resolve(Mockito.anyString())).thenReturn(Path.of("/Users/Lenovo/OneDrive/Gambar/EzyCamp/EquipmentImage/timeNow_image.jpg"));
        Path imagePath = IMAGE_PATH.resolve(imageName);
        EquipmentImage imageSave = EquipmentImage.builder().name(imageName).path(imagePath.toString())
                .originalName(image.getOriginalFilename()).size(image.getSize())
                .equipment(equipment).url("/api/equipments/images/"+imageName).build();
        when(imageRepository.saveAndFlush(Mockito.any(EquipmentImage.class))).thenReturn(imageSave);
        equipmentImageService.addImage(equipment, image);
        verify(imageRepository, times(1)).saveAndFlush(any(EquipmentImage.class));
    }

    @Test
    void getByName(){
        String name = "1715876646926_image.jpg";
        EquipmentImage image = EquipmentImage.builder().path("/Users/Lenovo/OneDrive/Gambar/EzyCamp/EquipmentImage/1715876646926_image.jpg").build();
        Resource expectedResource = null;
        try {
            expectedResource = new UrlResource(Paths.get(image.getPath()).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        when(imageRepository.findByName(anyString())).thenReturn(Optional.of(image));
        Resource result = equipmentImageService.getByName(name);
        assertEquals(result.getFilename(), expectedResource.getFilename());
    }

    @Test
    void delete(){
        EquipmentImage equipmentImage = EquipmentImage.builder().id("1").path("/Users/Lenovo/OneDrive/Gambar/EzyCamp/EquipmentImage/1715650917217_image.jpg").build();
        when(imageRepository.findById(anyString())).thenReturn(Optional.of(equipmentImage));
        doNothing().when(imageRepository).delete(any(EquipmentImage.class));
        equipmentImageService.delete(equipmentImage);
        verify(imageRepository,times(1)).delete(any(EquipmentImage.class));
    }
}
