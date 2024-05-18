package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateByGuideRequest;
import com.enigma.ezycamp.dto.request.UpdateLocationRequest;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.LocationRepository;
import com.enigma.ezycamp.service.implement.LocationServiceImpl;
import com.enigma.ezycamp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private LocationImageService locationImageService;
    private LocationService locationService;
    @BeforeEach
    void setUp(){
        locationService = new LocationServiceImpl(locationRepository,validationUtil, locationImageService);
    }

    @Test
    void addLocation(){
        MockMultipartFile image = new MockMultipartFile("image", "image", "image/jpg", "image".getBytes());
        NewLocationRequest request = NewLocationRequest.builder().name("name").description("desc").safetyTips("safe").recommendedActivity("activity").nearestStoreAddress("address").images(List.of(image)).build();
        LocationImage locationImage = LocationImage.builder().size(request.getImages().get(0).getSize()).build();
        when(locationImageService.addImageLocation(any(Location.class), any(MultipartFile.class))).thenReturn(locationImage);
        Location location = locationService.addLocation(request);
        assertEquals(location.getImages().get(0).getSize(), image.getSize());
    }

    @Test
    void getById(){
        String id = "id";
        Location location = Location.builder().id(id).build();
        when(locationRepository.findById(anyString())).thenReturn(Optional.of(location));
        Location result = locationService.getById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    void getAll(){
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        Page<Location> location = new PageImpl<>(Collections.emptyList());
        when(locationRepository.findAll(any(Pageable.class))).thenReturn(location);
        Page<Location> result = locationService.getAll(request);
        assertEquals(result.getTotalPages(), location.getTotalPages());
    }

    @Test
    void updateLocation(){
        UpdateLocationRequest request = UpdateLocationRequest.builder().id("1").description("desc").name("name").nearestStoreAddress("address").build();
        Location location = Location.builder().id(request.getId()).name(request.getName()).description(request.getDescription()).nearestStoreAddress(request.getNearestStoreAddress()).build();
        when(locationRepository.findById(anyString())).thenReturn(Optional.of(location));
        when(locationRepository.saveAndFlush(any(Location.class))).thenReturn(location);
        Location result = locationService.updateLocation(request);
        assertEquals(result.getName(), request.getName());
        assertEquals(result.getDescription(), request.getDescription());
    }

    @Test
    void updateByGuide(){
        UpdateByGuideRequest request = UpdateByGuideRequest.builder().id("1").recommendationActivity("activity").safetyTips("tips").build();
        Location location = Location.builder().id(request.getId()).recommendedActivity(request.getRecommendationActivity()).safetyTips(request.getSafetyTips()).build();
        when(locationRepository.findById(anyString())).thenReturn(Optional.of(location));
        when(locationRepository.saveAndFlush(any(Location.class))).thenReturn(location);
        Location result = locationService.updateByGuide(request);
        assertEquals(request.getSafetyTips(), result.getSafetyTips());
    }
}
