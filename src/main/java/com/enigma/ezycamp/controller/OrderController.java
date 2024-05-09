package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.NewOrderRequest;
import com.enigma.ezycamp.dto.request.SearchOrderRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateStatusRequest;
import com.enigma.ezycamp.dto.response.PagingResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Order;
import com.enigma.ezycamp.security.AuthenticatedUser;
import com.enigma.ezycamp.service.OrderService;
import com.enigma.ezycamp.util.ValidationUtil;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Order>> addOrder(@RequestPart(name = "order") String jsonOrder,
                                                       @RequestPart(name = "guarantee")MultipartFile guarantee){
        WebResponse<Order> response;
        try {
            NewOrderRequest request = objectMapper.readValue(jsonOrder, new TypeReference<NewOrderRequest>() {});
            request.setImage(guarantee);
            Order order = orderService.addOrder(request);
            response = WebResponse.<Order>builder().statusCode(HttpStatus.CREATED.value())
                    .message("Berhasil menambahkan transaksi").data(order).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (JsonProcessingException e){
            response = WebResponse.<Order>builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Gagal menambahkan transaksi").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Order>>> getAllOrder(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "date", required = false) String date
    ){
        SearchOrderRequest request = SearchOrderRequest.builder().customerName(name)
                .date(date).direction(direction).page(page).size(size).sortBy(sortBy).build();
        Page<Order> orders = orderService.findAllOrder(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(orders.getTotalPages())
                .totalElement(orders.getTotalElements())
                .page(orders.getPageable().getPageNumber()+1)
                .size(orders.getPageable().getPageSize())
                .hasNext(orders.hasNext())
                .hasPrevious(orders.hasPrevious())
                .build();
        WebResponse<List<Order>> response = WebResponse.<List<Order>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data pemesanan").data(orders.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Order>>> getAllOrderByCustomerId(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @PathVariable String id
    ){
        SearchRequest request = SearchRequest.builder().direction(direction).page(page)
                .size(size).sortBy(sortBy).param(id).build();
        Page<Order> orders = orderService.findByCustomerId(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(orders.getTotalPages())
                .totalElement(orders.getTotalElements())
                .page(orders.getPageable().getPageNumber()+1)
                .size(orders.getPageable().getPageSize())
                .hasNext(orders.hasNext())
                .hasPrevious(orders.hasPrevious())
                .build();
        WebResponse<List<Order>> response = WebResponse.<List<Order>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data pemesanan").data(orders.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/guide/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Order>>> getAllOrderByGuideId(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @PathVariable String id
    ){
        SearchRequest request = SearchRequest.builder().direction(direction).page(page)
                .size(size).sortBy(sortBy).param(id).build();
        Page<Order> orders = orderService.findByGuideId(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(orders.getTotalPages())
                .totalElement(orders.getTotalElements())
                .page(orders.getPageable().getPageNumber()+1)
                .size(orders.getPageable().getPageSize())
                .hasNext(orders.hasNext())
                .hasPrevious(orders.hasPrevious())
                .build();
        WebResponse<List<Order>> response = WebResponse.<List<Order>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data pemesanan").data(orders.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/status")
    public ResponseEntity<WebResponse<?>> updateStatus(@RequestBody Map<String, Object> request){
        UpdateStatusRequest statusRequest = UpdateStatusRequest.builder()
                .paymentId(request.get("order_id").toString())
                .status(request.get("transaction_status").toString()).build();
        orderService.updateStatus(statusRequest);
        return ResponseEntity.ok(WebResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Berhasil memperbarui status transaksi").build());
    }
}
