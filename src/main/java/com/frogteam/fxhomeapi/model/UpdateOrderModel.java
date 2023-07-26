package com.frogteam.fxhomeapi.model;

import lombok.*;

@Data
@NoArgsConstructor
public class UpdateOrderModel {
    private String repairerId;
    private String paymentMethod;
    private String status;
}
