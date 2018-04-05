package com.example.provaH2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvaEntitaRepository extends JpaRepository<provaEntita, Integer> {

    public provaEntita findOneByCognome(String c);

    public List<provaEntita> findByCognome(String c);

}
