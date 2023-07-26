package com.frogteam.fxhomeapi.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.frogteam.fxhomeapi.entity.ServiceEntity;
import com.frogteam.fxhomeapi.entity.ServiceFormEntity;
import com.frogteam.fxhomeapi.model.ResponseDTO;
import com.frogteam.fxhomeapi.model.ServiceFormModel;
import com.frogteam.fxhomeapi.model.ServiceModel;
import com.frogteam.fxhomeapi.service.ServiceFormService;
import com.frogteam.fxhomeapi.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServiceFormService serviceFormService;

    @PostMapping(path = "services")
    public ResponseEntity<ResponseDTO> createService(@RequestBody(required = false) ServiceModel service) {
        try {
            if (service.getServiceName().isEmpty()) {
                ResponseDTO response = ResponseDTO.badRequest();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            ServiceEntity serviceModel = serviceService.createService(service);
            ResponseDTO response = ResponseDTO.success();
            response.setData(serviceModel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "services")
    public ResponseEntity<ResponseDTO> getService() {
        try {
            List<ServiceEntity> services = serviceService.getAllServices();
            ResponseDTO response = ResponseDTO.success();
            response.setData(services);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "services/{service-id}")
    public ResponseEntity<ResponseDTO> getServiceById( @PathVariable("service-id") String serviceId) {
        try {
            ServiceEntity service = serviceService.getServiceById(serviceId);
            ResponseDTO response = ResponseDTO.success();
            response.setData(service);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "services/{service-id}")
    public ResponseEntity<ResponseDTO> updateService(@RequestBody(required = false) ServiceModel serviceModel, @PathVariable("service-id") String serviceId) {
        try {
            ServiceEntity service = serviceService.updateServiceById(serviceId, serviceModel);
            ResponseDTO response = ResponseDTO.success();
            response.setData(service);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.internalServerError();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "services/{service-id}/forms")
    public ResponseEntity<ResponseDTO> createServiceForm(@RequestBody(required = false) ServiceFormModel serviceForm, @PathVariable("service-id") String serviceId) {
        try {
            if (serviceForm.getFormName().isEmpty() || serviceForm.getPrice() == 0 || serviceId == null) {
                ResponseDTO response = ResponseDTO.badRequest();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            ServiceFormEntity serviceFormEntity = serviceFormService.createServiceForm(serviceForm, serviceId);
            ResponseDTO response = ResponseDTO.success();
            response.setData(serviceFormEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "services/{service-id}/forms")
    public ResponseEntity<ResponseDTO> getFormByServiceId(@PathVariable("service-id") String serviceId) {
        try {
            List<ServiceFormEntity> listForms = serviceFormService.getAllFormsByServiceId(serviceId);
            ResponseDTO response = ResponseDTO.success();
            response.setData(listForms);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/forms/{form-id}")
    public ResponseEntity<ResponseDTO> updateServiceForm(@RequestBody(required = false) ServiceFormModel formModel, @PathVariable("form-id") String formId) {
        try {
            ServiceFormEntity form = serviceFormService.updateServiceFormByFormId(formId, formModel);
            ResponseDTO response = ResponseDTO.success();
            response.setData(form);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.internalServerError();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "forms/{form-id}")
    public ResponseEntity<ResponseDTO> getFormById(@PathVariable("form-id") String formId) {
        try {
            ServiceFormEntity form = serviceFormService.getServiceFormByFormId(formId);
            ResponseDTO response = ResponseDTO.success();
            response.setData(form);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}