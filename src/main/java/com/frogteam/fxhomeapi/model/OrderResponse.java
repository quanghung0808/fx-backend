package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
public class OrderResponse {
    private String orderId;
    private String orderName;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String repairerId;
    private String repairerName;
    private String repairerPhone;
    private String repairerExp;
    private String repairerSpe;
    private LocalDateTime bookingTime;
    private List<OrderDetailResponse> orderDetails;
    private String paymentMethod;
    private int totalPrice;
    private String note;
    private String status;
}
