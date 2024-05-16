package com.enigma.ezycamp.controller;

import com.enigma.ezycamp.constant.UserRole;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateCustomerRequest;
import com.enigma.ezycamp.dto.response.RegisterResponse;
import com.enigma.ezycamp.dto.response.WebResponse;
import com.enigma.ezycamp.entity.Customer;
import com.enigma.ezycamp.entity.Role;
import com.enigma.ezycamp.entity.UserAccount;
import com.enigma.ezycamp.security.AuthenticatedUser;
import com.enigma.ezycamp.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @MockBean
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(roles = "ADMIN")
    @Test
    void findCustomerById() throws Exception {
        String id = "1";
        Customer customer = Customer.builder().id(id).build();
        when(customerService.getCustomerById(anyString())).thenReturn(customer);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/"+id)
                        .content(id))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    WebResponse<Customer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Customer>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Berhasil mendapatkan data customer", response.getMessage());
                } );
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findCustomerByUsername() throws Exception{
        when(authenticatedUser.hasCustomerId(anyString())).thenReturn(true);
        String username = "user";
        UserAccount account = UserAccount.builder().username(username).roles(List.of(Role.builder().role(UserRole.ROLE_CUSTOMER).build())).isEnable(true).build();
        Customer customer = Customer.builder().userAccount(account).build();
        when(customerService.getCustomerByUsername(anyString())).thenReturn(customer);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/username/"+username)
                        .content(username))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findAllCustomer() throws Exception {
        SearchRequest request = SearchRequest.builder().page(1).size(10).sortBy("name").direction("asc").build();
        List<Customer> customers = List.of(Customer.builder().build());
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        Page<Customer> customerPage = new PageImpl<>(customers,page, customers.size());
        when(customerService.getAllCustomer(any(SearchRequest.class))).thenReturn(customerPage);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers").content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<List<Customer>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<Customer>>>() {});
                    assertEquals(200, response.getStatusCode());
                });
    }

    @WithMockUser(roles = "CUSTOMER")
    @Test
    void updateCustomer() throws Exception {
        when(authenticatedUser.hasCustomerId(anyString())).thenReturn(true);
        UpdateCustomerRequest request = UpdateCustomerRequest.builder().id("1").name("name").phone("0888").username("username").build();
        Customer customer = Customer.builder().id(request.getId()).name(request.getName()).build();
        when(customerService.updateCustomer(any(UpdateCustomerRequest.class))).thenReturn(customer);
        String stringRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers").contentType(MediaType.APPLICATION_JSON_VALUE).content(stringRequest)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<Customer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Customer>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(request.getName(), response.getData().getName());
                });
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void disableCustomerById() throws Exception {
        String id = "1";
        doNothing().when(customerService).disableById(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/"+id).content(id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {WebResponse<Customer> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<Customer>>() {});
                    assertEquals(200, response.getStatusCode());
                    assertEquals(response.getMessage(), "Berhasil menghapus data customer");
                });
    }
}
