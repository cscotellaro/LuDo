package com.example.provaH2.UI.Layout;

import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.BroadcastListener;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.BroadcasterList;
import com.example.provaH2.gestioneGioco.Controller;
import com.example.provaH2.guess.GameController;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

import java.util.ArrayList;

//@SpringView(name = "waitingForPlayers")
public class WaitingForPlayers extends VerticalLayout /*implements View, ContaUtenti */{

    private Label waiting;
    private VerticalLayout layoutNomi;
    private Controller controller;
    private Broadcaster broadcaster;
    private Account account;
//  private CanRejoinGame gameUI;

    public WaitingForPlayers( /*CanRejoinGame gameUI*/) {

        account=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        String cod=VaadinService.getCurrentRequest().getParameter("cod");
        if(cod== null) {
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(new Label("nessuna partita  :("));
            Button goHome= new Button("go Home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/private/home");
            });
            addComponent(goHome);
            return;
        }

        //TODO rivedere la politica del can Join
        Long id=(Long) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        broadcaster = BroadcasterList.getBroadcaster(cod);
        if(broadcaster==null){
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(new Label("nessuna partita con questo codice :("));
            Button goHome= new Button("go Home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/private/home");
            });
            addComponent(goHome);
            return;
        }


        System.out.println("sto per registrarmi al broadcaster "+ UI.getCurrent());
        if(broadcaster.isCanJoin()){
            broadcaster.register(id,(BroadcastListener) UI.getCurrent());
        /*}else if(broadcaster.canIJoin(account.getId())){
            System.out.println("mi potrei joinare di nuovo");
            broadcaster.register(id,(Broadcaster.BroadcastListener) UI.getCurrent());
            gameUI.rejoinGame();
            return;
        */
        } else{
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            addComponent(new Label("la partita è già iniziata non puoi joinarti"));
            Button goHome= new Button("go Home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/");
            });
            addComponent(goHome);
            return;
        }


        waiting = new Label("I'm "+account.getFullName()+ " Waiting for players...");
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        addComponent(waiting);

        controller= (GameController) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controllerGame"+cod);
       //questo if è solo per la stampa lo puoi pure levare proprio
        if(controller!=null){
            Account account2=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
            System.out.println("Sono "+account2.getFullName()+ "controllo dello start : "+ controller.getId() + "__"+controller.getBroadcaster().getId() + " " + broadcaster.getId());
        }

        if(controller!=null && controller.getBroadcaster().getId().equals(broadcaster.getId())){
            TextField link= new TextField("copy this link");
            System.out.println("URL: " + Page.getCurrent().getLocation());
            //System.out.println("URL: " );
            //link.setValue("localhost:8080/private/gioco?cod="+cod);
            link.setValue(Page.getCurrent().getLocation().toString());
            link.setReadOnly(true);
            link.selectAll();
            addComponent(link);
            Button start= new Button("START");
            System.out.println("\tSTART sto per aggiungere il bottone e il controller è " + controller.getId());
            start.addClickListener(clickEvent -> {
                //System.out.println(controller.getId() + " b_ " + broadcaster.getId());
                controller.giocaAncora();
                controller.startGame();
            });
            addComponent(start);
        }

        Panel panelNomi= new Panel();
        layoutNomi= new VerticalLayout();
        layoutNomi.setSizeUndefined();
        panelNomi.setContent(layoutNomi);
        panelNomi.setWidth("600px");
        panelNomi.setHeight("10%");
        addComponent(panelNomi);
    }

    public void aggiornaCountUser(int n, ArrayList<String> nomi){
        System.out.println("aggiotna counter " + n + " "+ getUI());
        waiting.setEnabled(false);
        waiting.setValue("I'm "+account.getFullName()+"Waiting for users... "+ n+ "connected");
        waiting.setEnabled(true);
        layoutNomi.removeAllComponents();
        for(String str: nomi){
            layoutNomi.addComponent(new Label(str));
        }
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }
}
