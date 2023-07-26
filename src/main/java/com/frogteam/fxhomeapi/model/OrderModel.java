package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderModel {
    private String customerId;
    private LocalDateTime bookingTime;
    private List<OrderDetailModel> services;
    private String paymentMethod;
    private int totalPrice;
    private String note;
}
