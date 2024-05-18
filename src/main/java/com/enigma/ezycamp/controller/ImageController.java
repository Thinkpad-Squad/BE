package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.service.EquipmentImageService;
import com.enigma.ezycamp.service.GuideImageService;
import com.enigma.ezycamp.service.LocationImageService;
import com.enigma.ezycamp.service.OrderGuaranteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final EquipmentImageService equipmentImageService;
    private final GuideImageService guideImageService;
    private final LocationImageService locationImageService;
    private final OrderGuaranteeService orderGuaranteeService;

    @GetMapping(path = "/api/equipments/images/{imageName}")
    public ResponseEntity<Resource> downloadEquipmentImage(@PathVariable(name = "imageName") String name){
        Resource resource = equipmentImageService.getByName(name);
        String headerValue = String.format("attachment; filename=%s", resource.getFilename());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    }

    @GetMapping(path = "/api/guides/images/{imageName}")
    public ResponseEntity<Resource> downloadGuideImage(@PathVariable(name = "imageName") String name){
        Resource resource = guideImageService.getByName(name);
        String headerValue = String.format("attachment; filename=%s", resource.getFilename());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    }

    @GetMapping(path = "/api/locations/images/{imageName}")
    public ResponseEntity<Resource> downloadLocationImage(@PathVariable(name = "imageName") String name){
        Resource resource = locationImageService.getByName(name);
        String headerValue = String.format("attachment; filename=%s", resource.getFilename());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    }

    @GetMapping(path = "/api/orders/guaranteeImages/{imageName}")
    public ResponseEntity<Resource> downloadGuaranteeImage(@PathVariable(name = "imageName") String name){
        Resource resource = orderGuaranteeService.getByName(name);
        String headerValue = String.format("attachment; filename=%s", resource.getFilename());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
    }
}
