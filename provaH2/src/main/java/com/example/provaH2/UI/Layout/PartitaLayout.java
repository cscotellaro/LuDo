package com.example.provaH2.UI.Layout;

import com.example.provaH2.UI.PuoSuggerire;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.vaadin.ui.*;

public class PartitaLayout extends HorizontalLayout {

    //TODO: qua sarebbe più putito fare a parte layout sinistro?

    private ParoleSuggeriteLayout layoutParole;
    private VerticalLayout layoutSinistro;
    private VerticalLayout layoutIndizi;
    private VerticalLayout layoutChat;
    private Label numerOfUser;

    public PartitaLayout(PuoSuggerire puoSuggerire, int numberOfPlayers){
        layoutParole= new ParoleSuggeriteLayout(puoSuggerire);
        layoutSinistro= new VerticalLayout();
        layoutIndizi= new VerticalLayout();
        layoutChat= new VerticalLayout();
        numerOfUser= new Label("Number of Players: " + numberOfPlayers);

        layoutSinistro.addComponents(numerOfUser, layoutIndizi, layoutChat);
        TextField chatField= new TextField();
        Button send= new Button("Send");
        send.addClickListener(clickEvent -> {
            if (!chatField.isEmpty()){
                puoSuggerire.sendChat(chatField.getValue());
                chatField.clear();
            }
        });
        layoutChat.addComponents(chatField, send);
        // /addComponent(new Label("partita layout"));
        addComponents(layoutSinistro, layoutParole);
    }

    public void addIndizio(String message){
        //System.out.println("Sono "+ this + " parola: " + message);
        layoutIndizi.addComponent(new Label(message));
    }

    public void parolaSuggerita(String parola){
        layoutParole.aggiungiParola(parola);
    }

    public void voteParola(String parola){
        layoutParole.voteParola(parola);
    }

    public void unvoteParola (String parola){
        layoutParole.unvoteParola(parola);
    }

    public void aggiornaNumeroUtenti(int n){
        numerOfUser.setEnabled(false);
        numerOfUser.setValue("Number of Players: "+ n);
        numerOfUser.setEnabled(true);
    }

    public void reciveMessage(String name, String message){
        layoutChat.addComponent(new Label("[" + name+ "]: " + message));
    }
}
