package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.LocationRepository;
import com.enigma.ezycamp.service.LocationImageService;
import com.enigma.ezycamp.service.LocationService;
import com.enigma.ezycamp.util.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final ValidationUtil validationUtil;
    private final LocationImageService locationImageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Location addLocation(NewLocationRequest request) {
        validationUtil.validate(request);
        if (request.getImages().isEmpty()) throw new ConstraintViolationException("Gambar lokasi tidak boleh kosong", null);
        Location location = Location.builder().name(request.getName()).description(request.getDescription())
                .recommendedActivity(request.getRecommendedActivity()).safetyTips(request.getSafetyTips()).build();
        List<LocationImage> images = new ArrayList<>();
        for (MultipartFile image:request.getImages()){
            LocationImage imageAdded = locationImageService.addImageLocation(location, image);
            images.add(imageAdded);
        }
        location.setImages(images);
        return location;
    }

    @Transactional(readOnly = true)
    @Override
    public Location getById(String id) {
        return locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lokasi wisata tidak ditemukan"));
    }
}
