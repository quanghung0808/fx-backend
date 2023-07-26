package com.frogteam.fxhomeapi.controller;

import com.frogteam.fxhomeapi.entity.OrderEntity;
import com.frogteam.fxhomeapi.model.OrderModel;
import com.frogteam.fxhomeapi.model.OrderResponse;
import com.frogteam.fxhomeapi.model.ResponseDTO;
import com.frogteam.fxhomeapi.model.UpdateOrderModel;
import com.frogteam.fxhomeapi.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createOrder(@RequestBody(required = false) OrderModel order) {
        try {
            OrderResponse orderResponse = orderService.createOrder(order);
            ResponseDTO response = ResponseDTO.success();
            response.setData(orderResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping()
    public ResponseEntity<ResponseDTO> getOrders() {
        try {
            List<OrderEntity> orders = orderService.getOrders();
            ResponseDTO response = ResponseDTO.success();
            response.setData(orders);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "{order-id}")
    public ResponseEntity<ResponseDTO> updateOrder(@RequestBody(required = false) UpdateOrderModel updateStatusModel, @PathVariable("order-id") String orderId) {
        try {
            OrderResponse orderResponse = orderService.updateOrder(orderId, updateStatusModel);
            ResponseDTO response = ResponseDTO.success();
            response.setData(orderResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Get orders by customer ID")
    @GetMapping(params = {"customerId", "status"} , path = "customer")
    public ResponseEntity<ResponseDTO> getOrdersByCustomerId(@RequestParam("customerId") String customerId, @RequestParam("status") String status) {
        try {
            List<OrderEntity> orders = orderService.getOrdersByCustomerIdAndStatus(customerId, status);
            ResponseDTO response = ResponseDTO.success();
            response.setData(orders);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary = "Get orders by repairer ID")
    @GetMapping(params =  {"repairerId", "status"}, path = "repairer")
    public ResponseEntity<ResponseDTO> getOrdersByRepairerId(@RequestParam("repairerId") String repairerId, @RequestParam("status") String status) {
        try {
            List<OrderEntity> orders = orderService.getOrdersByRepairerIdAndStatus(repairerId, status);
            ResponseDTO response = ResponseDTO.success();
            response.setData(orders);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "{order-id}")
    public ResponseEntity<ResponseDTO> getOrderByOrderId(@PathVariable("order-id") String orderId) {
        try {
            OrderResponse order = orderService.getOrderByOrderId(orderId);
            ResponseDTO response = ResponseDTO.success();
            response.setData(order);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}