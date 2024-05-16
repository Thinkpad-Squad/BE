package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEquipmentRequest {
    @NotBlank(message = "Nama peralatan tidak boleh kosong")
    private String name;
    @NotBlank(message = "Deskripsi peralatan tidak boleh kosong")
    private String description;
    @NotNull(message = "Harga sewa tidak boleh kosong")
    @Min(value = 0, message = "Harga sewa peralatan tidak boleh kurang dari 0")
    private Long price;
    @NotNull(message = "Stok peralatan tidak boleh kosong")
    @Min(value = 0, message = "Stok peralatan tidak boleh kurang dari 0")
    private Integer stock;
    @NotNull(message = "Gambar peralatan tidak boleh kosong")
    @Size(min = 1, message = "Gambar peralatan tidak boleh kosong")
    private List<MultipartFile> images;
}
