package com.frogteam.fxhomeapi.controller;

import com.frogteam.fxhomeapi.entity.AccountEntity;
import com.frogteam.fxhomeapi.model.*;
import com.frogteam.fxhomeapi.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {
    @Autowired
    AuthenticationService service;

    @PostMapping("/customer-register")
    public ResponseEntity<ResponseDTO> customerRegister(@RequestBody CustomerRegisterRequest request) {
        try {
            AccountEntity customer = service.customerRegister(request);
            ResponseDTO response = ResponseDTO.success();
            response.setData(customer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/repairer-register")
    public ResponseEntity<ResponseDTO> repairerRegister(@RequestBody RepairerRegisterRequest request) {
        try {
            AccountEntity repairer = service.repairerRegister(request);
            ResponseDTO response = ResponseDTO.success();
            response.setData(repairer);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login( @RequestBody LoginRequest request) {
        try {
            AccountEntity login = service.login(request);
            ResponseDTO response = ResponseDTO.success();
            response.setData(login);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            ResponseDTO response = ResponseDTO.badRequestCustom(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ResponseDTO response = ResponseDTO.badRequest();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }    }



}
