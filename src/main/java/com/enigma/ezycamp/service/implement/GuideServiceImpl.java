package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.GuideRepository;
import com.enigma.ezycamp.service.GuideService;
import com.enigma.ezycamp.util.ValidationUtil;
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
    private final ValidationUtil validationUtil;

    @Transactional(readOnly = true)
    @Override
    public Guide getGuideById(String id) {
        return guideRepository.findByIdGuide(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pemandu tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Guide> getAllGuide(SearchRequest request) {
        if(request.getPage()<1) request.setPage(1);
        if(request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getParam()==null) return guideRepository.findAllGuide(pageable);
        else return guideRepository.findByLocationGuide("%"+request.getParam()+"%", pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Guide updateGuide(UpdateGuideRequest request) {
        validationUtil.validate(request);
        Guide guide = getGuideById(request.getId());
        guide.setName(request.getName());
        guide.setPhone(request.getPhone());
        guide.setPrice(request.getPrice());
        UserAccount account = guide.getUserAccount();
        account.setUsername(request.getUsername());
        guide.setUserAccount(account);
        return guideRepository.saveAndFlush(guide);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disableById(String id) {
        Guide guide = getGuideById(id);
        UserAccount account = guide.getUserAccount();
        account.setIsEnable(false);
        guide.setUserAccount(account);
        guideRepository.saveAndFlush(guide);
    }
}
