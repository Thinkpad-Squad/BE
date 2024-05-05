package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartRequest {
    @NotBlank(message = "ID peralatan tidak boleh kosong")
    private String equipmentId;
    @NotNull(message = "Kuantitas tidak boleh kosong")
    @Min(value = 0, message = "Jumlah tidak boleh lebih kecil dari 0")
    private Integer quantity;
}
