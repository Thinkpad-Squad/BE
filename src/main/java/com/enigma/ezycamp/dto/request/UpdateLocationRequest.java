package com.enigma.ezycamp.dto.request;

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
public class UpdateLocationRequest {
    @NotBlank(message = "ID lokasi tidak boleh kosong")
    private String id;
    @NotBlank(message = "Nama lokasi tidak boleh kosong")
    private String name;
    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String description;
    private String recommendedActivity;
    private String safetyTips;
    @NotBlank(message = "Alamat toko cabang terdekat tidak boleh kosong")
    private String nearestStoreAddress;
    private List<MultipartFile> images;
}
