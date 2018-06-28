package com.example.provaH2.gestioneGioco;

import com.example.provaH2.guess.BroadcasterGuess;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract  class Controller {
    private Long accountId;
    private Broadcaster broadcaster;
    private Game game;
    //metodo chiamato da Broadcaster
    public abstract void countUser(int n);

    //metodi chiamati da waiting for players

    public  abstract  void giocaAncora();
    public  abstract  void startGame();

    //questo verrà chiamato dal bottone crea partita quando faremo il bottone vero
    //Crea un broadcaster con un intero univoco
    public String creaPartita(Game game){
        this.game=game;
        String broadcasterId;
        accountId= (Long) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        String timeStamp = new SimpleDateFormat("dd.HH.mm.ss").format(new Date());
        broadcasterId= accountId+"_"+ timeStamp;
        //id=broadcasterId;
        System.out.println("un controller viene chiamato crea partitia "+ broadcasterId );
        try {
            broadcaster=game.getBroadcasterClass().getConstructor(Controller.class, String.class).newInstance(this, broadcasterId);
            //broadcaster= (Broadcaster) broadcasterClass.getConstructors()[0].newInstance(this,broadcasterId);
            //broadcaster = broadcasterClass.getDeclaredConstructor(broadcasterClass).newInstance(this, broadcasterId);
            BroadcasterList.creaBroadcaster(broadcaster);
        }catch (Exception e){
            e.printStackTrace();
        }

        return broadcasterId;
    }

    public Broadcaster getBroadcaster(){
        System.out.println(" sono nel controller  il mio accountId è "+ accountId+"get broadcaster BROADCASTER= "+ broadcaster.getId());
        return  broadcaster;
    }

    public String getId(){
        //return id;
        return  broadcaster.getId();
    }

    public Game getGame(){
        return game;
    }
}
