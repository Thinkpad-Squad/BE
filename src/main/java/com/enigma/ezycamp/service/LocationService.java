package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateByGuideRequest;
import com.enigma.ezycamp.dto.request.UpdateLocationRequest;
import com.enigma.ezycamp.entity.Location;
import org.springframework.data.domain.Page;

public interface LocationService {
    Location addLocation(NewLocationRequest request);
    Location getById(String id);
    Page<Location> getAll(SearchRequest request);
    Location updateLocation(UpdateLocationRequest request);
    Location updateByGuide(UpdateByGuideRequest request);
}
