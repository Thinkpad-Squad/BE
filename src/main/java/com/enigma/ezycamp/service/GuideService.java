package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.entity.Guide;
import org.springframework.data.domain.Page;

public interface GuideService {
    Guide getGuideById(String id);
    Page<Guide> getAllGuide(SearchRequest request);
    Guide updateGuide(UpdateGuideRequest request);
    void disableById(String id);
}
