package com.example.provaH2.UI.view;

import com.example.provaH2.guess.GameController;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@SpringView(name = "customers")
public class CustomerView extends VerticalLayout implements View {
/*
    @Autowired
    private CustomerService service;
    private Grid<Customer> grid;
*/
    /*@Autowired @Lazy
    private Broadcaster broadcaster;
/*    private Label conta;
*/

    @Autowired @Lazy
    private GameController gameController;
     @PostConstruct
    protected  void  initialize(){
/*
        setSizeFull();

        grid= new Grid();
        grid.setSizeFull();

        grid.setItems(service.getCustomers());
        grid.addColumn(Customer::getFirstName).setCaption("firstName");
        grid.addColumn(Customer::getLastName).setCaption("lastName");
        addComponent(grid);
  */

        Button crea= new Button("Create new group");
      /*  crea.addClickListener(clickEvent -> {
            int i=BroadcasterList.creaBroadcaster();
            broadcaster=BroadcasterList.getBroadcaster(i);
            broadcaster.register((Broadcaster.BroadcastListener) getUI());
            crea.setEnabled(false);
            addComponent(new Label("localhost:8080/?cod="+i));
        });
*/
        crea.addClickListener(clickEvent -> {
            //int i=BroadcasterList.creaBroadcaster();
       //     String broadcasterId= gameController.creaPartita();
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("controller", gameController);
            //VaadinService.getCurrentRequest().getWrappedSession().setAttribute("creatore",true);
         //   Page.getCurrent().setLocation("/private/gioco?cod="+broadcasterId);
        });
        addComponent(new Label("Customers"));
        addComponent(crea);

       /* conta=new Label();
        addComponent(conta);*/
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

    public void conta(int i){
        System.out.println("conta è stato chiamato");
        conta.setValue("aa"+i);
    }
    */
}
