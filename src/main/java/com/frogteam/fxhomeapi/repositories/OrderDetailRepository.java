package com.frogteam.fxhomeapi.repositories;

import com.frogteam.fxhomeapi.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,String> {
    public Optional<List<OrderDetailEntity>> findByOrderId(String orderId);

}
