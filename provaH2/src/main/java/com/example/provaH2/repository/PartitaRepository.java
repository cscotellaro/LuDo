package com.example.provaH2.repository;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartitaRepository extends JpaRepository<Partita, Long> {


    Partita findOneById(Long id);

    @Query("SELECT p FROM Partita p JOIN p.array pa WHERE pa.account = ?1")
    List<Partita> cercaPartite(Account a);
}
