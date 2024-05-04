package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.repository.GuideRepository;
import com.enigma.ezycamp.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {
    private final GuideRepository guideRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addGuide(Guide guide) {
        guideRepository.saveAndFlush(guide);
    }

    @Transactional(readOnly = true)
    @Override
    public Guide getGuideById(String id) {
        return guideRepository.findByIdGuide(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pemandu wisata tidak ditemukan"));
    }
}
