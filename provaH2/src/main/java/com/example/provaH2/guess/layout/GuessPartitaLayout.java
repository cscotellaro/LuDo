package com.example.provaH2.guess.layout;

import com.example.provaH2.UI.PlayUI;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

public class GuessPartitaLayout extends VerticalLayout implements PartitaLayout{

    //TODO: qua sarebbe più putito fare a parte layout sinistro?

    private ParoleSuggeriteLayout layoutParole;
    private VerticalLayout layoutSinistro;
    private VerticalLayout layoutIndizi;
    private ChatLayout layoutChat;
    private Label numerOfUser;
    private Label secondi;

    public GuessPartitaLayout(PlayUI puoSuggerire, Integer numberOfPlayers, HashMap<String, Embedded> playersImg){
        this.setHeight(100, Unit.PERCENTAGE);
        this.setWidth(100,Unit.PERCENTAGE);
        this.addStyleName("GuessMainLayout");
        HorizontalLayout header= new HorizontalLayout();
        header.setWidth(100, Unit.PERCENTAGE);
        header.setHeight(70, Unit.POINTS);

        Embedded img= puoSuggerire.getGiocoImg();
        Image gameImg= new Image(null, img.getSource());
        gameImg.setWidth(70, Unit.POINTS);
        gameImg.setHeight(70, Unit.POINTS);
        gameImg.addStyleName("imgGame");
        header.addComponent(gameImg);
        Label guess= new Label("Guess");
        header.addComponent(guess);
        guess.addStyleName("guessName");
        header.setComponentAlignment(guess, Alignment.MIDDLE_LEFT);
        header.setExpandRatio(guess, 1.5f);
        numerOfUser= new Label("Number of Players: " + numberOfPlayers);
        header.addComponent(numerOfUser);
        numerOfUser.addStyleName("numPlayerLabel");
        //header.setExpandRatio(secondi,2f);
        header.setComponentAlignment(numerOfUser, Alignment.MIDDLE_RIGHT);

        CssLayout body= new CssLayout();
        body.setHeight(100,Unit.PERCENTAGE);
        body.setWidth(100,Unit.PERCENTAGE);

        VerticalLayout layoutSinistro=new VerticalLayout();
        layoutSinistro.addStyleName("layoutIndizi");
        layoutSinistro.setWidth(30, Unit.PERCENTAGE);
        layoutSinistro.setHeight(100,Unit.PERCENTAGE);
        layoutSinistro.setMargin(false);
        layoutSinistro.setSpacing(false);
        Panel panelIndizi= new Panel();
        panelIndizi.setWidth(100, Unit.PERCENTAGE);
        panelIndizi.setHeight(100, Unit.PERCENTAGE);
        panelIndizi.addStyleName(ValoTheme.PANEL_BORDERLESS);
        layoutIndizi=new VerticalLayout();
        layoutIndizi.setMargin(false);
        // layoutIndizi.addStyleName("layoutIndizi");
        secondi=new Label("time:");
        secondi.addStyleName("timeLabel");
        //layoutIndizi.addComponent(secondi);
        layoutIndizi.setWidth(100, Unit.PERCENTAGE);
        panelIndizi.setContent(layoutIndizi);
        layoutSinistro.addComponents(secondi, panelIndizi);
        layoutSinistro.setExpandRatio(panelIndizi, 2.0f);

        layoutParole= new ParoleSuggeriteLayout((PuoSuggerire) puoSuggerire);
        layoutParole.addStyleName("layoutParole");
        layoutParole.setWidth(40, Unit.PERCENTAGE);


        layoutChat= new ChatLayout((PuoSuggerire) puoSuggerire, playersImg);
        layoutChat.setWidth(30, Unit.PERCENTAGE);
        //layout2.addComponent(l1);

        body.addComponents(layoutSinistro,layoutParole,layoutChat);
        this.addComponents(header,body);
        this.setExpandRatio(body, 2f);
       // body.setExpandRatio(layoutParole, 1.4f);
       // body.setExpandRatio(layoutIndizi, 1.2f);
       // body.setExpandRatio(layoutChat, 1f);


/*      System.out.println("GAMEUI: " +UI.getCurrent() + " puòsuggerire: "+ puoSuggerire );
        layoutParole= new ParoleSuggeriteLayout((PuoSuggerire) puoSuggerire);
        layoutSinistro= new VerticalLayout();
        layoutIndizi= new VerticalLayout();
        layoutChat= new ChatLayout((PuoSuggerire) puoSuggerire, playersImg);
        numerOfUser= new Label("Number of Players: " + numberOfPlayers);

        secondi= new Label("time: ");

        layoutSinistro.addComponents(numerOfUser,secondi, layoutIndizi, layoutChat);


       this.addComponents(layoutSinistro,layoutParole);

        System.out.println("la height :" + this.getHeight());
        layoutSinistro.setHeight("100%");
        layoutSinistro.setWidth(100, Unit.PERCENTAGE);
        layoutSinistro.setMargin(true);


        layoutIndizi.setHeight("100%");
        layoutIndizi.setWidth(100, Unit.PERCENTAGE);
        layoutSinistro.setExpandRatio(layoutIndizi, 1.0f);
        layoutSinistro.setExpandRatio(layoutChat, 1.8f);

       //layoutSinistro.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        //layoutParole.setHeight("100%");
        //layoutSinistro.setSizeFull();
       //layoutParole.setSizeFull();

        //addComponent(main);
        //this.setComponentAlignment(main, Alignment.MIDDLE_CENTER);


         /* TextField chatField= new TextField();
        Button send= new Button("Send", VaadinIcons.PAPERPLANE_O);
        send.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        send.addClickListener(clickEvent -> {
            if (!chatField.isEmpty()){
                puoSuggerire.sendChat(chatField.getValue());
                chatField.clear();
            }
        });
        layoutChat.addComponents(chatField, send);
       */
        //addComponent(new Label("partita layout"));
        //   addComponents(layoutSinistro, layoutParole);

       /* HorizontalLayout main= new HorizontalLayout();
        main.addComponents(layoutSinistro, layoutParole);
        //main.setSizeUndefined();
        main.setHeight("100%");
        main.setWidth(100,Unit.PERCENTAGE);
        */

    }

    public void addIndizio(String message){
        //System.out.println("Sono "+ this + " parola: " + message);
        Label indizio=new Label(message);
        indizio.addStyleName("indizi");
        indizio.setWidth(100, Unit.PERCENTAGE);
        layoutIndizi.addComponent(indizio);
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

    @Override
    public void aggiornaNumeroUtenti(int n){
        numerOfUser.setEnabled(false);
        numerOfUser.setValue("Number of Players: "+ n);
        numerOfUser.setEnabled(true);
    }

    public void reciveMessage(String name, String message){
        //layoutChat.addComponent(new Label("[" + name+ "]: " + message));
        layoutChat.riceviMessaggio(name,message);
    }

    public void countDown(int n){
        secondi.setEnabled(false);
        secondi.setValue("Time: "+ n);
        secondi.setEnabled(true);
    }

    public void finePartita(/*boolean haiVinto, List<Punteggio> punteggi, Object parola*/){
        layoutParole.disattivaBottone();
/*        Window window= new Window();
        window.center();
        window.setModal(true);
  */
    }
}
