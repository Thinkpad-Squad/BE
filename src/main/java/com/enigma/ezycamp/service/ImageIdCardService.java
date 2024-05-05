package com.enigma.ezycamp.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageIdCardService {
    ImageIdCard addImage(MultipartFile imageCard);
}
