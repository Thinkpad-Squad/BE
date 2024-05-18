package com.enigma.ezycamp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusRequest {
    private String orderId;
    private String transactionStatus;
}
