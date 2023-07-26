package com.frogteam.fxhomeapi.repositories;

import com.frogteam.fxhomeapi.entity.ServiceEntity;
import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceFormRepository extends JpaRepository<ServiceFormEntity,String> {
    public Optional<ServiceFormEntity> findByFormNameAndServiceId(String formName, String serviceId);
    public Optional<ServiceFormEntity> findByFormIdAndServiceId(String formId, String serviceId);

    public Optional<ServiceFormEntity> findByFormId(String formId);

    public List<ServiceFormEntity> findByServiceId(String serviceId);

    public Optional<ServiceFormEntity> findByFormNameAndServiceIdAndFormIdNot(String formName, String serviceId, String formId);

}
