package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image addImage(MultipartFile image);
}
