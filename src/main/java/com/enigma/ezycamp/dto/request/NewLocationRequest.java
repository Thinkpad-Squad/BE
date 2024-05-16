package com.enigma.ezycamp.dto.request;

import com.enigma.ezycamp.entity.LocationImage;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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
public class NewLocationRequest {
    @NotBlank(message = "Nama lokasi tidak boleh kosong")
    private String name;
    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String description;
    private String recommendedActivity;
    private String safetyTips;
    @NotBlank(message = "Alamat toko cabang terdekat tidak boleh kosong")
    private String nearestStoreAddress;
    @NotNull(message = "Gambar lokasi tidak boleh kosong")
    @Size(max = 10485760, message = "Ukuran file gambar tidak boleh melebihi 10MB")
    @Size(min = 1, message = "Gambar lokasi tidak boleh kosong")
    private List<MultipartFile> images;
}
