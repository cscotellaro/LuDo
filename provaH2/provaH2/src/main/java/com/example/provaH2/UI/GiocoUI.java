package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.ParoleSuggeriteLayout;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.BroadcasterList;
import com.example.provaH2.gestioneGioco.GameController;
import com.vaadin.annotations.Push;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.vaadin.addthis.AddThis;

@Push
@SpringUI(path = "gioco")
public class GiocoUI extends UI  implements Broadcaster.BroadcastListener{

    //TODO: alla fine della partita ricordati che devi levare il creatore=true da dentro la sessione (o devo levarlo subito?)
    //TODO: ci sta il problema di che succede quando ricarichi la pagina
    private Broadcaster broadcaster;
    private Label numeroUtenti;
    private VerticalLayout layout;
    private ParoleSuggeriteLayout layoutParole;
    private HorizontalLayout mainLayout=new HorizontalLayout();
    //private HashMap<String, ParolaLayout> parole=new HashMap<>();

    //variabili solo di chi crea
    //TODO: qua si potrebe fare con un solo bottone aggiustando il codice del gamecontroller
    private Button start;
    private Button restart;
    private GameController controller;

    //variabili solo di chi joina
    private Button unjoin;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        /******QUESTO  va tolto  quando capiamo spring security************/
                Boolean logged=(Boolean) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("loggato");
        if(logged== null || logged== false){
            new Notification("Non sei loggato").show(Page.getCurrent());
            Page.getCurrent().setLocation("./Login");
        }

        Boolean creatore= (Boolean) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("creatore");

        //TODO: Cinzia che ne dici se magari estendiamo un vertical layout apposta per fare sta cosa così semplifichiamo il codice
        layout= new VerticalLayout();
        numeroUtenti= new Label("");

        /***********************controlli sul codice***********************/
        //TODO: manca il controllo sul fatto che ce ne sia effettivamente una con quel cod
        String cod=vaadinRequest.getParameter("cod");
        System.out.println("aaaaaaaaaa"+cod);
        if(cod== null) {
            layout.addComponent(new Label("nessuna partita in corso"));
            setContent(layout);
            return;
        }

        int indice;
        try{
            indice= Integer.parseInt(cod);
        }catch (NumberFormatException e){
            layout.addComponent(new Label("codice partita non valido"));
            setContent(layout);
            return;
        }

        /***************************aggiunta dei bottoni*************************/
        layout.addComponent(new Label("codice ="+indice));
        /*if(broadcaster==null){
            if(creatore!=null && creatore) {
                registra(indice);
                start= new Button("START");
                start.addClickListener(clickEvent -> {
                    broadcaster.startGame(scegli());
                    VaadinService.getCurrentRequest().getWrappedSession().setAttribute("creatore", false);
                });
                layout.addComponent(start);
            }else {
                Button join= new Button("JOIN");
                join.addClickListener(clickEvent -> {
                    registra(indice);
                    layout.removeComponent(join);
                });
                layout.addComponent(join);
            }
        }
        */

        //TODO: ma il controllo broadcaster diverso da null qua mi serve?
        //broadcaster= BroadcasterList.getBroadcaster(indice);
        controller= (GameController) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controller");
        broadcaster = BroadcasterList.getBroadcaster(indice);
        if(broadcaster.isCanJoin()){
            if(controller!= null){
                //*********************************************qua si può fare il can join
                //registra(indice);
                broadcaster.register(this);
                start= new Button("START");
                start.addClickListener(clickEvent -> {
                    controller.startGame();
                    //**********************************************questo nn lo so se ci vuole
                    VaadinService.getCurrentRequest().getWrappedSession().setAttribute("creatore", false);
                });
                layout.addComponent(start);
            } else {
                Button join = new Button("JOIN");
                join.addClickListener(clickEvent -> {
                    broadcaster.register(this);
                    //registra(indice);
                    layout.removeComponent(join);
                });
                layout.addComponent(join);
            }
        }else{
            mainLayout.addComponent(new Label("sorry you can't join this match"));
        }

