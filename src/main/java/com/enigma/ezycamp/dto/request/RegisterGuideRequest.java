package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

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
    @NotNull(message = "Foto diri tidak boleh kosong")
    @Size(min = 1, max = 2097152, message = "Foto diri tidak boleh berukuran lebih dari 2MB")
    private MultipartFile selfieImage;
    @NotNull(message = "Kartu identitas tidak boleh kosong")
    @Size(min = 1, max = 2097152, message = "Kartu identitas tidak boleh berukuran lebih dari 2MB")
    private MultipartFile idCardImage;
    @NotBlank(message = "Lokasi pemanduan wisata tidak boleh kosong")
    private String location;
    @NotBlank(message = "Username tidak boleh kosong")
    @Length(min = 6, message = "Username tidak boleh kurang dari 6 karakter")
    private String username;
    @NotBlank(message = "Password tidak boleh kosong")
    @Length(min = 8, message = "Password tidak boleh kurang dari 8 karakter")
    private String password;
}
