package com.enigma.ezycamp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;
    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    private String phone;
    @NotBlank(message = "Username tidak boleh kosong")
    @Length(min = 6, message = "Username tidak boleh kurang dari 6 karakter")
    private String username;
    @NotBlank(message = "Password tidak boleh kosong")
    @Length(min = 8, message = "Password tidak boleh kurang dari 8 karakter")
    private String password;
}
