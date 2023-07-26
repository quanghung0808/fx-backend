package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerRegisterRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String fullName;
    private LocalDateTime dateOfBirth;
    private String address;
}