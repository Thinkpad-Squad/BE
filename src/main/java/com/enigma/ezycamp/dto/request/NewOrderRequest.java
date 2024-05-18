package com.enigma.ezycamp.dto.request;

import com.enigma.ezycamp.constant.OrderType;
import com.enigma.ezycamp.constant.PaymentType;
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
public class NewOrderRequest {
    @NotBlank(message = "ID customer tidak boleh kosong")
    private String customerId;
    private String guideId;
    @NotBlank(message = "ID lokasi tidak boleh kosong")
    private String locationId;
    @NotNull(message = "Tanggal transaksi tidak boleh kosong")
    private String date;
    @NotNull(message = "Hari peminjaman tidak boleh kosong")
    @Min(value = 1, message = "Hari peminjaman tidak boleh kurang dari 1")
    private Integer day;
    private String sentAddress;
    @NotNull(message = "Daftar peralatan yang disewa tidak boleh kosong")
    @Size(min = 1, message = "Daftar peralatan yang disewa tidak boleh kosong")
    private List<OrderEquipmentRequest> orderEquipmentRequests;
    @NotNull(message = "Gambar jaminan diperlukan untuk menyewa")
    private MultipartFile image;
    @NotNull(message = "Tipe pemesanan tidak boleh kosong")
    private OrderType orderType;
    @NotNull(message = "Tipe pembayaran tidak boleh kosong")
    private PaymentType paymentType;
}
