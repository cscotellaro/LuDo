package com.example.provaH2.UI;

import com.example.provaH2.UI.view.CustomerView;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

//@Push
//@Theme("darktheme")
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = "/private/home")
public class CustomerUI extends UI{

    @Autowired
    private SpringViewProvider viewProvider;
    private Navigator navigator;

     @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout=new VerticalLayout();

        MenuBar menu= new MenuBar();
        menu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        Panel viewContent= new Panel();
        viewContent.addStyleName(ValoTheme.PANEL_BORDERLESS);

        layout.addComponents(menu, viewContent);
        layout.setComponentAlignment(menu,Alignment.TOP_RIGHT);
        layout.setSizeFull();
        viewContent.setSizeFull();
        layout.setExpandRatio(viewContent,1);


        navigator= new Navigator(this, viewContent);
        navigator.addProvider(viewProvider);
  //      navigator.setErrorView(errorView);
        menu.addItem("Home", e->onHomeClicked());
        menu.addItem("Statistiche", e->onCustomersClicked());
        menu.addItem("Settings", e->onSettingsClicked());
        menu.addItem("Logout", e-> onLogoutClicked());
        setContent(layout);

        navigator.navigateTo("home");

        if(vaadinRequest.getParameter("cod")!=null){
        //TODO: mettere dove va se ho il codice nell url

        }
    }

    private void onCustomersClicked(){
        navigator.navigateTo("customers");
    }

    private void onLogoutClicked(){
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", false);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", null);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("account", null);
        Page.getCurrent().setLocation("/");
    }

    private void onSettingsClicked(){
        navigator.navigateTo("settings");
    }

    private void onHomeClicked(){
        navigator.navigateTo("home");
    }
/*


    private void onDashboardClicked(){
        navigator.navigateTo("dashboard");
    }
*/
/*
    @Override
    public void receiveBroadcast(String message) {
        System.out.println("Ho ricevuto n broadcast");
    }

    @Override
    public void countUser(int i) {
        System.out.println("è stato chiamato il metodo count user");
        ((CustomerView)navigator.getCurrentView()).conta(i);

    }*/
}
