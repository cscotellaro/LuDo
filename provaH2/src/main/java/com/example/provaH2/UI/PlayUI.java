package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.WaitingForPlayers;
import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.BroadcastListener;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.Controller;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.BroadcasterGuess;
import com.example.provaH2.guess.GameController;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PlayUI extends UI  implements BroadcastListener{
    public static final String BASE_PATH="private/";

    private Embedded img;
    private Account account;
    private Broadcaster broadcaster;
    private WaitingForPlayers waitingForPlayers;
    protected PartitaLayout partitaLayout;
    protected  HashMap<String, Embedded> immaginiPlayer;
    private Class<?extends PartitaLayout> partitaClass;
    private String cod;
    private Controller controller;
    //DONE: quando spossti i metodi rimetti qua boradcaster
    /*farei un metodo get Broadcaster perchè la sottoclasse se vuole il broadcaster se lo piglia
     * */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout mainlayout= new VerticalLayout();
        account=(Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        img=(Embedded) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountImg");
        cod=VaadinService.getCurrentRequest().getParameter("cod");
        controller= (Controller) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controllerGame"+cod);

        waitingForPlayers= new WaitingForPlayers(/*this*/);
        //  waitingForPlayers.inizializza();

        broadcaster= (Broadcaster) waitingForPlayers.getBroadcaster();
        Panel panel= new Panel();
        panel.setWidth(100,Unit.PERCENTAGE);
        panel.addStyleName("MainPanelWFP");
      //  panel.setHeight(100, Unit.PERCENTAGE);
        panel.setContent(waitingForPlayers);
        //setContent(waitingForPlayers);
        setContent(panel);
        //getUI().setPollInterval(1000);
        addDetachListener((event)-> {
            System.out.println("Sono nel LISTENER del detach....");
        });



/*        JavaScript.getCurrent().addFunction("aboutToClose", new JavaScriptFunction() {
            @Override
            public void call(org.json.JSONArray arguments) throws org.json.JSONException {
                System.out.println("Window/Tab is Closed.");
                //TODO Call Method to Clean the Resource before window/Tab Close.
            }
        });
  */
        JavaScript.getCurrent().addFunction("aboutToClose",(array)->{
            System.out.println("Window/Tab is Closed.");
            if(areYouHost()){
                broadcaster.hostLost();
            }

            if(broadcaster!=null){
                broadcaster.unregister(account.getId(),this);
            }

        });

        //Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function (e[code]) { var e = (e || window.event); aboutToClose(); return; };");
        Page.getCurrent().getJavaScript().execute("window.onbeforeunload = function () { aboutToClose(); return; };");

        String hb=getSession().getConfiguration().getInitParameters().getProperty("heartbeatInterval");
        System.out.println("heartbeat : " +hb);
        getSession().getConfiguration().getInitParameters().setProperty("heartbeatInterval", "1");
        hb=getSession().getConfiguration().getInitParameters().getProperty("heartbeatInterval");
        System.out.println("heartbeat : " +hb);
    }


    //TODO: io l'ho commentato ma nn so perchè lo tenevo
    /*public WaitingForPlayers getWaitingForPlayers() {
        return waitingForPlayers;
    }
*/

    //TODO: questo metodo in realtà mi sa che si può anche togliere (NO lo leveremo quando leviamo la sol cessa del count)
    @Override
    public String getName() {
        return account.getFullName();
    }

    //da spostare
    @Override
    public void gameStarted(Class<? extends  PartitaLayout> partitaClass,int numPlayers, HashMap<String ,Embedded> playersImg) {
        System.out.println("game started chiamato " + this);
        //partitaLayout=new PartitaLayout(this, broadcaster.getNumerOfPlayer());
       // partitaLayout=instanziatePartitaLayout();
        immaginiPlayer=playersImg;
        try {
            partitaLayout = partitaClass.getConstructor(PlayUI.class, Integer.class, HashMap.class).newInstance(this, numPlayers, playersImg);
            access(() -> {
                setContent(partitaLayout);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //da vedere
    @Override
    public void countUser(int i,HashMap<String, Embedded> accountImg) {
        access(()-> {
            System.out.println("è stato chiamato il run dentro count user " +this);
            //((ContaUtenti)navigator.getCurrentView()).aggiornaCountUser(i,nomi);
            if(waitingForPlayers!=null){
                waitingForPlayers.aggiornaCountUser(i, accountImg);
            }
            if(partitaLayout!=null){
                partitaLayout.aggiornaNumeroUtenti(i);
            }
        });
    }


    //TODO: cambia questo fatto della notifica ti prego
    @Override
    public abstract void fineDellaPartita(boolean haiVinto, List<Punteggio> punteggi, Object parola); /*{
        System.out.println("heartbeat :" + getLastHeartbeatTimestamp());
        access(() -> {
            //partitaLayout.finePartita(haiVinto,punteggi,parola);
            if(haiVinto){
                Notification.show("YOU WIN: "+ parola);
            }else{
                Notification.show("YOU LOOSE: "+ parola);


        });
    }*/

    @Override
    public void hostLost() {
        access(() -> {
            Window w=new Window();
            w.setModal(true);
            w.setClosable(false);
            w.setResizable(false);
            w.center();
            w.setHeight(150,Unit.POINTS);
            w.setWidth(200, Unit.POINTS);
            VerticalLayout vl=new VerticalLayout();
            vl.setSpacing(true);
            vl.setMargin(true);
            vl.setSizeFull();
            vl.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            vl.addComponent(new Label("Host lost"));
            Button home=new Button("Home");
            home.addClickListener(clickEvent ->Page.getCurrent().setLocation("/private/home") );
            vl.addComponent(home);
            w.setContent(vl);

            addWindow(w);
        });
    }

    @Override
    public Embedded getProfileImage(){
        return img;
    }

    @Override
    public Account getAccount(){
        return  account;
    }


    public Embedded getGiocoImg() {
        return broadcaster.getGameImg();
    }

    //da vedere
    //TODO: qua ci sta da decidere come lo vogliamo gestire
    @Override
    public void registratoDiNuovo() {

    }

    //va bene
    @Override
    public void detach() {
        System.out.println("sono nel metodo detach....");

        if(areYouHost()){
            broadcaster.hostLost();
        }
        if(broadcaster!=null){
            broadcaster.unregister(account.getId(),this);
        }
        super.detach();
    }

    protected Broadcaster getBroadcaster(){
        return  broadcaster;
    }

    protected PartitaLayout getPartitaLayout(){
        return  partitaLayout;
    }

    protected HashMap<String, Embedded> getImmaginiPlayer(){
        return immaginiPlayer;
    }

    protected  boolean areYouHost(){
       // Controller controller= (Controller) VaadinSession.getCurrent().getAttribute("controllerGame"+cod);
        System.out.println("controller "  + controller);
        if(controller!=null && controller.getBroadcaster().getId().equals(broadcaster.getId())){
            return true;
        }else{
            return false;
        }
    }

    protected void restartGame(){
      //  Controller controller= (Controller) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controllerGame"+cod);

        if(controller!=null && controller.getBroadcaster().getId().equals(broadcaster.getId())){
           controller.rigioca();
        }
    }
    //public abstract PartitaLayout instanziatePartitaLayout();
}
