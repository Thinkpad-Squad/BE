package com.enigma.ezycamp.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image addImage(MultipartFile image);
}
