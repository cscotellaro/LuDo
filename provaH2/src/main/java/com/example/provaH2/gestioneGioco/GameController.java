package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Item;
import com.example.provaH2.repository.ItemRepository;
import com.vaadin.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@Component
@Lazy
public class GameController implements Broadcaster.Controller{

    @Autowired
    private ItemRepository repositoryI;
    private Broadcaster broadcaster;
    private Item item;
    private HashMap <String, ParolaSuggerita> parole;
    private int max;
    private Random random= new Random();

    public String creaPartita(){
        /*int tot= repositoryI.numeroRighe();
        random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        item =repositoryI.findOneById(rand);
        */

        String broadcasterId;
        //TODO: Cinzia ma qua non mi serve il cotrollo su null, vero? E per l'id?
        /*nel senso qua per arrivarci devo essere loggata e poi l'id v a bene così oppure magari mettiamo la colonna
        * id in account e mettiamo unique sul fullName. Ah e poi la granularità del timestamp va bene??*/
        Long accountId= (Long)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        String timeStamp = new SimpleDateFormat("dd.HH.mm.ss").format(new Date());
        broadcasterId= accountId+"_"+ timeStamp;
        broadcaster= new Broadcaster(this, broadcasterId);
        BroadcasterList.creaBroadcaster(broadcaster);
        return broadcasterId;
    }

    //questo metodo era solo fatto per dire che potrei fare il restart game
    //in tal caso la scelta casuale posso farla in un metodo e usarlo pure in creaPartita
    public void giocaAncora(){
        int tot= repositoryI.numeroRighe();
        //Random random= new Random();
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
                Thread.sleep(5000);
            }catch (InterruptedException e){
                //TODO: che ci metto qua??
            }
            broadcaster.stopSend();

            //TODO: Cinzia tutto questo codice per vedere i massimi va controllato
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
                    System.out.println("win");
                    //broadcaster.allowJoin();
                    broadcaster.comunicaEsito(true, item.getParola());
                    return;
                }
            }

            System.out.println("lose");
            //TODO: in realtà sia qua che sopra l allow jion lo devo mettere a vero quando il tizio decide di fare una nuova partita
            //alla fine l ho messo dentro al broadcaster dopo aver comunicato gli esiti ti fa fare il join di nuovo
            //broadcaster.allowJoin();
            broadcaster.comunicaEsito(false, item.getParola());
        }).start();
    }

    //TODO: Tesò ma qua si deve gestire la syncro per incremento della parola?
    //in realtà non credo perchè questo viene chiamato solo da un metodo che è già syncronizzato
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
