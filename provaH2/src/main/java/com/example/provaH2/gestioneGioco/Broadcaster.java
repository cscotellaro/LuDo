package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.apache.tomcat.jni.Thread;


public class Broadcaster implements Serializable{

    //TODO: tesoro ma queste due interfacce le dovremmo levare da qua dentro?
    public interface BroadcastListener {
        void receiveIndizio(String message);
        void countUser(int i, ArrayList<String> utenti);
        void gameStarted();
        void parolaSuggerita(String parola);
        void onVoteParola(String parola);
        void onUnvoteParola(String parola);
        void fineDellaPartita(boolean haiVinto, String parola);
        void registratoDiNuovo();
        void reciveChatMessage(String name, String message);
        void parolaGiaSuggerita();
        String getName();
    }

    public interface Controller{
        boolean aggiungiParola(String parola, Long accountId);
        void voteParola(String parola, Long accountId);
        void unVoteParola(String parola, Long accountId);
        void countUser(int n);
    }

    ExecutorService executorService;
    //private /*static*/ LinkedList<BroadcastListener> listeners;
    private HashMap<Long, BroadcastListener> listeners;
    private boolean canJoin;
    private boolean canSend;
    private Controller gameController;
    private String id;

    public Broadcaster(Controller controller , String id){
        this.gameController=controller;
        this.id= id;
        canJoin=true;
        canSend=false;
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
            gameController.countUser(listeners.size());

            ArrayList<String> nomi= new ArrayList<>();
            listeners.forEach((aLong, broadcastListener) -> {
                nomi.add(broadcastListener.getName());
            });

            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    broadcastListener.countUser(listeners.size(), nomi);
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
            gameController.countUser(listeners.size());

            ArrayList<String> nomi = new ArrayList<>();
            listeners.forEach((aLong, broadcastListener) -> {
                nomi.add(broadcastListener.getName());
            });

            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(() -> {
                    broadcastListener.countUser(listeners.size(), nomi);
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

    public synchronized void broadcast( final String message) {
        if(canSend) {
            //System.out.println("Sono "+ this + " parola: " + message);
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    broadcastListener.receiveIndizio(message);
                });
            });
            /*for (final BroadcastListener listener : listeners) {
                executorService.execute(() -> {
                    listener.receiveBroadcast(message);
                });
            }*/
        }
    }

    public synchronized void suggerisciParola(String parola, Long accountId){
       if(canSend) {
           if(gameController.aggiungiParola(parola, accountId)){
               listeners.forEach((aLong, broadcastListener) -> {
                   executorService.execute(()-> {
                       broadcastListener.parolaSuggerita(parola);
                   });
               });
           }else{
               executorService.execute(()-> {
                   listeners.get(accountId).parolaGiaSuggerita();
               });
           }

           /*for (final BroadcastListener listener : listeners) {
               executorService.execute(() -> {
                   listener.parolaSuggerita(parola);
               });
           }*/
       }
    }

    public synchronized void voteParola(String parola, Long accountId) {
        if (canSend) {
            gameController.voteParola(parola, accountId);
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(() -> {
                    broadcastListener.onVoteParola(parola);
                });
            });
        }
    }

    public synchronized void unvoteParola(String parola, Long accountId) {
        if (canSend) {
            gameController.unVoteParola(parola, accountId);
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(() -> {
                    broadcastListener.onUnvoteParola(parola);
                });
            });
        }
    }


    public synchronized void startGame(/*Item myItem*/){
        //itemDellaUi=myItem;
        canJoin= false;
        canSend=true;
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                broadcastListener.gameStarted();
            });
        });
        /*for (final BroadcastListener listener: listeners) {
            executorService.execute(() ->{
                listener.gameStarted();
            });
        }*/

    }

    public void comunicaEsito(boolean haiVinto, String parola){
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

    public synchronized void sendChatMessage(String name, String message){
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                broadcastListener.reciveChatMessage(name, message);
            });
        });
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

    public void stopSend(){
        canSend=false;
    }

    public void allowJoin(){
        canJoin=true;
    }

    public String getId(){
        return id;
    }

    public int getNumerOfPlayer(){
        return listeners.size();
    }
}
