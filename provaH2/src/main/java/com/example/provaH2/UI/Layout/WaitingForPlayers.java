package com.example.provaH2.UI.Layout;

import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.BroadcastListener;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.BroadcasterList;
import com.example.provaH2.gestioneGioco.Controller;
import com.example.provaH2.guess.GameController;
import com.romeosa.copytoclipboard.CopyToClipboard;
import com.romeosa.copytoclipboard.CopyToClipboardButton;
import com.vaadin.addon.responsive.Responsive;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.HashMap;

//@SpringView(name = "waitingForPlayers")
public class WaitingForPlayers extends VerticalLayout /*implements View, ContaUtenti */{

    private Label numPlayers;
    private VerticalLayout layoutNomi;
    private Controller controller;
    private Broadcaster broadcaster;
    private Account account;
//  private CanRejoinGame gameUI;

    public WaitingForPlayers( /*CanRejoinGame gameUI*/) {
        this.addStyleName("WFPverticalMainLayout");
        /**************************************controlli che ci sia una partita******************************************/
        account=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        String cod=VaadinService.getCurrentRequest().getParameter("cod");
        if(cod== null) {
            this.setHeight(100, Unit.PERCENTAGE);
            this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Label error= new Label("nessuna partita  :(");
            error.addStyleName("WFPerror");
            //addComponent(error);
            Button goHome= new Button("go Home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/private/home");
            });
            goHome.addStyleName("Home");
            VerticalLayout middle= new VerticalLayout();
            middle.addStyleName("WFPLayoutError");
            middle.addComponents(error,goHome);
            addComponent(middle);

