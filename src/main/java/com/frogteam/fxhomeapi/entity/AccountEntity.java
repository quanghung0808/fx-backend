package com.frogteam.fxhomeapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Account")
public class AccountEntity {
    @Id
    @Column(name = "AccountID")
    private String accountId;

    @Column(name = "RoleID")
    private String roleId;

    @Column(name = "RatingID")
    private String ratingId;

    @Column(name = "Email")
    private String email;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Password")
    private String password;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "DateOfBirth")
    private LocalDateTime dateOfBirth;

    @Column(name = "Address")
    private String address;

    @Column(name = "BankingNumber")
    private String bankingNumber;

    @Column(name = "AverageRatingStar")
    private Float AverageRatingStar;

    @Column(name = "IdentityNumber")
    private String identityNumber;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "ExperienceYear")
    private Integer experienceYear;

    @Column(name = "Status")
    private boolean status;




}
