package com.example.provaH2.gestioneGioco;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broadcaster  implements Serializable{

    public interface BroadcastListener {
        void receiveBroadcast(String message);
    }

    /*static*/ ExecutorService executorService = Executors.newSingleThreadExecutor();
    private /*static*/ LinkedList<BroadcastListener> listeners= new LinkedList<BroadcastListener>();


    public /*static*/ synchronized void register( BroadcastListener listener) {
        //System.out.println("sono il boradcaster ed è stato chiamato register "+ listeners.size());
        listeners.add(listener);
    }

    public /*static*/ synchronized void unregister( BroadcastListener listener) {
        listeners.remove(listener);
    }

    public /*static*/ synchronized void broadcast( final String message) {
        //System.out.println("sono il broadcaster ed è stato chimato bradcast" + listeners.size());

        //NOTA: questo for è senza parentesi (già so che quando lo vai a modificaer te ne scordi)
        for (final BroadcastListener listener: listeners)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.receiveBroadcast(message);
                }
            });
    }
}
