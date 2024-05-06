package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGuideRequest {
    @NotBlank(message = "ID pemandu tidak boleh kosong")
    private String id;
    @NotBlank(message = "Nama pemandu tidak boleh kosong")
    private String name;
    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    private String phone;
    @NotNull(message = "Harga jasa tidak boleh kosong")
    @Min(value = 0, message = "Harga jasa tidak boleh kurang dari 0")
    private Long price;
    @NotBlank(message = "Username tidak boleh kosong")
    @Length(min = 6, message = "Username tidak boleh kurang dari 6 karakter")
    private String username;
}
