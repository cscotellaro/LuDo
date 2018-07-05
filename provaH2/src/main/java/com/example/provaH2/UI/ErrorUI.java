package com.example.provaH2.UI;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.*;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;

//@Push
//@SpringUI(path = "/errorrrrrrrrrrrrr")
public class ErrorUI extends UI {

    @Autowired
    private SpringViewProvider viewProvider;

    /*@Autowired
    StatisticheView errorView;
*/
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        System.out.println("i m a error page");
        try {
            ((VaadinServletResponse) VaadinService.getCurrentResponse()).getHttpServletResponse().sendRedirect("http://www.google.com");
        }catch (Exception e){
            e.printStackTrace();
        }
        Page.getCurrent().open("http://www.google.com", "_self", true);
        VerticalLayout verticalLayout= new VerticalLayout();
        Navigator nav = new Navigator(UI.getCurrent(),verticalLayout);
        nav.addProvider(viewProvider);
       // nav.setErrorView(errorView);
        nav.navigateTo("customers");
        setContent(verticalLayout);
        /*Page.getCurrent().setLocation("/Login");
  //      access(() -> {
        //    System.out.println("iiiiiiiiiiiiiiiiiiiiii");
            VerticalLayout verticalLayout= new VerticalLayout();
            verticalLayout.addComponent(new Label("error page"));
            setContent(verticalLayout);
      //  });
*/
    }



    @WebServlet(urlPatterns = "/errore/*", name = "ErrorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ErrorUI.class, productionMode = true)
    public static class ErrorUIServlet extends VaadinServlet {
    }


}
