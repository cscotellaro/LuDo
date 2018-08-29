package com.example.provaH2.prova;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.layout.ParoleSuggeriteLayout;
import com.example.provaH2.gestioneGioco.BroadcastListener;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.BroadcasterList;
import com.example.provaH2.guess.GameController;
import com.vaadin.annotations.Push;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.vaadin.leif.headertags.Viewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Push
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = "private/giocoOLD")
public class GiocoUI extends UI  implements BroadcastListener {

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
    private GameController controller;

    //variabili solo di chi joina
    private Button unjoin;

    @Override
    public void hostLost() {

    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        //******QUESTO  va tolto  quando capiamo spring security************/
        /*Boolean logged=(Boolean) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("loggato");
        if(logged== null || logged== false){
            new Notification("Non sei loggato").show(Page.getCurrent());
            Page.getCurrent().setLocation("/Login");
        }
*/      //Boolean creatore= (Boolean) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("creatore");


        //VaadinService.getCurrentRequest().getWrappedSession().setMaxInactiveInterval(10);
        //System.out.println("interval "+ VaadinService.getCurrentRequest().getWrappedSession().getMaxInactiveInterval());

        //TODO: Cinzia che ne dici se magari estendiamo un vertical layout apposta per fare sta cosa così semplifichiamo il codice
        layout= new VerticalLayout();
        numeroUtenti= new Label("");

        /***********************controlli sul parametro codice***********************/
        //DONE: manca il controllo sul fatto che ce ne sia effettivamente una con quel cod
        String cod=vaadinRequest.getParameter("cod");
        if(cod== null) {
            layout.addComponent(new Label("nessuna partita "));
            setContent(layout);
            return;
        }

        Long id=(Long) getSession().getSession().getAttribute("accountId");
        if(id==null){
            layout.addComponent(new Label("qualcosa non va....chi sei??"));
            setContent(layout);
            return;
        }
        /*int indice;
        try{
            indice= Integer.parseInt(cod);
        }catch (NumberFormatException e){
            layout.addComponent(new Label("codice partita non valido"));
            setContent(layout);
            return;
        }
*/
        layout.addComponent(new Label("codice ="+cod));

        /***************************aggiunta dei bottoni*************************/
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

        //broadcaster= BroadcasterList.getBroadcaster(indice);
        controller= (GameController) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("controller");
        broadcaster = BroadcasterList.getBroadcaster(cod);
        if(broadcaster==null){
            layout.addComponent(new Label("Mmmmmmm nessuna partita con questo codice"));
            setContent(layout);
            return;
        }
        if(broadcaster.isCanJoin()){
            if(controller!= null){
                //*********************************************qua si può fare il can join
                //registra(indice);
                broadcaster.register(id,this);
                start= new Button("START");
                start.addClickListener(clickEvent -> {
                    controller.giocaAncora();
                    controller.hostGame();
                    //VaadinService.getCurrentRequest().getWrappedSession().setAttribute("creatore", false);
                });
                layout.addComponent(start);
            } else {
                Button join = new Button("JOIN");
                join.addClickListener(clickEvent -> {
                    //broadcaster.register(id,this);
                    //registra(indice);
                    layout.removeComponent(join);
                });
                layout.addComponent(join);
            }
        }else{
            layout.addComponent(new Label("sorry you can't join this match"));
        }

       //
        //
        // layoutParole= new ParoleSuggeriteLayout(broadcaster);



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
        //setPollInterval(3000);
    }

    //@Override
    public void receiveIndizio(String message) {
        access(()-> {
            layout.addComponent(new Label(message));
        });
    }

    //@Override
    public void countUser(int i, HashMap<String, Embedded> nomi) {
        //System.out.println("è stato chiamato il metodo count user");
        access(()-> {
            System.out.println("è stato chiamato il run dentro count user");
            numeroUtenti.setEnabled(false);
            numeroUtenti.setValue("Cinzia "+ i);
            numeroUtenti.setEnabled(true);
        });
    }

    @Override
    public void fineDellaPartita(boolean haiVinto, List<Punteggio> punteggi, Object parola) {

    }

    // @Override
    public void gameStarted(Class<? extends PartitaLayout> classe, int n,HashMap<String , Embedded> acc){
        try{
            getSession().getSession().setMaxInactiveInterval(10);
            //VaadinService.getCurrentRequest().getWrappedSession().setMaxInactiveInterval(10);
            System.out.println(getSession().getSession().getMaxInactiveInterval());
        }catch (Exception e){
            System.out.println("eccezione nel cambiare l'interval della session");
            e.printStackTrace();
        }

        access(()-> {
            layoutParole.ripulisci();
            //TODO: questa potrebbe essere un po' sporca
            mainLayout.removeComponent(layout);
            layout=new VerticalLayout();
            mainLayout.addComponent(layout);
            if(start!=null){
                layout.removeComponent(start);
            }
            if(unjoin!=null){
                layout.removeComponent(unjoin);
            }
            mainLayout.addComponent(layoutParole);
            Notification.show("gioco iniziato");
        });
    }

   // @Override
    public void parolaSuggerita(String parola) {
        access(() -> {
           //
            //
            //
            // layoutParole.aggiornaParole(parola);

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

    public Embedded getProfileImage(){
        return null;
    }
   // @Override
    public void reciveChatMessage(String name, String message) {

    }

    //TODO: abbiamo un problema quando io chiudo il browser lo chiama dopo un sacco di tempo
    @Override
    public void detach() {
        if(broadcaster!=null){
         //   broadcaster.unregister(this);
        }
        //TODO: questo non so se ci vuole
        getSession().getSession().setMaxInactiveInterval(1800);
        super.detach();
    }

   // @Override
    public void fineDellaPartita(boolean haiVinto, Object parola){
        //TODO: e se tengo più partite?
        getSession().getSession().setMaxInactiveInterval(1800);
        access(() -> {
            if(haiVinto){
                Notification.show("YOU WIN: "+ parola);
            }else{
                Notification.show("YOU LOOSE: "+ parola);
            }
            //parole=new HashMap<>();
         /*   layoutParole.ripulisci();
            //______________: questa potrebbe essere un po' sporca
            mainLayout.removeComponent(layout);
            layout=new VerticalLayout();
            mainLayout.addComponent(layout);
           */ if(controller!= null){
                layout.addComponent(start);
            }else{
                unjoin= new Button("UNJOIN");
                unjoin.addClickListener(clickEvent -> {
                    if(broadcaster!=null){
                        //broadcaster.unregister(this);
                        layout.removeComponent(unjoin);
                        Page.getCurrent().setLocation("./");
                    }
                });
                layout.addComponent(unjoin);
            }
        });
    }

   // @Override
    public void parolaGiaSuggerita() {

    }

    //@Override
    public void registratoDiNuovo() {
        access(() -> {
            if(layoutParole!=null){
                layoutParole.removeAllComponents();
            }
            layout.removeAllComponents();
            layout.addComponent(new Label("you are registred in another page"));
        });
    }

    //@Override
    public String getName() {
        return "nome messo per implementare l interfaccia";
    }

   // @Override
    public void onVoteParola(String parola) {

    }

    //@Override
    public void onUnvoteParola(String parola) {

    }
}
