package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.LocationImage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface LocationImageService {
    LocationImage addImageLocation(Location location, MultipartFile locationImage);
    Resource getByName(String name);
    void delete(LocationImage locationImage);
}
