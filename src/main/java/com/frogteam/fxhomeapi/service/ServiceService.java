package com.frogteam.fxhomeapi.service;

import com.frogteam.fxhomeapi.entity.ServiceEntity;
import com.frogteam.fxhomeapi.model.ServiceModel;

import java.util.List;

public interface ServiceService {
    List<ServiceEntity> getAllServices();

    ServiceEntity getServiceById(String serviceId);

    ServiceEntity createService(ServiceModel service) throws Exception;

    ServiceEntity updateServiceById(String serviceId, ServiceModel updateService) throws Exception;
}
