package com.frogteam.fxhomeapi.service.implement;

import com.frogteam.fxhomeapi.entity.ServiceEntity;
import com.frogteam.fxhomeapi.model.ServiceModel;
import com.frogteam.fxhomeapi.repositories.ServiceRepository;
import com.frogteam.fxhomeapi.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public ServiceEntity getServiceById(String serviceId) {
        return serviceRepository.findByServiceId(serviceId).get();

    }

    @Override
    public ServiceEntity createService(ServiceModel service) throws Exception {
        try {
            String uuid = UUID.randomUUID().toString().replace("-","").substring(0,8);
            ServiceEntity check = serviceRepository.findByServiceName(service.getServiceName());
            if (check != null) {
                throw new DataIntegrityViolationException("Duplicate service");
            }
            ServiceEntity serviceEntity = ServiceEntity.builder()
                    .serviceId(uuid)
                    .serviceName(service.getServiceName())
                    .serviceImg(service.getServiceImg())
                    .status(true)
                    .build();
            serviceRepository.save(serviceEntity);
            return serviceEntity;
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }

    }

    @Override
    public ServiceEntity updateServiceById(String serviceId, ServiceModel updateService) throws Exception {
        try {
            ServiceEntity check = serviceRepository.findByServiceNameAndServiceIdNot(updateService.getServiceName(), serviceId);
            if (check != null) {
                throw new DataIntegrityViolationException("Duplicate service");
            }
            Optional<ServiceEntity> serviceEntity = serviceRepository.findByServiceId(serviceId);
            if (serviceEntity.isEmpty()) {
                throw new DataIntegrityViolationException("This service doesn't exist");
            }
            serviceEntity.get().setServiceName(updateService.getServiceName());
            serviceEntity.get().setServiceImg(updateService.getServiceImg());
            serviceEntity.get().setStatus(updateService.isStatus());

            serviceRepository.save(serviceEntity.get());
            return serviceEntity.get();
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Internal Server Error");
        }
    }


}
