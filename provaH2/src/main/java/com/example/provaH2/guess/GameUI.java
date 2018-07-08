package com.example.provaH2.guess;

import com.example.provaH2.UI.PlayUI;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.guess.layout.GuessPartitaLayout;
import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.BroadcasterGuess;
import com.example.provaH2.guess.GuessBroadcasterListener;
import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.leif.headertags.Viewport;

import java.util.HashMap;
import java.util.List;

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
    private Window window;

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
    public void gameStarted(Class<? extends PartitaLayout> partitaClass, int numPlayers, HashMap<String, Embedded> playersImg) {
        super.gameStarted(partitaClass, numPlayers, playersImg);
        access(() -> {
            if(window!=null){
                window.close();
            }
        });
    }

    @Override
    public void fineDellaPartita(boolean haiVinto, List<Punteggio> punteggi, Object parola) {
        access(() -> {
            ((GuessPartitaLayout)partitaLayout).finePartita();
            window= new Window();
            window.setModal(true);
            window.center();

            VerticalLayout mainlayout= new VerticalLayout();
            mainlayout.setWidth(100,Unit.PERCENTAGE);
            Label parolaLabel= new Label("the word is: "+(String)parola);
            parolaLabel.setWidth(100,Unit.PERCENTAGE);
            parolaLabel.addStyleName("parolaDaIndovinare");
            mainlayout.addComponent(parolaLabel);


            Panel panel= new Panel();
            panel.setHeight(300, Unit.POINTS);
            panel.setWidth(100,Unit.PERCENTAGE);
            panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
            VerticalLayout layout=new VerticalLayout();
            layout.setWidth(100,Unit.PERCENTAGE);
            panel.setContent(layout);
            mainlayout.addComponent(panel);
            for(int i=0; i<10; i++){
                punteggi.forEach(punteggio -> {
                    HorizontalLayout hl= new HorizontalLayout();
                    hl.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
                    hl.addStyleName("HLPalyerPoints");
                    hl.setWidth(100,Unit.PERCENTAGE);
                    Image image= new Image(null, immaginiPlayer.get(punteggio.getAccount().getFullName()).getSource());
                    image.setWidth(25, Unit.POINTS);
                    image.setHeight(25,Unit.POINTS);
                    image.addStyleName("pallinoChat");
                    hl.addComponent(image);
                    Label nome= new Label(punteggio.getAccount().getFullName());
                    nome.setWidth(100,Unit.PERCENTAGE);
                    hl.addComponent(nome);
                    hl.addComponent(new Label(punteggio.getPunti()+""));

                    hl.setExpandRatio(nome,1.3f);
                    layout.addComponent(hl);
                });
            }

            System.out.println("Sono "+ account.getFullName()+ " host:"+ areYouHost());
            Button home= new Button("home");
            home.addClickListener(clickEvent -> {
                broadcaster.unregister(account.getId(), this);
                Page.getCurrent().setLocation("http://localhost:8080/private/home");
            });
            HorizontalLayout foot=new HorizontalLayout();
            foot.setWidth(100, Unit.PERCENTAGE);
            if(areYouHost()){
                Button restart= new Button("Restart");
                foot.addComponent(restart);
                foot.setComponentAlignment(restart, Alignment.TOP_LEFT);
                restart.addClickListener(clickEvent -> {
                    //window.close();
                    restartGame();
                });
            }
            foot.addComponent(home);
            foot.setComponentAlignment(home, Alignment.TOP_RIGHT);

            mainlayout.addComponent(foot);
            window.setWidth(450, Unit.POINTS);
            window.setResizable(false);
            window.setClosable(false);
            window.addStyleName("translucent");
            window.setContent(mainlayout);
            addWindow(window);
        });
    }
}