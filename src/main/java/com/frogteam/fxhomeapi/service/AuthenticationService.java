package com.frogteam.fxhomeapi.service;

import com.frogteam.fxhomeapi.entity.AccountEntity;

import com.frogteam.fxhomeapi.model.CustomerRegisterRequest;
import com.frogteam.fxhomeapi.model.LoginRequest;
import com.frogteam.fxhomeapi.model.RepairerRegisterRequest;
import com.frogteam.fxhomeapi.repositories.AccountRepository;
import com.frogteam.fxhomeapi.utils.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    AccountRepository accountRepository;

    public AccountEntity customerRegister(CustomerRegisterRequest request) throws Exception {
        try {
            Optional<AccountEntity> check = accountRepository.findByEmail(request.getEmail());
            if(check.isPresent()) throw new DataIntegrityViolationException("User already exist");
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            Timestamp now = DateHelper.getTimestampAtZone(DateHelper.VIETNAM_ZONE);
            AccountEntity user = AccountEntity.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(request.getPassword())
                    .roleId("jdsf3sd1")
                    .accountId(uuid)
                    .address(request.getAddress())
                    .dateOfBirth(request.getDateOfBirth())
                    .createdDate(now.toLocalDateTime())
                    .status(true)
                    .build();
            accountRepository.save(user);
            return user;
        }catch (DataIntegrityViolationException e) {
            throw e;
        }
        catch(Exception e) {
            throw new Exception("Internal Server Error");
        }
    }
    public AccountEntity repairerRegister(RepairerRegisterRequest request) throws Exception {
        try {
            Optional<AccountEntity> check = accountRepository.findByEmail(request.getEmail());
            if(check.isPresent()) throw new DataIntegrityViolationException("User already exist");
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            Timestamp now = DateHelper.getTimestampAtZone(DateHelper.VIETNAM_ZONE);
            AccountEntity user = AccountEntity.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(request.getPassword())
                    .roleId("mbgkf23i")
                    .accountId(uuid)
                    .address(request.getAddress())
                    .dateOfBirth(request.getDateOfBirth())
                    .createdDate(now.toLocalDateTime())
                    .status(true)
                    .bankingNumber(request.getBankingNumber())
                    .identityNumber(request.getIdentityNumber())
                    .specialization(request.getSpecialization())
                    .experienceYear(request.getExperienceYear())
                    .build();
            accountRepository.save(user);
            return user;
        }catch (DataIntegrityViolationException e) {
            throw e;
        }
        catch(Exception e) {
            throw new Exception("Internal Server Error");
        }
    }
    public AccountEntity login(LoginRequest request) throws Exception {
        try {
            Optional<AccountEntity> checkLogin = accountRepository.findByEmail(request.getEmail());
            if(checkLogin.isPresent()) {
                if(checkLogin.get().getPassword().equals(request.getPassword())) return checkLogin.get();
                else throw new DataIntegrityViolationException("Password is not correct");
            }
            else throw new DataIntegrityViolationException("User is not exist");
        }catch (DataIntegrityViolationException e) {
            throw e;
        }
        catch(Exception e) {
            throw new Exception("Internal Server Error");
        }
    }
}
