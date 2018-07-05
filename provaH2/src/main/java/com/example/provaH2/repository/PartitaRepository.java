package com.example.provaH2.repository;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface PartitaRepository extends JpaRepository<Partita, Long> {


    Partita findOneById(Long id);

    @Query("SELECT p FROM Partita p JOIN p.array pa WHERE pa.account = ?1 ORDER BY p.gioco, p.timestamp DESC")
    //@Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    List<Partita> cercaPartite(Account a);

    @Query("SELECT p FROM Partita p JOIN p.array pa WHERE pa.account = ?1 AND p.timestamp= (SELECT max(p1.timestamp) FROM Partita p1 JOIN p1.array pa1 WHERE pa1.account = ?1)")
    Partita lastPartita(Account a);
}
