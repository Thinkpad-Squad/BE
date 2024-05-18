package com.enigma.ezycamp.service.implement;

import com.enigma.ezycamp.constant.OrderStatus;
import com.enigma.ezycamp.constant.PaymentType;
import com.enigma.ezycamp.dto.request.NewOrderRequest;
import com.enigma.ezycamp.dto.request.SearchRequest;
import com.enigma.ezycamp.dto.request.UpdateStatusRequest;
import com.enigma.ezycamp.entity.*;
import com.enigma.ezycamp.repository.OrderRepository;
import com.enigma.ezycamp.service.*;
import com.enigma.ezycamp.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final EquipmentService equipmentService;
    private final GuideService guideService;
    private final LocationService locationService;
    private final OrderGuaranteeService guaranteeService;
    private final PaymentService paymentService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrder(NewOrderRequest request) {
        validationUtil.validate(request);
        Customer customer = customerService.getCustomerById(request.getCustomerId());
        Location location = locationService.getById(request.getLocationId());
        Order order = Order.builder().customer(customer).location(location).orderStatus(OrderStatus.PENDING)
                .date(parseDate(request.getDate())).orderType(request.getOrderType())
                .paymentType(request.getPaymentType()).day(request.getDay()).build();
        if (request.getGuideId() != null) order.setGuide(guideService.getGuideById(request.getGuideId()));
        if(request.getSentAddress()!=null) order.setSentAddress(request.getSentAddress());
        OrderGuaranteeImage guaranteeImage = guaranteeService.addGuarantee(request.getImage());
        order.setOrderGuaranteeImage(guaranteeImage);
        List<OrderEquipment> orderEquipments = request.getOrderEquipmentRequests().stream().map(eq -> {
            Equipment equipment = equipmentService.getEquipmentById(eq.getEquipmentId());
            if(equipment.getStock() < eq.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity pemesanan melebihi stock barang");
            else equipmentService.changeStock(eq.getEquipmentId(), equipment.getStock()-eq.getQuantity());
            return OrderEquipment.builder().equipment(equipment).quantity(eq.getQuantity()).order(order)
                    .build();
        }).toList();
        order.setOrderEquipments(orderEquipments);
        orderRepository.saveAndFlush(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order changeOrderStatus(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data order tidak ditemukan"));
        if (order.getOrderStatus() == OrderStatus.ACTIVE) order.setOrderStatus(OrderStatus.FINISHED);
        else if(order.getOrderStatus() == OrderStatus.PENDING){
            if(order.getPaymentType() == PaymentType.TRANSFER){
                Payment payment = paymentService.addPayment(order);
                order.setPayment(payment);
            } else {
                Payment payment = paymentService.cashPayment(order);
                order.setPayment(payment);
            }
            order.setOrderStatus(OrderStatus.ACTIVE);
        }
        return orderRepository.saveAndFlush(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order rejectOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data order tidak ditemukan"));
        if(order.getOrderStatus() == OrderStatus.PENDING) order.setOrderStatus(OrderStatus.REJECTED);
        return orderRepository.saveAndFlush(order);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Order> findAllOrder(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getParam() != null) return orderRepository.findByOrderId(request.getParam(), pageable);
        else return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Order> findByCustomerId(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return orderRepository.findAllByCustomerId(request.getParam(), pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Order> findByGuideId(SearchRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(10);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        return orderRepository.findAllByGuideId(request.getParam(), pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateStatusRequest request) {
        Payment payment = paymentService.findById(request.getOrderId());
        payment.setStatus(request.getTransactionStatus());
    }

    private static Date parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date tempDate = new Date();
        try {
            tempDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tempDate;
    }
}
