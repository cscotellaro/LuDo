package com.example.provaH2.UI.view;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.codecamp.vaadin.webnotifications.WebNotifications;

import javax.annotation.PostConstruct;

@SpringView(name = "dashboard")
public class DashboardView extends VerticalLayout implements View {

    @PostConstruct
    protected  void  initialize(){
        addComponent(new Label("Dashboard"));

        Button b= new Button("Logout");
        b.addClickListener(clickEvent -> {
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", false);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", null);
            Page.getCurrent().setLocation("/Login");
        });
        addComponent(b);
        /*
        * RELEASE NOTES
        *    Ensure that notification is shown with UI access.
        *    Make the callback Runnables serializable.
        *    Add option to focus browser and tab on click.
        * */

        Button b1= new Button("Mostra notifica");
        b1.addClickListener(clickEvent -> {
            WebNotifications.create(b1, "Title")
                    .body("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus. Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.")
                    /*.icon("theme://img/logo.png")*/.tag("sometag")
                    .onClick(() -> {
                        Notification.show("onClick");
                        //TODO: qua devo fare in modo di mettere che va su una pagina
                    }).show();

        });
        addComponent(b1);

        Button link=new Button("prova link");
        link.addClickListener(clickEvent -> {
           getUI().getNavigator().navigateTo("customers");
        });
        link.addStyleName(ValoTheme.BUTTON_LINK);
        link.addStyleName("linkProva");
        addComponent(link);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
