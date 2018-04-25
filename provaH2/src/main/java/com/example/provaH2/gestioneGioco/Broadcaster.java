package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Item;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.apache.tomcat.jni.Thread;

/*@Component
@Lazy*/
public class Broadcaster implements Serializable{
    /*@Autowired
    private ItemRepository repositoryI;

    @Autowired
    private void setItemRepository(ItemRepository repositoryI){
        this.repositoryI=repositoryI;
        System.err.println("sono nel setItemRepo di un broadcaster");

    }*/

    public interface BroadcastListener {
        void receiveBroadcast(String message);
        void countUser(int i);
        void gameStarted();
        void parolaSuggerita(String parola);
        void fineDellaPartita(boolean haiVinto, String parola);
    }

    public interface Controller{
        void aggiungiParola(String parola);
    }

    /*static*/ ExecutorService executorService;
    private /*static*/ LinkedList<BroadcastListener> listeners;
    private boolean canJoin;
    private boolean canSend;
    private Controller gameController;
    //private Item itemDellaUi;

    public Broadcaster(Controller controller){
        this.gameController=controller;
        canJoin=true;
        canSend=false;
        listeners= new LinkedList<BroadcastListener>();
        executorService = Executors.newSingleThreadExecutor();
    }


    //TODO: devo fare che non mi posso registrare due volte
    public /*static*/ synchronized void register( BroadcastListener listener) {
        //System.out.println("sono il boradcaster ed è stato chiamato register "+ listeners.size());
        if(canJoin){
            listeners.add(listener);
            for (final BroadcastListener listen: listeners) {
                executorService.execute(()-> {
                    listen.countUser(listeners.size());
                });
            }
        }
    }

    public /*static*/ synchronized void unregister( BroadcastListener listener) {
        listeners.remove(listener);
        for (final BroadcastListener listen: listeners) {
            executorService.execute(()-> {
                listen.countUser(listeners.size());
            });
        }
        if(listeners.size()==0){
            //TODO: qua dovrei chiamare BroadcasterList.remove(i) ma nn ho i. E poi non posso perchè mi smerda gli indici
            //ma il controllo su cajoin
        }
    }

    public /*static*/ synchronized void broadcast( final String message) {
        if(canSend) {
            for (final BroadcastListener listener : listeners) {
                executorService.execute(() -> {
                    listener.receiveBroadcast(message);
                });
            }
        }
    }

    public synchronized void suggerisciParola(String parola){
        //TODO: Cinzia vedi che qua io nn faccio che tu nn puoi mandare la parola tu la mandi e il broadcaster nn ti caca
        //secondo te è meglio così o è meglio se facciamo che tu fai il controllo se la puoi mandare o no direttamente in gamUI
       if(canSend) {
           gameController.aggiungiParola(parola);
           for (final BroadcastListener listener : listeners) {
               executorService.execute(() -> {
                   listener.parolaSuggerita(parola);
               });
           }
       }
    }

    public synchronized void startGame(/*Item myItem*/){
        //itemDellaUi=myItem;
        canJoin= false;
        canSend=true;
        for (final BroadcastListener listener: listeners) {
            executorService.execute(() ->{
                listener.gameStarted();
            });
        }
        /*new Thread(() -> {
            for(int i=0;i<4;i++){
                String indizio= itemDellaUi.getIndizio(i);
                broadcast(indizio);
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    //TODO: che ci metto qua??
                }
            }
        }).start();
        */
    }

    public void comunicaEsito(boolean haiVinto, String parola){
        for (final BroadcastListener listener: listeners) {
            executorService.execute(() ->{
                listener.fineDellaPartita(haiVinto,parola);
            });
        }
        allowJoin();
    }

    public boolean isCanJoin(){
        return canJoin;
    }

    public void stopSend(){
        canSend=false;
    }

    public void allowJoin(){
        canJoin=true;
    }
}
