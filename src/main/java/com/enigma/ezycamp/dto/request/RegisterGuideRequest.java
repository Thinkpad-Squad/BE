package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterGuideRequest {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;
    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    private String phone;
    @NotNull
    @Min(value = 0, message = "Harga jasa pemandu tidak boleh kurang dari 0")
    private Long price;
    @NotNull(message = "Foto tidak boleh kosong")
    @Size(max = 10485760, message = "Ukuran file gambar tidak boleh melebihi 10MB")
    private List<MultipartFile> images;
    @NotBlank(message = "ID lokasi tidak boleh kosong")
    private String location;
    @NotBlank(message = "Username tidak boleh kosong")
    @Length(min = 6, message = "Username tidak boleh kurang dari 6 karakter")
    private String username;
    @NotBlank(message = "Password tidak boleh kosong")
    @Length(min = 8, message = "Password tidak boleh kurang dari 8 karakter")
    private String password;
}
