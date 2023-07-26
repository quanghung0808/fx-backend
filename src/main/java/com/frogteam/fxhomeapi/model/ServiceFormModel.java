package com.frogteam.fxhomeapi.model;

import lombok.*;

@Builder
@Data
public class ServiceFormModel {
    private String formName;
    private int price;
    private boolean status;

}
