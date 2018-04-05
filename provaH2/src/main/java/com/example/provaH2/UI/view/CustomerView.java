package com.example.provaH2.UI.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = "customers")
public class CustomerView extends VerticalLayout implements View {
/*
    @Autowired
    private CustomerService service;
    private Grid<Customer> grid;
*/



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


        addComponent(new Label("Customers"));
        addComponent(crea);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
