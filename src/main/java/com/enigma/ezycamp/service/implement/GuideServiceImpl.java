package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.SearchUserRequest;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.repository.GuideRepository;
import com.enigma.ezycamp.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {
    private final GuideRepository guideRepository;


    @Transactional(readOnly = true)
    @Override
    public Guide getGuideById(String id) {
        return guideRepository.findByIdGuide(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pemandu wisata tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Guide> getAllGuide(SearchUserRequest request) {
        if(request.getPage()<1) request.setPage(1);
        if(request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()==null) return guideRepository.findAllGuide(pageable);
        else return guideRepository.findByNameGuide(request.getName(), pageable);
    }
}
