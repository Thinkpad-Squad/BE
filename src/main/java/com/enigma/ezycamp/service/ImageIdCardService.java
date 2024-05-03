package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.ImageIdCard;
import org.springframework.web.multipart.MultipartFile;

public interface ImageIdCardService {
    ImageIdCard addImage(MultipartFile imageCard);
}
