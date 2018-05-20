package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Item;
import com.example.provaH2.repository.ItemRepository;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
@VaadinSessionScope
@Lazy
public class GameController implements Broadcaster.Controller{

    @Autowired
    private ItemRepository repositoryI;
    private Broadcaster broadcaster;
    private String id;
    private Item item;
    private ConcurrentHashMap <String, ParolaVotata> parole;
    //private int max;
    private Random random= new Random();
    private PartitaThread partitaThread;
    private Long accountId;
    private  int numUser;

    public String creaPartita(){
        String broadcasterId;
        accountId= (Long)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        String timeStamp = new SimpleDateFormat("dd.HH.mm.ss").format(new Date());
        broadcasterId= accountId+"_"+ timeStamp;
        id=broadcasterId;
        System.out.println("un controller viene chiamato crea partitia "+ broadcasterId + "__" + id);

        broadcaster= new Broadcaster(this, broadcasterId);
        BroadcasterList.creaBroadcaster(broadcaster);
        return broadcasterId;
    }

    public String getId(){
        return id;
    }

    //questo metodo era solo fatto per dire che potrei fare il restart game
    //in tal caso la scelta casuale posso farla in un metodo e usarlo pure in creaPartita
    public void giocaAncora(){
        int tot= repositoryI.numeroRighe();
        //Random random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        item =repositoryI.findOneById(rand);

    //    max=0;
        parole=new ConcurrentHashMap<>();
    }

    public void startGame(){
        broadcaster.startGame();
        partitaThread=new PartitaThread();
        partitaThread.start();

        /*new Thread(() -> {

            for(int i=0;i<4;i++){
                String indizio= item.getIndizio(i);
                broadcaster.broadcast(indizio);
                try {
                    Thread.sleep(5000);
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
            //qui in realtà ho fatto come se ci potessero stare più massimi, ma  non lo so se va bene
            parole.forEach((s, parolaVotata) -> {
                if(parolaVotata.getNumeroVoti()>max){
                    max=parolaVotata.getNumeroVoti();
                }
            });
            ArrayList<ParolaVotata> massimi=new ArrayList();
            parole.forEach((s, parolaVotata) -> {
                if(parolaVotata.getNumeroVoti()==max){
                    massimi.add(parolaVotata);
                }
            });
            for (ParolaVotata parola: massimi){
                if(parola.getParolaSuggerita().equals(item.getParola())){
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
        */
    }

    //TODO: Tesò ma qua si deve gestire la syncro per incremento della parola?
    //in realtà non credo perchè questo viene chiamato solo da un metodo che è già syncronizzato
    @Override
    public boolean aggiungiParola(String str, Long accountId) {
        try{
            if(parole.containsKey(str)){
                return false;
            }else{
                parole.put(str, new ParolaVotata(str, accountId));
                return  true;
            }
      }catch (NullPointerException e){
          parole.put(str, new ParolaVotata(str, accountId));
          return  true;
      }

    }

    @Override
    public void voteParola(String parola, Long accountId) {
        try{
            if(!parole.containsKey(parola)){
                return;
            }else{
                parole.get(parola).addVoto(accountId);
                cercaParolaVotataDaTutti(numUser);
            }
        }catch (NullPointerException e){
            return;
        }
    }

    @Override
    public void unVoteParola(String parola, Long accountId) {
        try{
            if(!parole.containsKey(parola)){
                return;
            }else {
                parole.get(parola).removeVoto(accountId);
            }
        }catch (NullPointerException e){
            return;
        }
    }

    public Broadcaster getBroadcaster(){
        System.out.println(" sono nel controller  il mio accountId è "+ accountId+"get broadcaster BROADCASTER= "+ broadcaster.getId());
        return broadcaster;
    }

    private void cercaParolaVotataDaTutti(int numUtenti){
        ParolaVotata parolaVincente = parole.search(1, (s, parolaVotata) -> {
            if(parolaVotata.getNumeroVoti()==numUtenti){
                return parolaVotata;
            }else{
                return null;
            }
        });
        if(parolaVincente!=null){
            partitaThread.setParolaVincente(parolaVincente);
            partitaThread.interrupt();
        }
    }

    @Override
    public void countUser(int n) {
        //TODO: Cinzia ma qua quando qualcuno se ne va facciamo che controllo se gli altri sono tutti d'accordo?
        if(n<numUser){
            numUser=n;
            cercaParolaVotataDaTutti(numUser);
        }else {
            numUser=n;
        }
    }

    private class PartitaThread extends Thread{
        private ParolaVotata parolaVincente;

        @Override
        public void run() {
            for(int i=0;i<4;i++){
                String indizio= item.getIndizio(i);
                broadcaster.broadcast(indizio);
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    //TODO:non è che mi faccia impazzire sta cosa eh
                    terminaPartita();
                    return;
                }
            }

            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                //TODO: che ci metto qua??
                terminaPartita();
                return;
            }

            terminaPartita();
            return;
        }

        private void terminaPartita(){
            broadcaster.stopSend();
            if(parolaVincente!=null && parolaVincente.getParolaSuggerita().equals(item.getParola())){
                //broadcaster.allowJoin();
                broadcaster.comunicaEsito(true, item.getParola());
                return;
            }else{
                //broadcaster.allowJoin();
                broadcaster.comunicaEsito(false, item.getParola());
                return;
            }
        }

        public void setParolaVincente(ParolaVotata parolaVincente) {
            this.parolaVincente = parolaVincente;
        }
    }
}
