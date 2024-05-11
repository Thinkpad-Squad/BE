package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.GuideImage;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface GuideImageService {
    GuideImage addImage(Guide guide, MultipartFile guideImage);
    Resource getByName(String name);
}
