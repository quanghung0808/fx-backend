package com.frogteam.fxhomeapi.enums;


import lombok.Getter;

@Getter
public enum PaymentMethodEnum {
    CASH("Cash", "Tiền mặt"),
    BANK_TRANSFER("Banking", "Chuyển khoản");

    private final String method;
    private final String methodVN;

    PaymentMethodEnum(String method, String methodVN) {
        this.method =  method;
        this.methodVN =  methodVN;

    }
}