package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewLocationRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateByGuideRequest;
import com.enigma.ezycamp.dto.request.UpdateLocationRequest;
import com.enigma.ezycamp.dto.response.PagingResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.entity.Location;
import com.enigma.ezycamp.service.LocationService;
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
@RequestMapping(path = "/api/locations")
public class LocationController {
    private final LocationService locationService;
    private final ObjectMapper objectMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Location>> addLocation(@RequestPart(name = "location") String jsonLocation,
                                                             @RequestPart(name = "images")List<MultipartFile> images){
        WebResponse<Location> response;
        try {
            NewLocationRequest request = objectMapper.readValue(jsonLocation, new TypeReference<NewLocationRequest>() {});
            request.setImages(images);
            Location location = locationService.addLocation(request);
            response = WebResponse.<Location>builder().statusCode(HttpStatus.CREATED.value())
                    .message("Berhasil menambahkan lokasi").data(location).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            response = WebResponse.<Location>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal menambahkan data lokasi").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(path = "/{id}")
    public ResponseEntity<WebResponse<Location>> findLocationById(@PathVariable String id){
        Location location = locationService.getById(id);
        WebResponse<Location> response = WebResponse.<Location>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data lokasi").data(location).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Location>>> findAllLocation(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ){
        SearchRequest request = SearchRequest.builder().param(name).direction(direction)
                .page(page).size(size).sortBy(sortBy).build();
        Page<Location> locations = locationService.getAll(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(locations.getTotalPages())
                .totalElement(locations.getTotalElements())
                .page(locations.getPageable().getPageNumber()+1)
                .size(locations.getPageable().getPageSize())
                .hasNext(locations.hasNext())
                .hasPrevious(locations.hasPrevious()).build();
        WebResponse<List<Location>> response = WebResponse.<List<Location>>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil mendapatkan data lokasi")
                .data(locations.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Location>> updateLocation(@RequestPart(name = "location") String jsonLocation, @RequestPart(name = "images", required = false)List<MultipartFile> images){
        WebResponse<Location> response;
        try {
            UpdateLocationRequest request = objectMapper.readValue(jsonLocation, new TypeReference<UpdateLocationRequest>() {});
            request.setImages(images);
            Location location = locationService.updateLocation(request);
            response = WebResponse.<Location>builder()
                    .statusCode(HttpStatus.OK.value()).message("Berhasil memperbarui data lokasi")
                    .data(location).build();
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response = WebResponse.<Location>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal memperbarui data lokasi").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','GUIDE')")
    @PutMapping(path = "/guide", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Location>> updateByGuide(@RequestBody UpdateByGuideRequest request){
        Location location = locationService.updateByGuide(request);
        WebResponse<Location> response = WebResponse.<Location>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil memperbarui data lokasi").data(location).build();
        return ResponseEntity.ok(response);
    }
}
