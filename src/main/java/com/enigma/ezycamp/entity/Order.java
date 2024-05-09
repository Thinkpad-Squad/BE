package com.enigma.ezycamp.entity;

import com.enigma.ezycamp.constant.OrderType;
import com.enigma.ezycamp.constant.PaymentType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id")
    private Guide guide;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @Column(name = "date", updatable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Column(name = "order_type")
    private OrderType orderType;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<OrderEquipment> orderEquipments;
    @OneToOne
    @JoinColumn(name = "order_guarantee_id")
    private OrderGuaranteeImage orderGuaranteeImage;
    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    @JsonManagedReference
    private Payment payment;
}
