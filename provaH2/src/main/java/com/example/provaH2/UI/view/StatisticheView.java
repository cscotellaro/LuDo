package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
import com.example.provaH2.guess.GameController;
import com.example.provaH2.repository.PartitaRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = "statistiche")
public class StatisticheView extends VerticalLayout implements View {

    @Autowired
    private PartitaRepository partitaRepository;
    private Account account;
    private List<Partita> partite;
    private FormLayout form;

     @PostConstruct
    protected  void  initialize(){
         account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
         partite=partitaRepository.cercaPartite(account);

         setSpacing(false);
         setMargin(false);

         Label title = new Label("Partite Giocate");
         title.addStyleName(ValoTheme.LABEL_H1);
         addComponent(title);

         form = new FormLayout();
         form.setMargin(false);
         form.setWidth("100%");
         addComponent(form);

         if(partite.size()==0){

             Label label1= new Label("Non hai ancora effettuato una partita");
             /*form.*/addComponent(label1);

             Button goHome= new Button("go Home");
             goHome.addClickListener(clickEvent -> {
                 Page.getCurrent().setLocation("/private/home");
             });

             /*form.*/addComponent(goHome);
             setComponentAlignment(label1, Alignment.TOP_CENTER);
             setComponentAlignment(goHome, Alignment.TOP_CENTER);
             return;
         }

         String giocoCorrente="";
         for (Partita p: partite) {
             if(!giocoCorrente.equals(p.getGioco())){
                 giocoCorrente=p.getGioco();
                 Label section = new Label(p.getGioco());
                 section.addStyleName(ValoTheme.LABEL_H3);
                 section.addStyleName(ValoTheme.LABEL_COLORED);
                 form.addComponent(section);
             }
             Label tempo= new Label(p.getTimestamp().toString());
             tempo.setCaption("data");
             form.addComponent(tempo);

             //form.addComponent(new Label("partecipanti:"));
             FormLayout punteggi= new FormLayout();
             punteggi.setCaption("punteggi:");
             for (Punteggio punteggio: p.getArray()){
                 Label giocatore= new Label(punteggio.getPunti()+ "");
                 giocatore.setCaption(punteggio.getAccount().getFullName());
                 punteggi.addComponent(giocatore);
             }
             form.addComponent(punteggi);
         }

         //addComponent(new Label("statistiche"));

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

/*
    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        if(broadcaster!=null){
            broadcaster.unregister((Broadcaster.BroadcastListener) getUI());
        }
    }

   */
}
