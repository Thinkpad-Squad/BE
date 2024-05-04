package com.enigma.ezycamp.service;

import com.enigma.ezycamp.entity.Guide;

public interface GuideService {
    void addGuide(Guide guide);
    Guide getGuideById(String id);
}
