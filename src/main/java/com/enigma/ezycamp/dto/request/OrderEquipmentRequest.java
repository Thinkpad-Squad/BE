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
public class OrderEquipmentRequest {
    @NotBlank(message = "ID peralatan tidak boleh kosong")
    private String equipmentId;
    @NotNull(message = "Kuantitas penyewaan barang tidak boleh kosong")
    @Min(value = 1, message = "Kuantitas pemesanan tidak boleh kurang dari 1")
    private Integer quantity;
}
