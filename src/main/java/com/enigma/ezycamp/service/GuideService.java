package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchUserRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.entity.Guide;
import org.springframework.data.domain.Page;

public interface GuideService {
    void addGuide(Guide guide);
    Guide getGuideById(String id);
    Page<Guide> getAllGuide(SearchUserRequest request);
    Guide updateGuide(UpdateGuideRequest request);
}
