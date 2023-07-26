package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailModel {
    private String formId;
    private int  quantity;
    private String note;
}
