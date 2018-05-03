package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.gestioneGioco.GameController;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@SpringView(name = "home")
public class PrivateHomeView  extends VerticalLayout implements View{

    @Autowired @Lazy
    private GameController gameController;

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
            String broadcasterId= gameController.creaPartita();
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("controller", gameController);
            Page.getCurrent().setLocation("/private/gioco?cod="+broadcasterId);
        });
        verticalLayout.addComponent(crea);

        addComponent(verticalLayout);
    }
}
