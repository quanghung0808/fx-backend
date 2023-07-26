package com.frogteam.fxhomeapi.service.implement;

import com.frogteam.fxhomeapi.entity.ServiceEntity;
import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import com.frogteam.fxhomeapi.model.ServiceFormModel;
import com.frogteam.fxhomeapi.repositories.ServiceFormRepository;
import com.frogteam.fxhomeapi.repositories.ServiceRepository;
import com.frogteam.fxhomeapi.service.ServiceFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ServiceFormServiceImpl implements ServiceFormService {
    @Autowired
    ServiceFormRepository serviceFormRepository;
    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public List<ServiceFormEntity> getAllFormsByServiceId(String serviceId) {
        return serviceFormRepository.findByServiceId(serviceId);
    }

    @Override
    public ServiceFormEntity getServiceFormByFormId(String formId) {
        return serviceFormRepository.findByFormId(formId).get();
    }

    @Override
    public ServiceFormEntity createServiceForm(ServiceFormModel serviceForm, String serviceId) throws Exception {
        try {
            String uuid = UUID.randomUUID().toString().replace("-","").substring(0,8);
            Optional<ServiceFormEntity> optionalServiceForm = serviceFormRepository
                    .findByFormNameAndServiceId(serviceForm.getFormName(), serviceId);
            if (optionalServiceForm.isPresent()) {
                throw new DataIntegrityViolationException("Duplicate form name in "
                        + serviceRepository.findByServiceId(serviceId).get().getServiceName());
            }
            Optional<ServiceEntity> optionalService = serviceRepository.findById(serviceId);
            if (optionalService.isPresent()) {
                ServiceFormEntity serviceFormEntity = ServiceFormEntity.builder()
                        .formId(uuid)
                        .formName(serviceForm.getFormName())
                        .serviceId(serviceId)
                        .price(serviceForm.getPrice())
                        .status(true)
                        .build();
                serviceFormRepository.save(serviceFormEntity);
                return serviceFormEntity;
            } else throw new DataIntegrityViolationException("No link between Service & Service Form");
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

    @Override
    public ServiceFormEntity updateServiceFormByFormId(String formId, ServiceFormModel form) throws Exception {
        try {
            Optional<ServiceFormEntity> serviceFormEntity = serviceFormRepository.findByFormId(formId);
            if (serviceFormEntity.isEmpty()) {
                throw new DataIntegrityViolationException("This form doesn't exist");
            }
            else {
                ServiceFormEntity serviceForm = serviceFormEntity.get();
                Optional<ServiceFormEntity> check = serviceFormRepository.findByFormNameAndServiceIdAndFormIdNot(form.getFormName(), serviceForm.getServiceId(), formId);
                if (check.isPresent()) {
                    throw new DataIntegrityViolationException("Duplicate form name in "
                            + serviceRepository.findByServiceId( serviceForm.getServiceId()).get().getServiceName());
                }

                serviceForm.setFormName(form.getFormName());
                serviceForm.setPrice(form.getPrice());
                serviceForm.setStatus(form.isStatus());

                serviceFormRepository.save(serviceForm);
                return serviceForm;
            }

        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }

}
