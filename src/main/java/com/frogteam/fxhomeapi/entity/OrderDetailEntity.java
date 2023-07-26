package com.frogteam.fxhomeapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="OrderDetail")
public class OrderDetailEntity {
    @Id
    @Column(name = "OrderDetailID")
    private String orderDetailId;

    @Column(name = "FormID")
    private String formId;

    @Column(name = "OrderID")
    private String orderId;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Note")
    private String note;

    @Column(name = "Status")
    private boolean status;
}
