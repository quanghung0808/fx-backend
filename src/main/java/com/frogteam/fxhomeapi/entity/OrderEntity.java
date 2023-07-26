package com.frogteam.fxhomeapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Orders")
public class OrderEntity {
    @Id
    @Column(name = "OrderID")
    private String orderID;

    @Column(name = "CustomerID")
    private String customerId;

    @Column(name = "RepairerID")
    private String repairerID;

    @Column(name = "OrderName")
    private String orderName;

    @Column(name = "OrderDate")
    private LocalDateTime orderDate;

    @Column(name = "BookingTime")
    private LocalDateTime bookingTime;

    @Column(name = "TotalPrice")
    private int totalPrice;

    @Column(name = "Address")
    private String address;

    @Column(name = "Note")
    private String note;

    @Column(name = "Status")
    private String status;

    @Column(name = "PaymentMethod")
    private String paymentMethod;

}
