package com.example.provaH2.guess;

import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.Controller;

/**Da questa classe posso iìusare la variabile listeners e executorService
 *
 * */

public class BroadcasterGuess extends Broadcaster {

    private boolean canSend;
    private GameController gameController;

    public BroadcasterGuess(Controller controller, String id) {
        super(controller, id);
        canSend=false;
        gameController= (GameController) getGameController();
    }

    public synchronized void broadcast( final String message) {
        if(canSend) {
            //System.out.println("Sono "+ this + " parola: " + message);
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(()-> {
                    ((GuessBroadcasterListener)broadcastListener).receiveIndizio(message);
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
                        ((GuessBroadcasterListener)broadcastListener).parolaSuggerita(parola);
                    });
                });
            }else{
                executorService.execute(()-> {
                    ((GuessBroadcasterListener)listeners.get(accountId)).parolaGiaSuggerita();
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
                    ((GuessBroadcasterListener)broadcastListener).onVoteParola(parola);
                });
            });
        }
    }

    public synchronized void unvoteParola(String parola, Long accountId) {
        if (canSend) {
            gameController.unVoteParola(parola, accountId);
            listeners.forEach((aLong, broadcastListener) -> {
                executorService.execute(() -> {
                    ((GuessBroadcasterListener)broadcastListener).onUnvoteParola(parola);
                });
            });
        }
    }

    public synchronized void sendChatMessage(String name, String message){
        listeners.forEach((aLong, broadcastListener) -> {
            executorService.execute(()-> {
                ((GuessBroadcasterListener)broadcastListener).reciveChatMessage(name, message);
            });
        });
    }

    public synchronized void startGame(/*Item myItem*/){
        canSend=true;
        super.startGame();
    }

    public void stopSend(){
        canSend=false;
    }


}
