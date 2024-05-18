package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.dto.request.PaymentDetailRequest;
import com.enigma.ezycamp.dto.request.PaymentItemDetailRequest;
import com.enigma.ezycamp.dto.request.PaymentRequest;
import com.enigma.ezycamp.entity.Equipment;
import com.enigma.ezycamp.entity.Order;
import com.enigma.ezycamp.entity.OrderEquipment;
import com.enigma.ezycamp.entity.Payment;
import com.enigma.ezycamp.repository.PaymentRepository;
import com.enigma.ezycamp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String BASE_URL_SNAP;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, RestClient restClient, @Value("${midtrans.api.key}") String secretkey, @Value("${midtrans.api.snap-url}") String baseUrlSnap) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretkey;
        BASE_URL_SNAP = baseUrlSnap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment addPayment(Order order) {
        Long amount = order.getOrderEquipments().stream().map(value-> (
                value.getQuantity()*value.getEquipment().getPrice()*order.getDay()
        )).reduce(0L, Long::sum);
        List<PaymentItemDetailRequest> itemDetailRequestList = order.getOrderEquipments().stream().map(orderEquipment ->
                PaymentItemDetailRequest.builder().name(orderEquipment.getEquipment().getName()+" ("+order.getDay()+" days)")
                        .price(orderEquipment.getEquipment().getPrice() * order.getDay())
                        .quantity(orderEquipment.getQuantity()).build()).toList();
        String id = UUID.randomUUID().toString();
        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(id)
                        .amount(amount).build())
                .paymentItemDetails(itemDetailRequestList)
                .paymentMethod(List.of("credit_card", "cimb_clicks",
                        "bca_klikbca", "bca_klikpay", "bri_epay", "echannel", "permata_va",
                        "bca_va", "bni_va", "bri_va","cimb_va", "other_va", "gopay", "indomaret",
                        "danamon_online", "akulaku", "shopeepay", "kredivo", "uob_ezpay")).build();
        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(BASE_URL_SNAP).body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> body = response.getBody();
        Payment payment = Payment.builder().id(id)
                .url(body.get("redirect_url"))
                .status("ordered").build();
        return paymentRepository.saveAndFlush(payment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment cashPayment(Order order) {
        String id = UUID.randomUUID().toString();
        Payment payment = Payment.builder().id(id).status("cash order").url("cash").build();
        return paymentRepository.saveAndFlush(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public Payment findById(String id){
        return paymentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pembayaran tidak ditemukan"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFailedAndUpdateStatus() {
        List<String> failedStatus = List.of("deny", "failure", "cancel", "expire");
        List<Payment> payments = paymentRepository.findAllByStatusIn(failedStatus);
        for (Payment payment : payments) {
            for (OrderEquipment orderEquipment : payment.getOrder().getOrderEquipments()) {
                Equipment equipment = orderEquipment.getEquipment();
                equipment.setStock(equipment.getStock() + orderEquipment.getQuantity());
            }
            payment.setStatus("canceled");
        }
    }
}
