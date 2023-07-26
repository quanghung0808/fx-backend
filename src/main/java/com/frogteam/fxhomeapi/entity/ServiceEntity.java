package com.frogteam.fxhomeapi.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Service")
public class ServiceEntity {
    @Id
    @Column(name = "ServiceID")
    private String serviceId;
    @Column(name = "ServiceName")
    private String serviceName;
    @Column(name = "ServiceImg")
    private String serviceImg;
    @Column(name = "Status")
    private boolean status;
}
