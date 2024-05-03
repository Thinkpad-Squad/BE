package com.enigma.ezycamp.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtClaims {
    private String userAccountId;
    private List<String> roles;
}
