package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.entity.Guide;

public interface GuideService {
    Guide addGuide(Guide guide);
}
