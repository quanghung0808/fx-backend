package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailResponse {
    private String serviceName;
    private String formName;
    private int price;
    private int quantity;
    private String note;

}
