package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateGuideRequest;
import com.enigma.ezycamp.dto.response.PagingResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Guide;
import com.enigma.ezycamp.security.AuthenticatedUser;
import com.enigma.ezycamp.service.GuideService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/guides")
@RequiredArgsConstructor
public class GuideController {
    private final GuideService guideService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasGuideId(#id)")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Guide>> findGuideById(@PathVariable String id){
        Guide guide = guideService.getGuideById(id);
        WebResponse<Guide> response = WebResponse.<Guide>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data pemandu").data(guide).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Guide>>> findAllGuide(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "location", required = false) String location
    ){
        SearchRequest request = SearchRequest.builder().page(page).size(size).param(location)
                .direction(direction).sortBy(sortBy).build();
        Page<Guide> guides = guideService.getAllGuide(request);
        PagingResponse pagingResponse = PagingResponse.builder().totalPages(guides.getTotalPages())
                .totalElement(guides.getTotalElements()).page(guides.getPageable().getPageNumber()+1)
                .size(guides.getPageable().getPageSize()).hasNext(guides.hasNext())
                .hasPrevious(guides.hasPrevious()).build();
        WebResponse<List<Guide>> response = WebResponse.<List<Guide>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data guide").data(guides.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@authenticatedUser.hasGuideId(#request.id)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Guide>> updateGuide(@RequestBody UpdateGuideRequest request){
        Guide guide = guideService.updateGuide(request);
        WebResponse<Guide> response = WebResponse.<Guide>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil memperbarui data pemandu").data(guide).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasGuideId(#id)")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse> disableGuideById(@PathVariable String id){
        guideService.disableById(id);
        WebResponse response = WebResponse.builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil menghapus data pemandu").build();
        return ResponseEntity.ok(response);
    }
}
