package com.example.provaH2.guess;

import com.example.provaH2.guess.db.Item;
import com.example.provaH2.guess.db.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GuessPopulator {

    @Autowired
    private ItemRepository repositoryI;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
       System.out.println("Sono GUESS e sono stato chiamato");

      /*  Item item= new Item("Sara");
        item.addIndizio(0,"come come come come come come come");
        item.addIndizio(1,"dove");
        item.addIndizio(2,"quando");
        item.addIndizio(3,"perchè");
        repositoryI.save(item);

        Item item2= new Item("Sara2");
        item2.addIndizio(0,"come come come come come come 2");
        item2.addIndizio(1,"dove2");
        item2.addIndizio(2,"quando2");
        item2.addIndizio(3,"perchè2");
        repositoryI.save(item2);
        */

        Item item= new Item("tombola");
        item.addIndizio(0,"la notte di Natale");
        item.addIndizio(1,"attorno a un tavolo");
        item.addIndizio(2,"con dei fagioli in mano");
        item.addIndizio(3,"per fare la cinquina!");
        repositoryI.save(item);

        Item item1= new Item("tovaglia");
        item1.addIndizio(0,"mentre sto seduto");
        item1.addIndizio(1,"stesa danti a me");
        item1.addIndizio(2,"sbiadita dagli anni");
        item1.addIndizio(3,"perchè per apparecchiare uso sempre la stessa");
        repositoryI.save(item1);

        Item item2= new Item("vetro");
        item2.addIndizio(0,"un pomeriggio di maggio");
        item2.addIndizio(1,"a casa");
        item2.addIndizio(2,"trasparente come sempre");
        item2.addIndizio(3,"perchè un ottuso moscone continua a sbatterci contro");
        repositoryI.save(item2);

        Item item3= new Item("statua della libertà");
        item3.addIndizio(0,"dopo un lungo viaggio da Parigi");
        item3.addIndizio(1,"a New York");
        item3.addIndizio(2,"con le mani entrambe occupate");
        item3.addIndizio(3,"per reggere la fiaccola più famosa del mondo");
        repositoryI.save(item3);

        Item item4= new Item("carota");
        item4.addIndizio(0,"dopo una grande nevicata");
        item4.addIndizio(1,"in mezzo a due bottoni");
        item4.addIndizio(2,"arancione");
        item4.addIndizio(3,"per fare il naso al pupazzo di neve");
        repositoryI.save(item4);

        Item item5= new Item("Pasqua");
        item5.addIndizio(0,"una domnica di primavera");
        item5.addIndizio(1,"lontano da casa");
        item5.addIndizio(2,"con chi vuoi");
        item5.addIndizio(3,"perchè non è come il Natale che lo passi con i tuoi");
        repositoryI.save(item5);

        Item item6= new Item("camionista");
        item6.addIndizio(0,"dieci ore al giorno");
        item6.addIndizio(1,"seduto in cabina");
        item6.addIndizio(2,"con un braccio abbronzato");
        item6.addIndizio(3,"per trasportare il cemento da ROma  Milano");
        repositoryI.save(item6);


        repositoryI.findAll().forEach(System.out::println);

        long i= repositoryI.numeroRighe();
        //List<Item> i= repositoryI.dammiACaso();
        System.out.println(i);

    }
}
