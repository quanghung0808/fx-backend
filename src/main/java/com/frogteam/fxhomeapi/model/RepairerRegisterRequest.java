package com.frogteam.fxhomeapi.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RepairerRegisterRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String fullName;
    private LocalDateTime dateOfBirth;
    private String address;
    private String bankingNumber;
    private String identityNumber;
    private String specialization;
    private int experienceYear;
}
