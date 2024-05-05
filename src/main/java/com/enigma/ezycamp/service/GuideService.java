package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchUserRequest;
import com.enigma.ezycamp.entity.Guide;
import org.springframework.data.domain.Page;

public interface GuideService {
    Guide getGuideById(String id);
    Page<Guide> getAllGuide(SearchUserRequest request);
}
