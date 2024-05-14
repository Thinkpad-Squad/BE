package com.enigma.ezycamp.service;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.repository.GuideRepository;
import com.enigma.ezycamp.service.implement.GuideServiceImpl;
import com.enigma.ezycamp.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class GuideServiceTest {
    @Mock
    private GuideRepository guideRepository;
    @Mock
    private ValidationUtil validationUtil;
    private GuideService guideService;
    @BeforeEach
    void setUp(){
        guideService = new GuideServiceImpl(guideRepository, validationUtil);
    }

    @Test
    void getGuideById(){
        String id = "1";
        Guide guide = Guide.builder().id(id).build();
        when(guideRepository.findByIdGuide(anyString())).thenReturn(Optional.of(guide));
        Guide result = guideService.getGuideById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    void getAllGuide(){
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        Page<Guide> guides = new PageImpl<>(Collections.emptyList());
        when(guideRepository.findAllGuide(any(Pageable.class))).thenReturn(guides);
        Page<Guide> result = guideService.getAllGuide(request);
        assertEquals(result.getTotalPages(), guides.getTotalPages());
    }


    @Test
    void updateGuide(){
        UpdateGuideRequest request = UpdateGuideRequest.builder().id("1").name("name").phone("0888").price(1L).username("username").build();
        Guide guide = Guide.builder().id(request.getId()).name(request.getName()).phone(request.getPhone()).price(request.getPrice()).userAccount(UserAccount.builder().username(request.getUsername()).build()).build();
        when(guideRepository.findByIdGuide(anyString())).thenReturn(Optional.of(guide));
        when(guideRepository.saveAndFlush(any(Guide.class))).thenReturn(guide);
        Guide result = guideService.updateGuide(request);
        assertEquals(result.getId(), request.getId());
        assertEquals(result.getName(), request.getName());
    }

    @Test
    void disableById(){
        String id = "1";
        Guide guide = Guide.builder().id(id).userAccount(UserAccount.builder().isEnable(false).build()).build();
        when(guideRepository.findByIdGuide(anyString())).thenReturn(Optional.of(guide));
        when(guideRepository.saveAndFlush(any(Guide.class))).thenReturn(guide);
        guideService.disableById(id);
        verify(guideRepository, times(1)).saveAndFlush(any(Guide.class));
    }
}
