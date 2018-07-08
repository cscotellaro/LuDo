package com.example.provaH2.guess;


import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.Controller;
import com.example.provaH2.guess.db.Item;
import com.example.provaH2.guess.db.ItemRepository;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**deve tenere per forza
 *  get id
 *  getBRoad
 *  gioca ancora
 *  start game
 *
 *  count user
 * */

@Component
@VaadinSessionScope
//@Scope("prototype")
@Lazy
public class GameController extends Controller {

    @Autowired
    private ItemRepository repositoryI;
    private BroadcasterGuess broadcaster;
    private Item item;
    private ConcurrentHashMap <String, ParolaVotata> parole;
    private Random random= new Random();
    private PartitaThread partitaThread;
    private  int numUser;
    private  ArrayList<Account> accounts;
    // private Long accountId;
    //private String id;
    //private int max;

    public GameController(PartitaRepository partitaRepository){
        super(partitaRepository);
    }

    //questo metodo era solo fatto per dire che potrei fare il restart game
    //in tal caso la scelta casuale posso farla in un metodo e usarlo pure in creaPartita
    //questo metodo deve fare le operazioni che servono per iniziare una partita
    @Override
    public void giocaAncora(){
        int tot= repositoryI.numeroRighe();
        //Random random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        item =repositoryI.findOneById(rand);
    //    max=0;
        parole=new ConcurrentHashMap<>();

    }

    @Override
    public void startGame(){
        broadcaster=(BroadcasterGuess) super.getBroadcaster();
        broadcaster.startGame();
        if(partitaThread!=null){
            partitaThread.interrupt();
            partitaThread.stopTimer();
        }
        partitaThread=new PartitaThread();
        partitaThread.start();

    }

    //TODO: Tesò ma qua si deve gestire la syncro per incremento della parola?
    //in realtà non credo perchè questo viene chiamato solo da un metodo che è già syncronizzato
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
            partitaThread.stopTimer();
            partitaThread.terminaPartita();
        }
    }

    @Override
    public void countUser(int n, ArrayList<Account> accounts) {
        this.accounts=accounts;
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
        private Timer timer;
        @Override
        public void run() {
           /* int totTime=30;
            int i=0;
            for(;totTime>=0;totTime--){
                    broadcaster.countDown(totTime);
                if(totTime%5==0 &&totTime>=15 ){
                    String indizio= item.getIndizio(i);
                    broadcaster.broadcast(indizio);
                    i++;
                }
               // totTime--;
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    //terminaPartita();
                    return;
                }
            }
            terminaPartita();
*/
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                int totTime=30;
                int i=0;
                public void run() {
                   // System.out.println("time=" + totTime+ " i="+ i);
                    broadcaster.countDown(totTime);
                    if(totTime%5==0 &&totTime>=15 ){
                        String indizio= item.getIndizio(i);
                        broadcaster.broadcast(indizio);
                        i++;
                    }
                    totTime--;
                    if(totTime<0){
                        timer.cancel();
                        terminaPartita();
                    }
                }
            }, 0, 1000);


            return;
        }

        public void terminaPartita(){
            broadcaster.stopSend();
            assegnaPunteggi();
            if(parolaVincente!=null && parolaVincente.getParolaSuggerita().equals(item.getParola())){
                //broadcaster.allowJoin();
                broadcaster.fineDellaPartita(true, item.getParola());
                return;
            }else{
                //broadcaster.allowJoin();
                broadcaster.fineDellaPartita(false, item.getParola());
                return;
            }
        }

        //TODO: decidere come assegnamo i punteggi
        private void assegnaPunteggi(){
            for (Account a: accounts) {
                addPunteggio(new Punteggio(a,10));
            }
        }
        public void setParolaVincente(ParolaVotata parolaVincente) {
            this.parolaVincente = parolaVincente;
        }

        public void stopTimer(){
            timer.cancel();
        }
    }


}
