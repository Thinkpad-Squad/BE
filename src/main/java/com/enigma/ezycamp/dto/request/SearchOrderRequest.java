package com.enigma.ezycamp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchOrderRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String date;
    private String orderId;
}
