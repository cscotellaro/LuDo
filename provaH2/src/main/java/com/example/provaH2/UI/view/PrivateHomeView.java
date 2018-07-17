package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.gestioneGioco.Controller;
import com.example.provaH2.gestioneGioco.Game;
import com.example.provaH2.gestioneGioco.GameList;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.addon.responsive.Responsive;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringView(name = "home")
public class PrivateHomeView  extends VerticalLayout implements View{

    @Autowired
    private GameList gameList;

    @Autowired
    private PartitaRepository partitaRepository;

    private VerticalLayout verticalLayout;
    private Account account;

    @Autowired
    private ApplicationContext ctx;

    @PostConstruct
    protected  void  initialize(){
        account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        Embedded img= (Embedded)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountImg");

        verticalLayout= new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        /*********************sezione welcome***********************************/
        HorizontalLayout layoutWelcome= new HorizontalLayout();
//        verticalLayout.addComponent(label);
        Image image= new Image("",img.getSource());
        image.setWidth(9,Unit.EM);
        image.setHeight(9, Unit.EM );

        image.addStyleName("welcomeProfileImg");
        layoutWelcome.addComponent(image);
       // layoutWelcome.addComponent(label);
        //layoutWelcome.setComponentAlignment(label,Alignment.MIDDLE_CENTER);

        VerticalLayout layoutNomeEPartita= new VerticalLayout();
        layoutNomeEPartita.addStyleName("layoutNomeEPartita");
        Label nomeGiocatore= new Label("Welcome "+ account.getFullName());
        nomeGiocatore.addStyleName("welcomeLabel");
        layoutNomeEPartita.addComponent(nomeGiocatore);
        /****************************info sulle partite*************************************/
        Partita partita= partitaRepository.lastPartita(account);
        //System.out.println("\tho chiamato il partira repo");

        if(partita==null){
            Label label1= new Label("Non hai ancora effettuato una partita");
            //verticalLayout.addComponent(label1);
            layoutNomeEPartita.addComponent(label1);
        }else {
           // Label label1= new Label("Info sull'ultima partita");
            /*FormLayout partitaInfo = new FormLayout();
            partitaInfo.setCaption("Info sull'ultima partita");
            partitaInfo.setSizeUndefined();

            Label partitaVinta= new Label(""+partita.isVinta());
            partitaVinta.setCaption("è stata vita: ");
            Label partitaData= new Label(partita.getTimestamp().toString());
            partitaData.setCaption("data e ora");

            partitaInfo.addComponents(partitaVinta, partitaData);
            verticalLayout.addComponents(/*label1,partitaInfo);
        */
            FormLayout partitaInfo = new FormLayout();
            partitaInfo.addStyleName("WelcomePartita");
            partitaInfo.setMargin(false);
            partitaInfo.setSpacing(false);
            String s=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(partita.getTimestamp());
            Label ultimaPartita= new Label(partita.getGioco() + " " + s );
            ultimaPartita.setCaption("Last game: ");
            List<Punteggio> punteggi= partita.getArray();
            int n=0;
            for( Punteggio p: punteggi){
                if(p.getAccount().getId()==account.getId()){
                    n=p.getPunti();
                    break;
                }
            }
            Label punti= new Label(""+n);
            punti.setCaption("Points: ");
            partitaInfo.addComponents(ultimaPartita,punti);
            layoutNomeEPartita.addComponent(partitaInfo);
        }
        layoutWelcome.addComponent(layoutNomeEPartita);
        verticalLayout.addComponent(layoutWelcome);

        CssLayout cssLayout= new CssLayout();

        //cssLayout.setWidth(100.0f, Unit.PERCENTAGE);
        List<Game> list= gameList.getGameList();
        for(Game g:list) {

            //TODO:togli questo for per non far mettere questo gioco 5 volte
           // for(int i=0; i<5;i++){
                cssLayout.addComponent(creaLayoutGioco(g));
           // }

          /*  Button crea = new Button("Start game: " + g.getNomeGioco());
            crea.addClickListener(clickEvent -> {
                System.out.println("Sono il bottone sater game di " + account.getFullName() + "e sto per settare il gameController");
                Controller controller= ctx.getBean(g.getControllerClass());
                String broadcasterId = controller.creaPartita(g);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("controllerGame" + broadcasterId, controller);
                Page.getCurrent().setLocation("/"+g.getPathName()+"?cod=" + broadcasterId);
            });

            verticalLayout.addComponent(crea);
            Image privaImg= new Image(g.getNomeGioco(), getGameImage(Paths.get("src/main/java/com/example/provaH2/"+g.getImagePath()),g.getNomeGioco()).getSource());
            privaImg.setHeight("100px");
            privaImg.setWidth("100px");
            verticalLayout.addComponent(privaImg);
            HorizontalLayout prova= new HorizontalLayout();
            prova.addComponent(getGameImage(Paths.get("src/main/java/com/example/provaH2/"+g.getImagePath()), "sicuro nn va"));
            verticalLayout.addComponent(prova);
           */
            //verticalLayout.addComponent(getGameImage(Paths.get("src/main/java/com/example/provaH2/"+g.getImagePath()), "ma sicuro"));
            //System.out.println(Paths.get("src/main/java/com/example/provaH2/"+g.getImagePath()));
          //  verticalLayout.addComponent(getGameImage(Paths.get("src/main/java/com/example/provaH2/guess/guess.jpeg"), "gioco"));

        }

        //   verticalLayout.addComponent(img);

        //  verticalLayout.addComponent(getGameImage(Paths.get("src/main/resources/profilo.jpg"), "gioco"));
        // verticalLayout.addComponent(getGameImage(Paths.get("src/main/java/com/example/provaH2/guess/guess.jpeg"), "gioco"));

        verticalLayout.addComponent(cssLayout);
       // new Responsive(cssLayout);
        addComponent(verticalLayout);
    }

