package com.frogteam.fxhomeapi.service;

import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import com.frogteam.fxhomeapi.model.ServiceFormModel;

import java.util.List;

public interface ServiceFormService {
    List<ServiceFormEntity> getAllFormsByServiceId(String serviceId);

    ServiceFormEntity getServiceFormByFormId(String formId);
    ServiceFormEntity createServiceForm( ServiceFormModel serviceForm, String serviceId) throws Exception;

    ServiceFormEntity updateServiceFormByFormId(String formId, ServiceFormModel form) throws Exception;

}
