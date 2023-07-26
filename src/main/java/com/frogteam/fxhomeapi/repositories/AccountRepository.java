package com.frogteam.fxhomeapi.repositories;

import com.frogteam.fxhomeapi.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository  extends JpaRepository<AccountEntity,String> {
    public Optional<AccountEntity> findByAccountId(String accountId);

    public Optional<AccountEntity> findByEmail(String email);
}
