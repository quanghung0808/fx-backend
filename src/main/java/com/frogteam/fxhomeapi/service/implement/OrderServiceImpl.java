package com.frogteam.fxhomeapi.service.implement;

import com.frogteam.fxhomeapi.entity.AccountEntity;
import com.frogteam.fxhomeapi.entity.OrderDetailEntity;
import com.frogteam.fxhomeapi.entity.OrderEntity;
import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import com.frogteam.fxhomeapi.enums.OrderStatusEnum;
import com.frogteam.fxhomeapi.enums.PaymentMethodEnum;
import com.frogteam.fxhomeapi.model.*;
import com.frogteam.fxhomeapi.repositories.*;
import com.frogteam.fxhomeapi.service.OrderService;
import com.frogteam.fxhomeapi.utils.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    ServiceFormRepository serviceFormRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public OrderResponse createOrder(OrderModel orderModel) throws Exception {
        try {
            String customerId = orderModel.getCustomerId();
            List<String> statusList = new ArrayList<>();
            statusList.add(OrderStatusEnum.COMPLETED.getStatus());
            statusList.add(OrderStatusEnum.CANCELED.getStatus());

            Optional<OrderEntity> opt = orderRepository.findByCustomerIdAndStatusNotIn(customerId, statusList);
            if (opt.isEmpty()) {
                AccountEntity customer = accountRepository.findByAccountId(customerId).get();
                String payment = "";
                if (PaymentMethodEnum.BANK_TRANSFER.getMethod().equalsIgnoreCase(orderModel.getPaymentMethod())) {
                    payment = PaymentMethodEnum.BANK_TRANSFER.getMethod();
                } else if (PaymentMethodEnum.CASH.getMethod().equalsIgnoreCase(orderModel.getPaymentMethod())) {
                    payment = PaymentMethodEnum.CASH.getMethod();
                } else throw new DataIntegrityViolationException("Payment method is not right format");
                String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                Timestamp now = DateHelper.getTimestampAtZone(DateHelper.VIETNAM_ZONE);
                // POST a order
                OrderEntity orderEntity = OrderEntity.builder()
                        .orderID(uuid)
                        .customerId(orderModel.getCustomerId())
                        .orderName(uuid)
                        .orderDate(now.toLocalDateTime())
                        .bookingTime(orderModel.getBookingTime())
                        .totalPrice(orderModel.getTotalPrice())
                        .address(customer.getAddress())
                        .note(orderModel.getNote())
                        .status(OrderStatusEnum.WAITING.getStatus())
                        .paymentMethod(payment)
                        .build();
                orderRepository.save(orderEntity);

                //POST all order details
                List<OrderDetailModel> orderDetails = orderModel.getServices();
                List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
                for (int i = 0; i < orderDetails.size(); i++) {
                    String id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                    String formId = orderDetails.get(i).getFormId();
                    int quantity = orderDetails.get(i).getQuantity();
                    String note = orderDetails.get(i).getNote();
                    OrderDetailEntity orderDetailEntity = OrderDetailEntity.builder()
                            .orderDetailId(id)
                            .formId(formId)
                            .orderId(uuid)
                            .quantity(quantity)
                            .note(note)
                            .status(true)
                            .build();
                    orderDetailRepository.save(orderDetailEntity);
                    Optional<ServiceFormEntity> optionalServiceForm = serviceFormRepository.findByFormId(formId);
                    if (optionalServiceForm.isPresent()) {
                        ServiceFormEntity form = optionalServiceForm.get();
                        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                                .serviceName(serviceRepository.findByServiceId(form.getServiceId()).get().getServiceName())
                                .formName(form.getFormName())
                                .price(form.getPrice())
                                .quantity(quantity)
                                .note(note)
                                .build();
                        orderDetailResponses.add(orderDetailResponse);
                    } else throw new DataIntegrityViolationException("This formID is not exist");

                }
//
                OrderResponse orderResponse = OrderResponse.builder()
                        .orderId(uuid)
                        .orderName(uuid)
                        .customerId(orderEntity.getCustomerId())
                        .customerName(customer.getFullName())
                        .customerPhone(customer.getPhoneNumber())
                        .customerAddress(customer.getAddress())
                        .repairerId(null)
                        .repairerName(null)
                        .repairerPhone(null)
                        .repairerExp(null)
                        .repairerSpe(null)
                        .bookingTime(orderEntity.getBookingTime())
                        .orderDetails(orderDetailResponses)
                        .paymentMethod(payment)
                        .totalPrice(orderEntity.getTotalPrice())
                        .note(orderEntity.getNote())
                        .status(OrderStatusEnum.WAITING.getStatusVN())
                        .build();

                return orderResponse;
            } else throw new DataIntegrityViolationException("Customer have already an order");
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }


    @Override
    public List<OrderEntity> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderResponse updateOrder(String orderId, UpdateOrderModel updateOrderModel) throws Exception {
        try {
            Optional<OrderEntity> optionalOrder = orderRepository.findByOrderID(orderId);
            if (optionalOrder.isPresent()) {
                OrderEntity orderEntity = optionalOrder.get();
                String payment = "";
                if (PaymentMethodEnum.BANK_TRANSFER.getMethod().equalsIgnoreCase(updateOrderModel.getPaymentMethod())) {
                    payment = PaymentMethodEnum.BANK_TRANSFER.getMethod();
                } else if (PaymentMethodEnum.CASH.getMethod().equalsIgnoreCase(updateOrderModel.getPaymentMethod())) {
                    payment = PaymentMethodEnum.CASH.getMethod();
                } else throw new DataIntegrityViolationException("Payment method is not right format");
                orderEntity.setPaymentMethod(payment);
                List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findByOrderId(orderEntity.getOrderID()).get();
                String status = updateOrderModel.getStatus();
                if (!status.isEmpty()) {
                    if (status.equalsIgnoreCase(OrderStatusEnum.PENDING.getStatus())) {
                        String repairerId = updateOrderModel.getRepairerId();
                        List<String> statusList = new ArrayList<>();
                        statusList.add(OrderStatusEnum.COMPLETED.getStatus());
                        statusList.add(OrderStatusEnum.CANCELED.getStatus());

                        Optional<List<OrderEntity>> optional = orderRepository.findByRepairerIDAndStatusNotIn(repairerId, statusList);
                        if(optional.get().size() != 0) {
                            List<OrderEntity> orderEntities = optional.get();
                            for (int i = 0; i < orderEntities.size(); i++) {
                                Duration duration = Duration.between(orderEntities.get(i).getBookingTime(), orderEntity.getBookingTime());
                                long hours = duration.toHours();
                                if(hours > 6){
                                    orderEntity.setStatus(OrderStatusEnum.PENDING.getStatus());
                                    orderEntity.setRepairerID(repairerId);
                                    status = OrderStatusEnum.PENDING.getStatusVN();
                                    orderRepository.save(orderEntity);
                                }
                                else throw new DataIntegrityViolationException("The time difference exceeds 6 hours. Please place your order within the allowable timeframe");
                            }
                        }
                        else {
                            orderEntity.setStatus(OrderStatusEnum.PENDING.getStatus());
                            orderEntity.setRepairerID(repairerId);
                            status = OrderStatusEnum.PENDING.getStatusVN();
                            orderRepository.save(orderEntity);
                        }

                    } else if (status.equalsIgnoreCase(OrderStatusEnum.PROGRESS.getStatus())) {
                        Optional<List<OrderEntity>> optional = orderRepository.findByRepairerIDAndStatus(updateOrderModel.getRepairerId(), OrderStatusEnum.PROGRESS.getStatus());
                        if(optional.get().size() == 0)
                            orderEntity.setStatus(OrderStatusEnum.PROGRESS.getStatus());
                        else throw new DataIntegrityViolationException("Please finish all order first");
                        status = OrderStatusEnum.PROGRESS.getStatusVN();
                        orderRepository.save(orderEntity);
                    } else if (status.equalsIgnoreCase(OrderStatusEnum.PAYMENT.getStatus())) {
                        orderEntity.setStatus(OrderStatusEnum.PAYMENT.getStatus());
                        status = OrderStatusEnum.PAYMENT.getStatusVN();
                        orderRepository.save(orderEntity);
                    } else if (status.equalsIgnoreCase(OrderStatusEnum.COMPLETED.getStatus())) {
                        orderEntity.setStatus(OrderStatusEnum.COMPLETED.getStatus());
                        status = OrderStatusEnum.COMPLETED.getStatusVN();
                        orderRepository.save(orderEntity);
                    } else if (status.equalsIgnoreCase(OrderStatusEnum.CANCELED.getStatus()) && orderEntity.getStatus().equalsIgnoreCase(OrderStatusEnum.WAITING.getStatus())) {
                        orderEntity.setStatus(OrderStatusEnum.CANCELED.getStatus());
                        status = OrderStatusEnum.CANCELED.getStatusVN();
                        orderRepository.save(orderEntity);
                    } else if (status.equalsIgnoreCase(OrderStatusEnum.CANCELED.getStatus()) && orderEntity.getStatus().equalsIgnoreCase(OrderStatusEnum.PENDING.getStatus())) {
                        orderEntity.setStatus(OrderStatusEnum.WAITING.getStatus());
                        status = OrderStatusEnum.WAITING.getStatusVN();
                        orderEntity.setRepairerID(null);
                        orderRepository.save(orderEntity);

                        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                        OrderEntity copyOrder = OrderEntity.builder()
                                .orderID(uuid)
                                .orderDate(orderEntity.getOrderDate())
                                .orderName(orderEntity.getOrderName())
                                .address(orderEntity.getAddress())
                                .bookingTime(orderEntity.getBookingTime())
                                .totalPrice(orderEntity.getTotalPrice())
                                .note(orderEntity.getNote())
                                .paymentMethod(orderEntity.getPaymentMethod())
                                .customerId(orderEntity.getCustomerId())
                                .repairerID(orderEntity.getRepairerID())
                                .status(OrderStatusEnum.CANCELED.getStatus())
                                .build();
                        orderRepository.save(copyOrder);
                        for (int i = 0; i < orderDetailEntities.size(); i++) {
                            OrderDetailEntity current = orderDetailEntities.get(i);
                            String id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                            OrderDetailEntity copyDetail = OrderDetailEntity.builder()
                                    .orderDetailId(id)
                                    .formId(current.getFormId())
                                    .orderId(uuid)
                                    .quantity(current.getQuantity())
                                    .note(current.getNote())
                                    .status(true)
                                    .build();
                            orderDetailRepository.save(copyDetail);
                        }
                    } else throw new DataIntegrityViolationException("Status is a wrong format");
                }
                List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
                for (int i = 0; i < orderDetailEntities.size(); i++) {
                    String formId = orderDetailEntities.get(i).getFormId();
                    int quantity = orderDetailEntities.get(i).getQuantity();
                    String note = orderDetailEntities.get(i).getNote();
                    Optional<ServiceFormEntity> optionalServiceForm = serviceFormRepository.findByFormId(formId);
                    if (optionalServiceForm.isPresent()) {
                        ServiceFormEntity form = optionalServiceForm.get();
                        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                                .serviceName(serviceRepository.findByServiceId(form.getServiceId()).get().getServiceName())
                                .formName(form.getFormName())
                                .price(form.getPrice())
                                .quantity(quantity)
                                .note(note)
                                .build();
                        orderDetailResponses.add(orderDetailResponse);
                    }
                }
                AccountEntity repairer = AccountEntity.builder().build();
                AccountEntity customer = accountRepository.findByAccountId(orderEntity.getCustomerId()).get();

                if (orderEntity.getRepairerID() != null) {
                    repairer = accountRepository.findByAccountId(orderEntity.getRepairerID()).get();
                }
                OrderResponse orderResponse = OrderResponse.builder()
                        .orderId(orderEntity.getOrderID())
                        .orderName(orderEntity.getOrderName())
                        .customerId(orderEntity.getCustomerId())
                        .customerName(customer.getFullName())
                        .customerPhone(customer.getPhoneNumber())
                        .customerAddress(customer.getAddress())
                        .repairerId(repairer.getAccountId())
                        .repairerName(repairer.getFullName())
                        .repairerPhone(repairer.getPhoneNumber())
                        .repairerExp("" + repairer.getExperienceYear())
                        .repairerSpe(repairer.getSpecialization())
                        .bookingTime(orderEntity.getBookingTime())
                        .orderDetails(orderDetailResponses)
                        .paymentMethod(orderEntity.getPaymentMethod())
                        .totalPrice(orderEntity.getTotalPrice())
                        .note(orderEntity.getNote())
                        .status(status)
                        .build();

                return orderResponse;
            } else throw new DataIntegrityViolationException("Cannot find order " + orderId);

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    @Override
    public List<OrderEntity> getOrdersByCustomerIdAndStatus(String customerId, String status) throws Exception {
        try {
            Optional<List<OrderEntity>> orderEntities = orderRepository.findByCustomerIdAndStatus(customerId, status);
            if (orderEntities.isPresent()) {
                return orderEntities.get();
            }
            else throw new DataIntegrityViolationException("Cannot find any order");

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    @Override
    public List<OrderEntity> getOrdersByRepairerIdAndStatus(String repairerId , String status) throws Exception {
        try {
            Optional<List<OrderEntity>> orderEntities = orderRepository.findByRepairerIDAndStatus(repairerId, status);
            if (orderEntities.isPresent()) {
                return orderEntities.get();
            }
            else throw new DataIntegrityViolationException("Cannot find any order");

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    @Override
    public OrderResponse getOrderByOrderId(String orderId) throws Exception {
        try {
            Optional<OrderEntity> optionalOrder = orderRepository.findByOrderID(orderId);
            if (optionalOrder.isPresent()) {
                OrderEntity orderEntity = optionalOrder.get();
                AccountEntity customer = accountRepository.findByAccountId(orderEntity.getCustomerId()).get();
                AccountEntity repairer = AccountEntity.builder().build();
                if (orderEntity.getRepairerID() != null) {
                    repairer = accountRepository.findByAccountId(orderEntity.getRepairerID()).get();
                }
                List<OrderDetailEntity> orderDetailEntities =orderDetailRepository.findByOrderId(orderId).get();
                List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
                for (int i = 0; i < orderDetailEntities.size(); i++) {
                    String formId = orderDetailEntities.get(i).getFormId();
                    int quantity = orderDetailEntities.get(i).getQuantity();
                    String note = orderDetailEntities.get(i).getNote();
                    Optional<ServiceFormEntity> optionalServiceForm = serviceFormRepository.findByFormId(formId);
                    if (optionalServiceForm.isPresent()) {
                        ServiceFormEntity form = optionalServiceForm.get();
                        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                                .serviceName(serviceRepository.findByServiceId(form.getServiceId()).get().getServiceName())
                                .formName(form.getFormName())
                                .price(form.getPrice())
                                .quantity(quantity)
                                .note(note)
                                .build();
                        orderDetailResponses.add(orderDetailResponse);
                    }
                }
                OrderResponse orderResponse = OrderResponse.builder()
                        .orderId(orderEntity.getOrderID())
                        .orderName(orderEntity.getOrderName())
                        .customerId(orderEntity.getCustomerId())
                        .customerName(customer.getFullName())
                        .customerPhone(customer.getPhoneNumber())
                        .customerAddress(customer.getAddress())
                        .repairerId(repairer.getAccountId())
                        .repairerName(repairer.getFullName())
                        .repairerPhone(repairer.getPhoneNumber())
                        .repairerExp("" + repairer.getExperienceYear())
                        .repairerSpe(repairer.getSpecialization())
                        .bookingTime(orderEntity.getBookingTime())
                        .orderDetails(orderDetailResponses)
                        .paymentMethod(orderEntity.getPaymentMethod())
                        .totalPrice(orderEntity.getTotalPrice())
                        .note(orderEntity.getNote())
                        .status(orderEntity.getStatus())
                        .build();

                return orderResponse;
            }
            else throw new DataIntegrityViolationException("Cannot find!");

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }    }
}
