package com.frogteam.fxhomeapi.repositories;

import com.frogteam.fxhomeapi.entity.OrderEntity;
import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,String> {
    public Optional<OrderEntity> findByOrderID(String orderId);
    public Optional<OrderEntity> findByOrderName(String orderName);
    public Optional<OrderEntity> findByCustomerIdAndStatusNot(String customerId, String status);
    public Optional<OrderEntity> findByCustomerIdAndStatusNotIn(String customerId,  List<String> status);

    public Optional<List<OrderEntity>> findByRepairerIDAndStatusNotIn(String repairerId,  List<String> status);

    public Optional<List<OrderEntity>> findByCustomerIdAndStatus(String customerId, String status);
    public Optional<List<OrderEntity>> findByRepairerIDAndStatus(String repairerId, String status);


}
