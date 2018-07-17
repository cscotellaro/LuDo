package com.example.provaH2.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//@Push
//@Theme("hometheme")
@Title("Ludo")
@Theme("materialProva")
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = "/private/home")
public class PrivateUI extends UI{

    @Autowired
    private SpringViewProvider viewProvider;
    private Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout=new VerticalLayout();
        layout.setSpacing(false);

        MenuBar menu= new MenuBar();
        menu.setHeight(3,Unit.EM);
        menu.addStyleName("menuBarTitolo");
        menu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        Panel viewContent= new Panel();
        viewContent.addStyleName(ValoTheme.PANEL_BORDERLESS);

        layout.addComponents(menu, viewContent);
        layout.setComponentAlignment(menu,Alignment.TOP_RIGHT);
        layout.setSizeFull();
        layout.setMargin(false);
        viewContent.setSizeFull();
        layout.setExpandRatio(viewContent,1);


        navigator= new Navigator(this, viewContent);
        navigator.addProvider(viewProvider);
  //      navigator.setErrorView(errorView);
        menu.addItem("Home", e->onHomeClicked());
        menu.addItem("Statistiche", e->onStatisticheClicked());
        menu.addItem("Settings", e->onSettingsClicked());
        menu.addItem("Logout", e-> onLogoutClicked());
        setContent(layout);

        navigator.navigateTo("home");

        if(vaadinRequest.getParameter("cod")!=null){
        //TODO: mettere dove va se ho il codice nell url

        }
    }

    private void onStatisticheClicked(){
        navigator.navigateTo("statistiche");
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


}
