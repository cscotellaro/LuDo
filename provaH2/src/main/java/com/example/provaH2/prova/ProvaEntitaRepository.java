package com.example.provaH2.prova;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvaEntitaRepository extends JpaRepository<provaEntita, Integer> {

    public provaEntita findOneByCognome(String c);

    public List<provaEntita> findByCognome(String c);

}