    private Embedded getGameImage(Path path, String gameName){
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
        final StreamResource resource = new StreamResource(streamSource, gameName);
        resource.setMIMEType("image/jpeg");
        byte[] array = bas.toByteArray();

        // show the file contents - images only for now
        final Embedded embedded = new Embedded(null, resource);
        embedded.setMimeType("image/jpeg");

        embedded.setHeight("150px");
        embedded.setWidth("150px");
        return embedded;
    }

    private VerticalLayout creaLayoutGioco(Game game){
        VerticalLayout layoutGioco= new VerticalLayout();
        layoutGioco.addStyleName("layoutWelcomeGame");
        layoutGioco.setWidth(7.5f,Unit.CM);
        layoutGioco.setHeight(300, Unit.POINTS);
        layoutGioco.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Embedded embeddedImg= getGameImage(Paths.get("src/main/java/com/example/provaH2/"+game.getImagePath()), "vedi se va");
        Image img= new Image(null, embeddedImg.getSource());
        img.setWidth(100,Unit.POINTS);
        img.setHeight(100,Unit.POINTS);
        img.addStyleName("imgGame");
        layoutGioco.addComponent(img);

        Label gameName= new Label(game.getNomeGioco());
        gameName.addStyleName("WelcomeGameName");
        layoutGioco.addComponent(gameName);
        layoutGioco.setComponentAlignment(gameName, Alignment.TOP_LEFT);

        Label gameDescr= new Label(game.getDescrizioneGioco());
        gameDescr.setWidth(6, Unit.CM);
        gameDescr.setHeight(2.5f,Unit.CM);
        gameDescr.addStyleName("gameDescrWelcome");
        layoutGioco.addComponent(gameDescr);
        //layoutGioco.setExpandRatio(gameDescr, 1.1f);
        //layoutGioco.setExpandRatio(gameName, 1.1f);

        Button crea = new Button("start game"/* + game.getNomeGioco()*/);
        crea.addClickListener(clickEvent -> {
            System.out.println("Sono il bottone sater game di " + account.getFullName() + "e sto per settare il gameController");
            Controller controller= ctx.getBean(game.getControllerClass());
            String broadcasterId = controller.creaPartita(game);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("controllerGame" + broadcasterId, controller);
            //Page.getCurrent().setLocation("/"+game.getPathName()+"?cod=" + broadcasterId);
            Page.getCurrent().open("http://localhost:8080/"+game.getPathName()+"?cod=" + broadcasterId,"_blank",false);
        });

        layoutGioco.addComponent(crea);

        return layoutGioco;
    }
}
