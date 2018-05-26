package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.gestioneGioco.GameController;
import com.example.provaH2.repository.AccountRepository;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringView(name = "home")
public class PrivateHomeView  extends VerticalLayout implements View{

    @Autowired @Lazy
    private GameController gameController;
    @Autowired
    private PartitaRepository partitaRepository;

    private VerticalLayout verticalLayout;
    private Button crea;
    private Account account;

    @PostConstruct
    protected  void  initialize(){
        account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");

        verticalLayout= new VerticalLayout();
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label label= new Label("Welcome "+ account.getFullName());
        verticalLayout.addComponent(label);

        crea= new Button("Start game");
        crea.addClickListener(clickEvent -> {
            System.out.println("Sono il bottone sater game di " + account.getFullName() + "e sto per settare il gameController");
            String broadcasterId= gameController.creaPartita();
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("controllerGame"+broadcasterId, gameController);
            Page.getCurrent().setLocation("/private/gioco?cod="+broadcasterId);
        });
        verticalLayout.addComponent(crea);

        /**************************esperimenti immagini*************************************/
       /*
        ByteArrayOutputStream bas= new ByteArrayOutputStream();
        byte[] imgArray= account.getImage();
        if(imgArray!=null){
            bas.write(imgArray,0,imgArray.length);
        }else{
            try {
                Path path = Paths.get("src/main/resources/profilo.jpg");
                byte[] data = Files.readAllBytes(path);
                bas.write(data,0, data.length);
            }catch (IOException e){
                //TODO: qui che ci mettiamo? serve qualcosa tipo oh c'è qualche problema
                //e se ci stanno problemi nn posso manco fare il pezzo di dopo di settare l'immagine
            }
        }
        Embedded img= getProfileImg("nuscenness", bas);
        img.setHeight("200px");
        img.setWidth("200px");

        */
        Embedded img= (Embedded)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountImg");
        verticalLayout.addComponent(img);

        /****************************info sulle partite*************************************/
        Partita partita= partitaRepository.lastPartita(account);
        if(partita==null){
            Label label1= new Label("Non hai ancora effettuato una partita");
            verticalLayout.addComponent(label1);
        }else {
            Label label1= new Label("Info sull'ultima partita");
            FormLayout partitaInfo = new FormLayout();
            partitaInfo.setSizeUndefined();

            Label partitaVinta= new Label(""+partita.isVinta());
            partitaVinta.setCaption("è stata vita: ");
            Label partitaData= new Label(partita.getTimestamp().toString());
            partitaData.setCaption("data e ora");

            partitaInfo.addComponents(partitaVinta, partitaData);
            verticalLayout.addComponents(label1,partitaInfo);
        }
        addComponent(verticalLayout);
    }
/*
    private Embedded getProfileImg(final String name, final ByteArrayOutputStream bas) {
        // resource for serving the file contents
        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, name);
        resource.setMIMEType("image/jpeg");
        byte[] array = bas.toByteArray();

        // show the file contents - images only for now
        final Embedded embedded = new Embedded(name, resource);
        embedded.setMimeType("image/jpeg");
        return embedded;
    }
    */
}
