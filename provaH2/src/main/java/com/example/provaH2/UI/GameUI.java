package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.PartitaLayout;
import com.example.provaH2.UI.Layout.WaitingForPlayers;
import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.vaadin.leif.headertags.Viewport;

import java.util.ArrayList;

@Push
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = "private/gioco")
public class GameUI extends UI implements Broadcaster.BroadcastListener, PuoSuggerire {

    private Account account;
    private WaitingForPlayers waitingForPlayers;
    private PartitaLayout partitaLayout;
    private Broadcaster broadcaster;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainlayout= new VerticalLayout();
        account=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");

        waitingForPlayers= new WaitingForPlayers();
        broadcaster= waitingForPlayers.getBroadcaster();
        setContent(waitingForPlayers);
    }

    @Override
    public void receiveIndizio(String message) {
        access(()-> {
           // System.out.println("Sono "+ this + " parola: " + message);
            partitaLayout.addIndizio(message);
        });
    }

    @Override
    public void countUser(int i, ArrayList<String> nomi) {
        access(()-> {
            System.out.println("è stato chiamato il run dentro count user " +this);
            //((ContaUtenti)navigator.getCurrentView()).aggiornaCountUser(i,nomi);
            if(waitingForPlayers!=null){
                waitingForPlayers.aggiornaCountUser(i,nomi);
            }
            if(partitaLayout!=null){
                partitaLayout.aggiornaNumeroUtenti(i);
            }
        });
    }

    //TODO: questo metodo in realtà mi sa che si può anche togliere
    @Override
    public String getName() {
        return account.getFullName();
    }

    @Override
    public void gameStarted() {
        System.out.println("game started chiamato " + this);
        partitaLayout=new PartitaLayout(this, broadcaster.getNumerOfPlayer());
        access(() -> {
            setContent(partitaLayout);
        });
    }

    /**qua è quando le parole arrivano*/
    @Override
    public void parolaSuggerita(String parola) {
        access(() -> {
            partitaLayout.parolaSuggerita(parola);
        });
    }

    @Override
    public void onVoteParola(String parola) {
        access(() -> {
            partitaLayout.voteParola(parola);
        });
    }

    @Override
    public void onUnvoteParola(String parola) {
        access(() -> {
            partitaLayout.unvoteParola(parola);
        });

    }

    /**qua  è quando le parole le mandi***/
    @Override
    public void suggerisci(String parola) {
        if(broadcaster!=null){
            broadcaster.suggerisciParola(parola , account.getId());
        }
    }

    @Override
    public void voteParola(String parola) {
        broadcaster.voteParola(parola, account.getId());
    }

    @Override
    public void unvoteParola(String parola) {
        broadcaster.unvoteParola(parola, account.getId());
    }

    /***chat****/

    @Override
    public void reciveChatMessage(String name, String message) {
        access(() -> {
            if(partitaLayout!=null){
                partitaLayout.reciveMessage(name,message);
            }
        });
    }

    @Override
    public void sendChat(String message) {
        broadcaster.sendChatMessage(account.getFullName(), message);
    }

    @Override
    public void fineDellaPartita(boolean haiVinto, String parola) {
        access(() -> {
            if(haiVinto){
                Notification.show("YOU WIN: "+ parola);
            }else{
                Notification.show("YOU LOOSE: "+ parola);
            }
        });
    }

    //TODO: qua ci sta da decidere come lo vogliamo gestire
    @Override
    public void registratoDiNuovo() {

    }

    @Override
    public void detach() {
        if(broadcaster!=null){
            broadcaster.unregister(this);
        }
        super.detach();
    }
}