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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

@Push
@Theme("darktheme")
@Viewport("width=device-width, initial-scale=1")
@SpringUI(path = "/private/home")
public class CustomerUI extends UI/* implements Broadcaster.BroadcastListener*/{

    private Navigator navigator;
    private Broadcaster broadcaster;
    @Autowired
    private SpringViewProvider viewProvider;

  /*  @Autowired
    private CustomerView errorView;
*/
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout layout=new VerticalLayout();
        /*Boolean logged=(Boolean)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("loggato");
        if(logged== null || logged== false){
            //new Notification("Non sei loggato").show(Page.getCurrent());
            Page.getCurrent().setLocation("/Login");
        }
*/
        MenuBar menu= new MenuBar();
        Panel viewContent= new Panel();

        layout.addComponents(menu, viewContent);
        layout.setSizeFull();
        viewContent.setSizeFull();
        layout.setExpandRatio(viewContent,1);


        navigator= new Navigator(this, viewContent);
        navigator.addProvider(viewProvider);
  //      navigator.setErrorView(errorView);
        menu.addItem("Dashboard", e->onDashboardClicked());
        menu.addItem("Customers", e->onCustomersClicked());
        setContent(layout);

        navigator.navigateTo("dashboard");

        if(vaadinRequest.getParameter("cod")!=null){


        }
    }

    private void onDashboardClicked(){
        navigator.navigateTo("dashboard");
    }

    private void onCustomersClicked(){
        navigator.navigateTo("customers");
    }

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
