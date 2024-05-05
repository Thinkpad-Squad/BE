package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.entity.Location;

public interface LocationService {
    Location addLocation(NewLocationRequest request);
    Location getById(String id);
}