            //addComponents(error,goHome);
           return;
        }

        //TODO rivedere la politica del can Join
        Long id=(Long) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        broadcaster = BroadcasterList.getBroadcaster(cod);
        if(broadcaster==null){
            this.setHeight(100, Unit.PERCENTAGE);
            this.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            //setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Label error= new Label("no match with this code :(");
            //addComponent(error);
            Button goHome= new Button("home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/private/home");
            });
            goHome.addStyleName("Home");
            error.addStyleName("WFPerror");
            VerticalLayout middle= new VerticalLayout();
            middle.addComponents(error,goHome);
            middle.addStyleName("WFPLayoutError");

            //addComponent(goHome);
            addComponent(middle);

            //addComponents(error,goHome);
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
            this.setHeight(100, Unit.PERCENTAGE);
            this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Label error=new Label("Sorry, you can't join this round");
            //setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            //addComponent(error);
            Button goHome= new Button("home");
            goHome.addClickListener(clickEvent -> {
                Page.getCurrent().setLocation("/");
            });
            goHome.addStyleName("home");
            error.addStyleName("WFPerror");
            VerticalLayout middle= new VerticalLayout();
            middle.addComponents(error,goHome);
            middle.addStyleName("WFPLayoutError");
            addComponent(middle);

            //addComponents(error,goHome);
           return;
        }

        /*******************************************setting del layout**************************************/
        this.setHeight(100, Unit.PERCENTAGE);
        CssLayout mainlayout= new CssLayout();
        mainlayout.setHeight(100, Unit.PERCENTAGE);
        mainlayout.setWidth(100, Unit.PERCENTAGE);
        new Responsive(mainlayout);
      //  Panel mainPanel= new Panel();

        //setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout gameLayout= new VerticalLayout();
        gameLayout.addStyleName("WFPLayoutSx");
        //gameLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        HorizontalLayout logoEImg= new HorizontalLayout();
        logoEImg.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Embedded embeddedImg= broadcaster.getGameImg();
        Label gameName=new Label(broadcaster.getGame().getNomeGioco());
        gameName.addStyleName("WFPGameName");
        Image img= new Image(null, embeddedImg.getSource());
        img.setWidth(100,Unit.POINTS);
        img.setHeight(100,Unit.POINTS);
        img.addStyleName("imgGame");
        logoEImg.addComponents(img, gameName);

        Label descr=new Label(broadcaster.getGame().getDescrizioneLungaGioco());
        descr.setWidth(100, Unit.PERCENTAGE);
        descr.setContentMode(ContentMode.HTML);
        gameLayout.addComponents(logoEImg,descr);
        mainlayout.addComponent(gameLayout);
       // mainlayout.setComponentAlignment(gameLayout, Alignment.TOP_RIGHT);

        VerticalLayout layoutDestro= new VerticalLayout();
        layoutDestro.addStyleName("WFPLayoutDx");
        //layoutDestro.setHeight(100, Unit.PERCENTAGE);
        CssLayout WFPLayout=new CssLayout();
        WFPLayout.setWidth(100, Unit.PERCENTAGE);
        //WFPLayout.setMargin(false);
        Label waiting = new Label( " Waiting for players...");
        waiting.addStyleName("WFPLabel");
        WFPLayout.addComponent(waiting);
        numPlayers= new Label("0 connected");
        numPlayers.addStyleName("WFPNumPLayers");
        WFPLayout.addComponent(numPlayers);
        //WFPLayout.setComponentAlignment(numPlayers, Alignment.MIDDLE_RIGHT);
        layoutDestro.addComponent(WFPLayout);

        Panel panelNomi= new Panel();
        layoutNomi= new VerticalLayout();
        layoutNomi.setSizeUndefined();
        panelNomi.setContent(layoutNomi);
       // panelNomi.setWidth(450, Unit.POINTS);
        panelNomi.setHeight(350, Unit.POINTS);
        panelNomi.addStyleName("WFPPanelNomi");
        layoutDestro.addComponent(panelNomi);
        layoutDestro.addStyleName("WFPDestro");

        layoutDestro.setExpandRatio(panelNomi, 2f);

        controller= (Controller) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controllerGame"+cod);
        //questo if è solo per la stampa lo puoi pure levare proprio
        if(controller!=null){
            Account account2=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
            System.out.println("Sono "+account2.getFullName()+ "controllo dello start : "+ controller.getId() + "__"+controller.getBroadcaster().getId() + " " + broadcaster.getId());
        }

        if(controller!=null && controller.getBroadcaster().getId().equals(broadcaster.getId())){
            VerticalLayout formLayout=new VerticalLayout();
            formLayout.addStyleName("WFPLayoutLink");
            formLayout.setWidth(100, Unit.PERCENTAGE);
            formLayout.setMargin(false);
            formLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
            TextField link= new TextField("share with link");
            link.setWidth(100, Unit.PERCENTAGE);
            System.out.println("URL: " + Page.getCurrent().getLocation());
            //System.out.println("URL: " );
            //link.setValue("localhost:8080/private/gioco?cod="+cod);
            link.setValue(Page.getCurrent().getLocation().toString());
            link.setReadOnly(true);
            link.selectAll();
            formLayout.addComponent(link);


/*            Button marco= new Button("copy");
           // final CopyToClipboardButton cp= new CopyToClipboardButton();
            //Window w= new Window();
            //addComponent(cp);
            String string = "soReadyToHelp = function myFunction(p) {"
                    +"alert(p);"
                    +"var copyText= document.getElementById(\"gwt-uid-3\");"
                    +"copyText.select();"
                    +"copyText.readOnly=false;"
                    +"document.execCommand(\"copy\");"
                    +"}";
            JavaScript.getCurrent().execute(string);

            marco.addClickListener(clickEvent -> {
               link.selectAll();

                JavaScript.getCurrent().execute("soReadyToHelp('Hello');");
                //Page.getCurrent().getJavaScript().execute(string);


              //  cp.setClipboardText("Marco");
            });
            formLayout.addComponent(marco);
         */
            gameLayout.addComponent(formLayout);

            Button start= new Button("START");
            start.addStyleName("WFPstart");
            System.out.println("\tSTART sto per aggiungere il bottone e il controller è " + controller.getId());
            start.addClickListener(clickEvent -> {
                //System.out.println(controller.getId() + " b_ " + broadcaster.getId());
                controller.giocaAncora();
                controller.hostGame();
            });
            start.setWidth(120, Unit.POINTS);
            start.setHeight(40, Unit.POINTS);
            layoutDestro.addComponent(start);
            layoutDestro.setComponentAlignment(start, Alignment.MIDDLE_CENTER);
        }
        mainlayout.addComponent(layoutDestro);
      //  mainlayout.setExpandRatio(gameLayout,2f);
       // mainlayout.setExpandRatio(layoutDestro, 2f);
      //  mainPanel.setContent(mainlayout);
     //   mainPanel.setHeight(100, Unit.PERCENTAGE);
       // mainPanel.setWidth(100,Unit.PERCENTAGE);
        //addComponent(mainPanel);
       new Responsive(mainlayout);
        addComponent(mainlayout);
    }

    public void aggiornaCountUser(int n, HashMap<String, Embedded> accountImg){
        System.out.println("aggiotna counter " + n + " "+ getUI());
        numPlayers.setEnabled(false);
        numPlayers.setValue( n+ "connected");
        numPlayers.setEnabled(true);
        layoutNomi.removeAllComponents();

        for(int i=0; i<15;i++){
            accountImg.forEach((s, embedded) -> {
                HorizontalLayout player= new HorizontalLayout();
                player.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
                Image profileImg=new Image(null, embedded.getSource());
                profileImg.addStyleName("WFPplayerImg");
                //profileImg.setWidth(30, Unit.POINTS);
                //profileImg.setHeight(30,Unit.POINTS);
                player.addComponent(profileImg);
                Label playerName= new Label(s);
                playerName.addStyleName("WFPPlayerName");
                player.addComponent(playerName);
                layoutNomi.addComponent(player);
            });


        }
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }
}
