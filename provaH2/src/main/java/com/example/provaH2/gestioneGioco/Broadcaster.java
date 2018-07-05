package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Account;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.apache.tomcat.jni.Thread;

/** deve tenere
 *  isCanJoin
 *  register
 *  get Id
 */

public class Broadcaster implements Serializable{

  /*  public interface BroadcastListener {
       // void receiveIndizio(String message);
        void countUser(int i, ArrayList<String> utenti);
        void gameStarted();
       // void parolaSuggerita(String parola);
       // void onVoteParola(String parola);
       // void onUnvoteParola(String parola);
        void fineDellaPartita(boolean haiVinto, Object parola);
        void registratoDiNuovo();
       // void reciveChatMessage(String name, String message);
        // void parolaGiaSuggerita();
       String getName();
    }
*/
    /*public interface Controller{
       // boolean aggiungiParola(String parola, Long accountId);
       // void voteParola(String parola, Long accountId);
       // void unVoteParola(String parola, Long accountId);

    }*/

    protected ExecutorService executorService;
    protected HashMap<Long, BroadcastListener> listeners;
    private boolean canJoin;
    protected Controller gameController;
    private String id;

    //private /*static*/ LinkedList<BroadcastListener> listeners;

    public Broadcaster(Controller controller , String id){
        this.gameController=controller;
        this.id= id;
        canJoin=true;
        listeners= new HashMap<>();
        executorService = Executors.newSingleThreadExecutor();
    }

    //DONE:devo fare che non mi posso registrare due volte
    public synchronized void register( Long id, BroadcastListener listener) {
        System.out.println("sono il boradcaster ed è stato chiamato register "+ listeners.size()+ "  ui:"+ listener);
        if(canJoin){
            if(listeners.containsKey(id) && (BroadcastListener)listeners.get(id)!=listener){
                listeners.get(id).registratoDiNuovo();
                System.out.println("di nuovo");
            }
            listeners.put(id, listener);

            ArrayList<String> nomi= new ArrayList<>();
            ArrayList<Account> accounts= new ArrayList<>();
            listeners.forEach((aLong, broadcastListener) -> {
                Account a= broadcastListener.getAccount();
                accounts.add(a);
                nomi.add(a.getFullName());
            });
            gameController.countUser(listeners.size(), accounts);

            HashMap<String, Embedded> accountImg= new HashMap<>();
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    accountImg.put(broadcastListener.getName(), broadcastListener.getProfileImage());
                });
            });
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    broadcastListener.countUser(listeners.size(), accountImg);
                });
            });
            /*for (final BroadcastListener listen: listeners) {
                executorService.execute(()-> {
                    listen.countUser(listeners.size());
                });
            }
            */
        }
    }

    public  synchronized void unregister(Long accountId, BroadcastListener listener) {
        System.out.println("sono il boradcaster ed è stato chiamato UNregister "+ listeners.size() + "  ui:"+ listener );
        if(listeners.containsValue(listener)) {
            listeners.remove(accountId);

            ArrayList<String> nomi= new ArrayList<>();
            ArrayList<Account> accounts= new ArrayList<>();
            listeners.forEach((aLong, broadcastListener) -> {
                Account a= broadcastListener.getAccount();
                accounts.add(a);
                nomi.add(a.getFullName());
            });
            gameController.countUser(listeners.size(), accounts);

            HashMap<String, Embedded> accountImg= new HashMap<>();
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    accountImg.put(broadcastListener.getName(), broadcastListener.getProfileImage());
                });
            });

            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(() -> {
                    broadcastListener.countUser(listeners.size(), accountImg);
                });
            });
        }
        /*for (final BroadcastListener listen: listeners) {
            executorService.execute(()-> {
                listen.countUser(listeners.size());
            });
        }*/
        if(listeners.size()==0){
            //ma il controllo su cajoin
            BroadcasterList.rimuovi(id);
        }
    }


    public synchronized void startGame(/*Item myItem*/){
        //itemDellaUi=myItem;
        canJoin= false;
      //  canSend=true;
        HashMap<String, Embedded> accountImg= new HashMap<>();
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                accountImg.put(broadcastListener.getName(), broadcastListener.getProfileImage());
            });
        });
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                broadcastListener.gameStarted(gameController.getGame().getParitaLayoutClass(),listeners.size(),accountImg);
            });
        });
        /*for (final BroadcastListener listener: listeners) {
            executorService.execute(() ->{
                listener.gameStarted();
            });
        }*/

    }

    public void fineDellaPartita(boolean haiVinto, Object parola){
        gameController.savePartita();
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                broadcastListener.fineDellaPartita(haiVinto, parola);
            });
        });
        /*for (final BroadcastListener listener: listeners) {
            executorService.execute(() ->{
                listener.fineDellaPartita(haiVinto,parola);
            });
        }*/
        allowJoin();
    }


    public boolean isCanJoin(){
        return canJoin;
    }

    public  boolean canIJoin(Long id){
        if(!canJoin && listeners.containsKey(id)){
            return true;
        }else{
            return false;
        }
    }

    //TODO dove lo uso
    public void allowJoin(){
        canJoin=true;
    }

    public String getId(){
        return id;
    }

    public int getNumerOfPlayer(){
        return listeners.size();
    }

    public Controller getGameController() {
        return gameController;
    }

    public Embedded getGameImg(){
       return gameController.getGameImage();
    }

    public Game getGame(){
        return gameController.getGame();
    }
}
