package com.example.provaH2.repository;


import com.example.provaH2.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    public Account findOneByEmail(String email);
}
