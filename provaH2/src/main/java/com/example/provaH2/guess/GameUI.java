package com.example.provaH2.guess;

import com.example.provaH2.UI.PlayUI;
import com.example.provaH2.guess.layout.GuessPartitaLayout;
import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.BroadcasterGuess;
import com.example.provaH2.guess.GuessBroadcasterListener;
import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.vaadin.leif.headertags.Viewport;

@Push
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = PlayUI.BASE_PATH+"guess")
@PreserveOnRefresh
@Theme("darktheme")
//@VaadinServletConfiguration(productionMode = false, ui = GameUI.class,heartbeatInterval = 1)
public class GameUI extends PlayUI implements PuoSuggerire, GuessBroadcasterListener/*, CanRejoinGame*/ {

    //TODO:da togliere (e perchè?)
    //private GuessPartitaLayout partitaLayout;

    private Account account;
    private BroadcasterGuess broadcaster;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        account=getAccount();
        broadcaster= (BroadcasterGuess)getBroadcaster();
    }

    //da spostare
    @Override
    public void receiveIndizio(String message) {
        access(()-> {
           // System.out.println("Sono "+ this + " parola: " + message);
            ((GuessPartitaLayout)partitaLayout).addIndizio(message);
        });
    }

    /*public PartitaLayout instanziatePartitaLayout(){
        partitaLayout =new GuessPartitaLayout(this, broadcaster.getNumerOfPlayer());
        return  (PartitaLayout) partitaLayout;
    }
*/
    //TODO: come gestiamo il rejoin?
    //allora questo metodo lo tenevo commentato anche prima di andare a dividere GameUI e PlayUI
    //ma se lo decommento mi serve poi prendermi il waitingForPlayers
  /*  @Override
    public void rejoinGame() {
        System.out.println("rejoin game chiamato  " + this);
        broadcaster=waitingForPlayers.getBroadcaster();
        System.out.println("\t\t\t CINZIA");
        partitaLayout=new PartitaLayout(this, broadcaster.getNumerOfPlayer());
        access(() -> {
            setContent(partitaLayout);
        });
    }
*/
    //da spostare
    /**qua è quando le parole arrivano*/
    @Override
    public void parolaSuggerita(String parola) {
        access(() -> {
            ((GuessPartitaLayout)partitaLayout).parolaSuggerita(parola);
        });
    }

    //da spostare
    @Override
    public void parolaGiaSuggerita() {
        access(()->{
            Notification.show("Parola già suggerita");
        });
    }

    //da spostare
    @Override
    public void onVoteParola(String parola) {
        access(() -> {
            ((GuessPartitaLayout)partitaLayout).voteParola(parola);
        });
    }

    //da spostare
    @Override
    public void onUnvoteParola(String parola) {
        access(() -> {
            ((GuessPartitaLayout)partitaLayout).unvoteParola(parola);
        });

    }

    //da spostare
    /**qua  è quando le parole le mandi***/
    @Override
    public void suggerisci(String parola) {
        if(broadcaster!=null){
            broadcaster.suggerisciParola(parola , account.getId());
        }
    }

    //da spostare
    @Override
    public void voteParola(String parola) {
        broadcaster.voteParola(parola, account.getId());
    }

    //da spostare
    @Override
    public void unvoteParola(String parola) {
        broadcaster.unvoteParola(parola, account.getId());
    }

    /***chat****/
    //da spostare
    @Override
    public void reciveChatMessage(String name, String message) {
        access(() -> {
            if(partitaLayout!=null){
                ((GuessPartitaLayout)partitaLayout).reciveMessage(name,message);
            }
        });
    }

    //da spostare
    @Override
    public void sendChat(String message) {
        broadcaster.sendChatMessage(account.getFullName(), message);
    }


    @Override
    public void countDown(int n) {
        access(() -> {
            if(partitaLayout!=null){
                ((GuessPartitaLayout)partitaLayout).countDown(n);
            }
        });
    }

    @Override
    public void fineDellaPartita(boolean haiVinto, Object parola) {
        super.fineDellaPartita(haiVinto, parola);
        access(() -> ((GuessPartitaLayout)partitaLayout).finePartita());
    }
}