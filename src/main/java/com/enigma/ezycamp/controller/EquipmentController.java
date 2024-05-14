package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewEquipmentRequest;
import com.enigma.ezycamp.dto.request.RegisterGuideRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateEquipmentRequest;
import com.enigma.ezycamp.dto.response.PagingResponse;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.service.EquipmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final ObjectMapper objectMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Equipment>> addEquipment(@RequestPart(name = "equipment") String jsonEq,
                                                               @RequestPart(name = "images") List<MultipartFile> images){
        WebResponse<Equipment> response;
        try {
            NewEquipmentRequest request = objectMapper.readValue(jsonEq, new TypeReference<NewEquipmentRequest>() {});
            request.setImages(images);
            Equipment equipment = equipmentService.addEquipment(request);
            response = WebResponse.<Equipment>builder()
                    .statusCode(HttpStatus.CREATED.value()).message("Berhasil menambahkan data peralatan")
                    .data(equipment).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            response = WebResponse.<Equipment>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal memperbarui peralatan").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Equipment>> findEquipmentById(@PathVariable String id){
        Equipment equipment = equipmentService.getEquipmentById(id);
        WebResponse<Equipment> response = WebResponse.<Equipment>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data peralatan").data(equipment).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Equipment>>> findAllEquipment(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ){
        SearchRequest request = SearchRequest.builder().sortBy(sortBy).direction(direction)
                .size(size).page(page).param(name).build();
        Page<Equipment> equipments = equipmentService.getAllEquipment(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(equipments.getTotalPages())
                .totalElement(equipments.getTotalElements())
                .page(equipments.getPageable().getPageNumber()+1)
                .size(equipments.getPageable().getPageSize())
                .hasNext(equipments.hasNext())
                .hasPrevious(equipments.hasPrevious())
                .build();
        WebResponse<List<Equipment>> response = WebResponse.<List<Equipment>>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil mendapatkan data peralatan")
                .data(equipments.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Equipment>> updateEquipment(@RequestPart(name = "equipment") String jsonEq, @RequestPart(name = "images", required = false)List<MultipartFile> images){
        WebResponse<Equipment> response;
        try {
            UpdateEquipmentRequest request = objectMapper.readValue(jsonEq, new TypeReference<UpdateEquipmentRequest>() {});
            request.setImages(images);
            Equipment equipment = equipmentService.updateEquipment(request);
            response = WebResponse.<Equipment>builder()
                    .statusCode(HttpStatus.OK.value()).message("Berhasil memperbarui data peralatan")
                    .data(equipment).build();
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response = WebResponse.<Equipment>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal memperbarui peralatan").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse> disableEquipment(@PathVariable String id){
        equipmentService.disableEquipmentById(id);
        WebResponse response = WebResponse.builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil menghapus data peralatan").build();
        return ResponseEntity.ok(response);
    }
}
