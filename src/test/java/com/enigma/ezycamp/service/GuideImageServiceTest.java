package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.*;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.GuideImage;
import com.enigma.ezycamp.repository.GuideImageRepository;
import com.enigma.ezycamp.service.implement.GuideImageServiceImpl;
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
public class GuideImageServiceTest {
    @Mock
    private GuideImageRepository imageRepository;
    @Mock
    private Path IMAGE_PATH;
    private GuideImageService guideImageService;
    @BeforeEach
    void setUp(){
        guideImageService = new GuideImageServiceImpl(imageRepository, "/Users/Lenovo/OneDrive/Gambar/EzyCamp/GuideImage");
    }

    @Test
    void addImage(){
        Guide guide = Guide.builder().build();
        MultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "Test file content".getBytes());
        String imageName = "timeNow_image.jpg";
        when(IMAGE_PATH.resolve(Mockito.anyString())).thenReturn(Path.of("/Users/Lenovo/OneDrive/Gambar/EzyCamp/GuideImage/timeNow_image.jpg"));
        Path imagePath = IMAGE_PATH.resolve(imageName);
        GuideImage imageSave = GuideImage.builder().name(imageName).path(imagePath.toString())
                .originalName(image.getOriginalFilename()).size(image.getSize())
                .guide(guide).url("/api/guides/images/"+imageName).build();
        when(imageRepository.saveAndFlush(Mockito.any(GuideImage.class))).thenReturn(imageSave);
        guideImageService.addImage(guide, image);
        verify(imageRepository, times(1)).saveAndFlush(any(GuideImage.class));
    }

    @Test
    void getByName(){
        String name = "timeNow_image.jpg";
        GuideImage image = GuideImage.builder().path("/Users/Lenovo/OneDrive/Gambar/EzyCamp/GuideImage/1715656231104_image.jpg").build();
        Resource expectedResource = null;
        try {
            expectedResource = new UrlResource(Paths.get(image.getPath()).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        when(imageRepository.findByName(anyString())).thenReturn(Optional.of(image));
        Resource result = guideImageService.getByName(name);
        assertEquals(result.getFilename(), expectedResource.getFilename());
    }
}
