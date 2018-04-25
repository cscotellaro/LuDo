package com.example.provaH2.repository;

import com.example.provaH2.entity.Item;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Lazy
public interface ItemRepository extends JpaRepository<Item, Integer> {
    //@Query("SELECT i FROM Item i ORDER BY random()")
    //public List<Item> dammiACaso();
    @Query("SELECT count(*) FROM Item i")
    int numeroRighe();

    Item findOneById(int id);
}
