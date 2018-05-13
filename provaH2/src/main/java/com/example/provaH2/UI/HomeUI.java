package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.LoginLayout;
import com.example.provaH2.UI.Layout.RegistrazioneLayout;
import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

@SpringUI(path = "/")
@Viewport("width=device-width, initial-scale=1")
public class HomeUI extends UI {

    @Autowired
    private AccountRepository repositoryA;
    private HorizontalLayout mainlayout= new HorizontalLayout();
    private String cod;
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        String loginParam = vaadinRequest.getParameter("login");
        cod=vaadinRequest.getParameter("cod");
        if(loginParam!=null && loginParam.equals("true")){
            addWindow(creaWindow());
        }

        Button login= new Button("login");
        login.addClickListener(clickEvent -> {
            Boolean logged= (Boolean) vaadinRequest.getWrappedSession().getAttribute("loggato");
            if(logged!=null && logged==true){
                Page.getCurrent().setLocation("private/home");
            }
            getUI().addWindow(creaWindow());
        });
        mainlayout.addComponent(login);
        setContent(mainlayout);
    }

    private Window creaWindow(){
        final Window window = new Window();
        //window.setWidth(300.0f, Unit.PIXELS);

        LoginLayout layoutLogin = new LoginLayout(repositoryA, cod);
        RegistrazioneLayout registrazioneLayout= new RegistrazioneLayout(repositoryA);
        TabSheet tabSheet= new TabSheet();
        tabSheet.addTab(layoutLogin, "Login");
        tabSheet.addTab(registrazioneLayout, "Register");
        tabSheet.addSelectedTabChangeListener(selectedTabChangeEvent -> {
            window.center();
        });

        tabSheet.setSizeUndefined();
        window.setContent(tabSheet);
        //window.setSizeUndefined();
        window.center();
        return window;
    }
}
