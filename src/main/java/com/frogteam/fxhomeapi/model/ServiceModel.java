package com.frogteam.fxhomeapi.model;

import lombok.*;

@Builder
@Data
public class ServiceModel {
    private String serviceName;
    private String serviceImg;
    private boolean status;
}
