package com.example.provaH2.repository;


import com.example.provaH2.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findOneByEmail(String email);

    boolean existsByFullName(String fullName);

    Account findOneById(Long id);


    @Transactional
    @Modifying
    @Query("update Account a set a.password = ?2 where a.id = ?1")
    void updatePassword(Long id, String newPassword);
}
