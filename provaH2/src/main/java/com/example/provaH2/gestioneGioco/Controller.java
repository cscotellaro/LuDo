package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Embedded;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public abstract  class Controller {
    private Long accountId;
    private Broadcaster broadcaster;
    private Game game;
    private PartitaRepository partitaRepository;
    protected Partita partita;

    public Controller(PartitaRepository partitaRepository){
        this.partitaRepository= partitaRepository;
    }

    /**metodo chiamato da Broadcaster che aggiorna il numero degli utenti che sono in game*/
    public abstract void countUser(int n, ArrayList<Account> accounts);

    //metodi chiamati da waiting for players
    /**metodo che serve per inizializzare le variabili che servono per il gioco*/
    public  abstract  void giocaAncora();
    public  abstract  void startGame();

    //questo verrà chiamato dal bottone crea partita quando faremo il bottone vero
    //Crea un broadcaster con un intero univoco
    public String creaPartita(Game game){
        this.game=game;
        String broadcasterId;
        accountId= (Long) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        String timeStamp = new SimpleDateFormat("dd.HH.mm.ss").format(new Date());
        broadcasterId= accountId+"_"+ timeStamp;
        //id=broadcasterId;
        System.out.println("un controller viene chiamato crea partitia "+ broadcasterId );
        try {
            broadcaster=game.getBroadcasterClass().getConstructor(Controller.class, String.class).newInstance(this, broadcasterId);
            //broadcaster= (Broadcaster) broadcasterClass.getConstructors()[0].newInstance(this,broadcasterId);
            //broadcaster = broadcasterClass.getDeclaredConstructor(broadcasterClass).newInstance(this, broadcasterId);
            BroadcasterList.creaBroadcaster(broadcaster);
        }catch (Exception e){
            e.printStackTrace();
        }

        return broadcasterId;
    }

    public void hostGame(){
        partita= new Partita(new Timestamp(new Date().getTime()), game.getNomeGioco());
        startGame();
    }

    public void rigioca(){
        giocaAncora();
        hostGame();
    }


    protected void addPunteggio(Punteggio punteggio){
        partita.addPunteggio(punteggio);
    }

    protected void removePunteggio(Account account){
        partita.removePunteggio(account);
    }

    protected void removePunteggio(long account){
        partita.removePunteggio(account);
    }

    protected void removePunteggio(String accountName){
        partita.removePunteggio(accountName);
    }

    public void savePartita(){
        System.out.println(partita);
        partitaRepository.save(partita);
    }

    /* protected  void setPunteggi(ArrayList<Punteggio> punteggi){
        partita.setArray(punteggi);
    }
*/
    public Broadcaster getBroadcaster(){
        System.out.println(" sono nel controller  il mio accountId è "+ accountId+"get broadcaster BROADCASTER= "+ broadcaster.getId());
        return  broadcaster;
    }

    public String getId(){
        //return id;
        return  broadcaster.getId();
    }

    public Game getGame(){
        return game;
    }

    public Embedded getGameImage(){
        Path path= Paths.get("src/main/java/com/example/provaH2/"+game.getImagePath());
        ByteArrayOutputStream bas= new ByteArrayOutputStream();
        try {

            byte[] data = Files.readAllBytes(path);
            bas.write(data,0, data.length);
        }catch (IOException e){
            //TODO: qui che ci mettiamo? serve qualcosa tipo oh c'è qualche problema
            //e se ci stanno problemi nn posso manco fare il pezzo di dopo di settare l'immagine
        }

        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, game.getNomeGioco());
        resource.setMIMEType("image/jpeg");
        byte[] array = bas.toByteArray();

        // show the file contents - images only for now
        final Embedded embedded = new Embedded(null, resource);
        embedded.setMimeType("image/jpeg");

        embedded.setHeight("150px");
        embedded.setWidth("150px");
        return embedded;
    }
}
