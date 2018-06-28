package com.example.provaH2.guess;

import com.example.provaH2.guess.db.Item;
import com.example.provaH2.guess.db.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GuessProvaEvent {

    @Autowired
    private ItemRepository repositoryI;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
       System.out.println("Sono GUESS e sono stato chiamato");

        Item item= new Item("Sara");
        item.addIndizio(0,"come");
        item.addIndizio(1,"dove");
        item.addIndizio(2,"quando");
        item.addIndizio(3,"perchè");
        repositoryI.save(item);

        Item item2= new Item("Sara2");
        item2.addIndizio(0,"come2");
        item2.addIndizio(1,"dove2");
        item2.addIndizio(2,"quando2");
        item2.addIndizio(3,"perchè2");
        repositoryI.save(item2);
        repositoryI.findAll().forEach(System.out::println);

        long i= repositoryI.numeroRighe();
        //List<Item> i= repositoryI.dammiACaso();
        System.out.println(i);

    }
}
