package com.frogteam.fxhomeapi.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ServiceForm")
public class ServiceFormEntity {
    @Id
    @Column(name = "FormID")
    private String formId;
    @Column(name = "ServiceID")
    private String serviceId;
    @Column(name = "FormName")
    private String formName;
    @Column(name = "Price")
    private int price;
    @Column(name = "Status")
    private boolean status;
}