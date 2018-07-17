package com.example.provaH2.gestioneGioco;

import java.util.ArrayList;
import java.util.Hashtable;

public class BroadcasterList {

    //private static ArrayList<Broadcaster> lista= new ArrayList<>();

    private static Hashtable<String, Broadcaster> collection= new Hashtable<>();

    //TODO: si dovrebbe rinominare il metodo
    //nn so se i metodi devono essree syncronized
    //se uso la table l add è syncro di suo
    public static /*synchronized*/ void creaBroadcaster(Broadcaster b){
        //lista.add(new Broadcaster());
        //lista.add(b);
        //return lista.size()-1;
        collection.put(b.getId(),b);
    }

/*    public static Broadcaster getBroadcaster(int i){
        return lista.get(i);
    }

    public static synchronized void rimuovi(int i){
        lista.remove(i);
    }
*/

    public static Broadcaster getBroadcaster(String id){
        return  collection.get(id);
    }

    public static void  rimuovi(String id){
        collection.remove(id);
        System.out.println("rimuovi broadcaster chiamato");
    }
}
