package com.example.provaH2.gestioneGioco;

import java.util.ArrayList;

public class BroadcasterList {

    private static ArrayList<Broadcaster> lista= new ArrayList<>();

    //TODO: questa cosa non va bene
    //nn so se i metodi devono essree syncronized
    public static synchronized int creaBroadcaster(){
        lista.add(new Broadcaster());
        return lista.size()-1;
    }

    public static Broadcaster getBroadcaster(int i){
        return lista.get(i);
    }

    public static synchronized void rimuovi(int i){
        lista.remove(i);
    }

}
