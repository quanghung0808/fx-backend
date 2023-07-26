package com.frogteam.fxhomeapi.service;

import com.frogteam.fxhomeapi.entity.OrderEntity;
import com.frogteam.fxhomeapi.model.OrderModel;
import com.frogteam.fxhomeapi.model.OrderResponse;
import com.frogteam.fxhomeapi.model.UpdateOrderModel;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderModel orderModel) throws Exception;

    List<OrderEntity> getOrders();

    OrderResponse updateOrder(String orderName, UpdateOrderModel updateOrderModel) throws Exception;


    List<OrderEntity> getOrdersByCustomerIdAndStatus(String customerId , String status) throws Exception;

    List<OrderEntity> getOrdersByRepairerIdAndStatus(String repairerId , String status) throws Exception;

    OrderResponse getOrderByOrderId(String orderId) throws Exception;

}
