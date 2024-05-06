package com.enigma.ezycamp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String param;
}
