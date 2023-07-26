package com.frogteam.fxhomeapi.enums;


import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    WAITING("WAITING", "Chờ xác nhận"),
    PENDING("PENDING", "Chờ xử lí"),
    CANCELED("CANCELED", "Đã hủy"),
    PROGRESS("IN PROGRESS", "Đang thực hiện"),
    PAYMENT("WAITING FOR PAYMENT", "Chờ thanh toán"),
    COMPLETED("COMPLETED", "Đã hoàn thành");


    private final String status;
    private final String statusVN;

    OrderStatusEnum(String status, String statusVN) {
        this.status = status;
        this.statusVN = statusVN;
    }
}