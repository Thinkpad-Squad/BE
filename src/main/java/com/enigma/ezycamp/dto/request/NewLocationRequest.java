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
    @NotNull(message = "Gambar lokasi tidak boleh kosong")
    private List<MultipartFile> images;
}
