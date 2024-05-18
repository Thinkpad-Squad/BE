package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.ChangeCartRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.dto.response.PagingResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Cart;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.security.AuthenticatedUser;
import com.enigma.ezycamp.service.CustomerService;
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
@RequestMapping(path = "/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasCustomerId(#id)")
    @GetMapping(path = "/{id}")
    public ResponseEntity<WebResponse<Customer>> findCustomerById(@PathVariable String id) {
        Customer customer = customerService.getCustomerById(id);
        WebResponse<Customer> response = WebResponse.<Customer>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil mendapatkan data customer")
                .data(customer).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasUsername(#username)")
    @GetMapping(path = "/username/{username}")
    public ResponseEntity<WebResponse<Customer>> findCustomerByUsername(@PathVariable String username) {
        Customer customer = customerService.getCustomerByUsername(username);
        WebResponse<Customer> response = WebResponse.<Customer>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil mendapatkan data customer")
                .data(customer).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Customer>>> findAllCustomer(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name) {
        SearchRequest request = SearchRequest.builder().sortBy(sortBy).size(size)
                .page(page).direction(direction).param(name).build();
        Page<Customer> customers = customerService.getAllCustomer(request);
        PagingResponse pagingResponse = PagingResponse.builder().totalPages(customers.getTotalPages())
                .totalElement(customers.getTotalElements())
                .page(customers.getPageable().getPageNumber() + 1)
                .size(customers.getPageable().getPageSize()).hasNext(customers.hasNext())
                .hasPrevious(customers.hasPrevious()).build();
        WebResponse<List<Customer>> response = WebResponse.<List<Customer>>builder()
                .statusCode(HttpStatus.OK.value()).message("Berhasil mendapatkan data customer")
                .data(customers.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@authenticatedUser.hasCustomerId(#request.id)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Customer>> updateCustomer(@RequestBody UpdateCustomerRequest request) {
        Customer customer = customerService.updateCustomer(request);
        WebResponse<Customer> response = WebResponse.<Customer>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil memperbarui data customer").data(customer).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasCustomerId(#id)")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse> disableCustomerById(@PathVariable String id) {
        customerService.disableById(id);
        WebResponse response = WebResponse.builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil menghapus data customer").build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("@authenticatedUser.hasCustomerId(#id)")
    @PutMapping(path = "/{id}/carts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Cart>>> changeCart(@PathVariable String id,
                                                              @RequestBody ChangeCartRequest request) {
        List<Cart> carts = customerService.updateCart(id, request);
        WebResponse<List<Cart>> response = WebResponse.<List<Cart>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mengubah keranjang").data(carts).build();
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasCustomerId(#id)")
    @GetMapping(path = "/{id}/carts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<Cart>>> getAllCart(@PathVariable String id){
        List<Cart> carts = customerService.getAllCart(id);
        WebResponse<List<Cart>> response = WebResponse.<List<Cart>>builder().statusCode(HttpStatus.OK.value())
                .message("Berhasil mendapatkan data keranjang").data(carts).build();
        return ResponseEntity.ok(response);
    }
}