        layoutParole= new ParoleSuggeriteLayout(broadcaster);
        /*layoutParole= new VerticalLayout();
        TextField parolaField= new TextField();
        Button suggerisci= new Button("Suggerisci");
        suggerisci.addClickListener(clickEvent -> {
            if (!parolaField.isEmpty()){
                broadcaster.suggerisciParola(parolaField.getValue());
                parolaField.clear();
            }
        });
        layoutParole.addComponents(parolaField,suggerisci);
        */
        //layout.addComponent(new ParolaLayout(new ParolaSuggerita("Sara")));

        layout.addComponent(numeroUtenti);
        mainLayout.addComponent(layout);
        setContent(mainLayout);
    }

    @Override
    public void receiveBroadcast(String message) {
        access(()-> {
            layout.addComponent(new Label(message));
        });
    }

    @Override
    public void countUser(int i) {
        //System.out.println("è stato chiamato il metodo count user");
        access(()-> {
            System.out.println("è stato chiamato il run dentro count user");
            //layout.removeComponent(numeroUtenti);
            numeroUtenti.setEnabled(false);
            numeroUtenti.setValue("Cinzia "+ i);
            numeroUtenti.setEnabled(true);
            //layout.addComponent(numeroUtenti);
        });
    }

    @Override
    public void gameStarted(){
        access(()-> {
            if(start!=null){
                layout.removeComponent(start);
            }
            if(restart!=null){
                layout.removeComponent(restart);
            }
            if(unjoin!=null){
                layout.removeComponent(unjoin);
            }
            mainLayout.addComponent(layoutParole);
            Notification.show("gioco iniziato");
        });
    }

    @Override
    public void parolaSuggerita(String parola) {
        access(() -> {
            layoutParole.aggiornaParole(parola);
            //layoutParole.addComponent(new Label(parola));
            /*if(parole.containsKey(parola)) {
                parole.get(parola).aggionaNumero();
            } else {
                ParolaLayout parolaLayout= new ParolaLayout(parola, broadcaster);
                layoutParole.addComponent(parolaLayout);
                parole.put(parola, parolaLayout);
            }*/
        });
    }

    //TODO: abbiamo un problema quando io chiudo il browser nn mi chiama questo metodo quindi nn mi deregistra il broadcaster
    @Override
    public void detach() {
        if(broadcaster!=null){
            broadcaster.unregister(this);
        }
        super.detach();
    }

    @Override
    public void fineDellaPartita(boolean haiVinto, String parola){
        access(() -> {
            if(haiVinto){
                Notification.show("YOU WIN: "+ parola);
            }else{
                Notification.show("YOU LOOSE: "+ parola);
            }
            //parole=new HashMap<>();
            layoutParole.ripulisci();
            //TODO: questa potrebbe essere un po' sporca
            mainLayout.removeComponent(layout);
            layout=new VerticalLayout();
            mainLayout.addComponent(layout);
            if(controller!= null){
                restart= new Button("restart");
                restart.addClickListener(clickEvent -> {
                   controller.giocaAncora();
                   controller.startGame();
                });
                layout.addComponent(restart);
            }else{
                unjoin= new Button("UNJOIN");
                unjoin.addClickListener(clickEvent -> {
                    if(broadcaster!=null){
                        broadcaster.unregister(this);
                        layout.removeComponent(unjoin);
                        Page.getCurrent().setLocation("./");
                    }
                });
                layout.addComponent(unjoin);
            }
        });
    }

   /* private void registraa(int i){
        broadcaster = BroadcasterList.getBroadcaster(i);
        broadcaster.register(this);
    }
    */
/*
    private Item scegli(){
        int tot= repositoryI.numeroRighe();
        Random random= new Random();
        int rand=random.nextInt(tot)+1;
        System.out.println("su " + tot + " è stato scelto "+ rand);
        Item itemScelto =repositoryI.findOneById(rand);
        return itemScelto;
    }
    */
}
