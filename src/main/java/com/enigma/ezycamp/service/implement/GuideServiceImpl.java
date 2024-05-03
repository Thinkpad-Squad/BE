package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.repository.GuideRepository;
import com.enigma.ezycamp.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {
    private final GuideRepository guideRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Guide addGuide(Guide guide) {
        return guideRepository.saveAndFlush(guide);
    }
}
