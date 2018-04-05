package com.example.provaH2.UI;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
public class CustomerUI extends UI {

    private Navigator navigator;

    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout=new VerticalLayout();
        Boolean logged=(Boolean)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("loggato");
        if(logged== null || logged== false){
            new Notification("Non sei loggato").show(Page.getCurrent());
            Page.getCurrent().setLocation("./Login");
        }

        MenuBar menu= new MenuBar();
        Panel viewContent= new Panel();

        layout.addComponents(menu, viewContent);
        layout.setSizeFull();
        viewContent.setSizeFull();
        layout.setExpandRatio(viewContent,1);

        navigator= new Navigator(this, viewContent);
        navigator.addProvider(viewProvider);

        menu.addItem("Dashboard", e->onDashboardClicked());
        menu.addItem("Customers", e->onCustomersClicked());
        setContent(layout);

        navigator.navigateTo("dashboard");
    }

    private void onDashboardClicked(){
        navigator.navigateTo("dashboard");
    }

    private void onCustomersClicked(){
        navigator.navigateTo("customers");
    }

}
