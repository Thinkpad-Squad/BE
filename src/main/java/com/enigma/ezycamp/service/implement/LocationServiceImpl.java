package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateByGuideRequest;
import com.enigma.ezycamp.dto.request.UpdateLocationRequest;
import com.enigma.ezycamp.entity.EquipmentImage;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import com.enigma.ezycamp.repository.LocationRepository;
import com.enigma.ezycamp.service.LocationImageService;
import com.enigma.ezycamp.service.LocationService;
import com.enigma.ezycamp.util.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .recommendedActivity(request.getRecommendedActivity()).safetyTips(request.getSafetyTips())
                .nearestStoreAddress(request.getNearestStoreAddress()).build();
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
        return locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lokasi tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Location> getAll(SearchRequest request) {
        if(request.getPage()<1) request.setPage(1);
        if(request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getParam()==null) return locationRepository.findAll(pageable);
        else return locationRepository.findByNameLocation("%"+request.getParam()+"%", pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Location updateLocation(UpdateLocationRequest request) {
        validationUtil.validate(request);
        Location location = getById(request.getId());
        location.setName(request.getName());
        location.setDescription(request.getDescription());
        if(request.getSafetyTips()!=null) location.setSafetyTips(request.getSafetyTips());
        if (request.getRecommendedActivity()!=null) location.setRecommendedActivity(request.getRecommendedActivity());
        location.setNearestStoreAddress(request.getNearestStoreAddress());
        if (request.getImages()!=null){
            List<LocationImage> imageOlds = location.getImages();
            for (LocationImage imageOld:imageOlds){
                locationImageService.delete(imageOld);
            }
            List<LocationImage> imageList = new ArrayList<>();
            for (MultipartFile image: request.getImages()){
                LocationImage imageNew = locationImageService.addImageLocation(location,image);
                imageList.add(imageNew);
            }
            location.setImages(imageList);
        }
        return locationRepository.saveAndFlush(location);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Location updateByGuide(UpdateByGuideRequest request) {
        validationUtil.validate(request);
        Location location = getById(request.getId());
        location.setRecommendedActivity(request.getRecommendationActivity());
        location.setSafetyTips(request.getSafetyTips());
        return locationRepository.saveAndFlush(location);
    }
}
