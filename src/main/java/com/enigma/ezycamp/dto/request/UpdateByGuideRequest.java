package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateByGuideRequest {
    @NotBlank(message = "ID lokasi tidak boleh kosong")
    private String id;
    @NotBlank(message = "Rekomendasi aktivitas tidak boleh kosong")
    private String recommendationActivity;
    @NotBlank(message = "Tips keselamatan tidak boleh kosong")
    private String safetyTips;
}
