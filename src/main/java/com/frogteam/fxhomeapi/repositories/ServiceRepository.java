package com.frogteam.fxhomeapi.repositories;

import com.frogteam.fxhomeapi.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ServiceRepository  extends JpaRepository<ServiceEntity,String> {
    public ServiceEntity findByServiceName(String serviceName);
    public Optional<ServiceEntity> findByServiceId(String serviceId);
    public ServiceEntity findByServiceNameAndServiceIdNot(String serviceName, String serviceId);

}
