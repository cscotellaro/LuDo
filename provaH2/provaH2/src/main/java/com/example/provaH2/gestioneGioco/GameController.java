package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Item;
import com.example.provaH2.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Component
@Lazy
public class GameController implements Broadcaster.Controller{

    @Autowired
    private ItemRepository repositoryI;
    private Broadcaster broadcaster;
    private Item item;
    private HashMap <String, ParolaSuggerita> parole= new HashMap<>();
    private int max;

    public int creaPartita(){
        int tot= repositoryI.numeroRighe();
        Random random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        item =repositoryI.findOneById(rand);
        broadcaster= new Broadcaster(this);
        int n= BroadcasterList.creaBroadcaster(broadcaster);
        return n;
    }

    //questo metodo era solo fatto per dire che potrei fare il restart game
    //in tal caso la scelta casuale posso farla in un metodo e usarlo pure in creaPartita
    public void giocaAncora(){
        int tot= repositoryI.numeroRighe();
        Random random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        item =repositoryI.findOneById(rand);

        max=0;
        parole=new HashMap<>();
    }

    public void startGame(){
        broadcaster.startGame();
        new Thread(() -> {
            for(int i=0;i<4;i++){
                String indizio= item.getIndizio(i);
                broadcaster.broadcast(indizio);
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    //TODO: che ci metto qua??
                }
            }
            try {
                Thread.sleep(10000);
            }catch (InterruptedException e){
                //TODO: che ci metto qua??
            }
            broadcaster.stopGame();

            //TODO::tutto questo codice per vedere i massimi va controllato
            parole.forEach((s, parolaSuggerita) -> {
                if(parolaSuggerita.getVoti()>max){
                    max=parolaSuggerita.getVoti();
                }
            });
            ArrayList<ParolaSuggerita> massimi=new ArrayList();
            parole.forEach((s, parolaSuggerita) -> {
                if(parolaSuggerita.getVoti()==max){
                    massimi.add(parolaSuggerita);
                }
            });
            for (ParolaSuggerita parola: massimi){
                if(parola.getParola().equals(item.getParola())){
                    //TODO:hai vinto
                    System.out.println("win");
                    broadcaster.allowJoin();
                    return;
                }
            }

            //TODO: hai perso
            System.out.println("lose");
            //TODO: in realtà sia qua che sopra l allow jion lo devo mettere a vero quando il tizio decide fi dare una nuova partita
            broadcaster.allowJoin();
        }).start();
    }

    //TODO: nn so se si deve gestire la syncro per incremento della parola
    @Override
    public void aggiungiParola(String str) {
        if(parole.containsKey(str)){
            ParolaSuggerita parola=parole.get(str);
            parola.incrementaVoto();
            parole.put(str, parola);
        }else {
            parole.put(str, new ParolaSuggerita(str));
        }
    }
}
