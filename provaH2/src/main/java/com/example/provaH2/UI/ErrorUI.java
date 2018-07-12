package com.example.provaH2.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

//@Theme("hometheme")
@Theme("materialProva")
@SpringUI(path = "/error404")
public class ErrorUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout main=new VerticalLayout();
        main.setHeight(100, Unit.PERCENTAGE);
        main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label error= new Label("Ooooooooops");
        error.addStyleName("Ops");
        Label notFound= new Label("404 Page not found");
        notFound.addStyleName("notFound");
        Button goHome= new Button("Home");
        goHome.addClickListener(clickEvent -> {
            Page.getCurrent().setLocation("/private/home");
        });
        goHome.addStyleName("Home");
        VerticalLayout middle= new VerticalLayout();
        middle.addComponents(error,notFound,goHome);
        main.addComponent(middle);
        setContent(main);
    }

}
