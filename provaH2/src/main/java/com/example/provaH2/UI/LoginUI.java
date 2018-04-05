package com.example.provaH2.UI;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "/Login")
public class LoginUI extends UI {
    //public final static String prova="/Login";
    //TODO: manca spring security

    private Notif

    @Autowired
    private AccountRepository repositoryA;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        FormLayout layout =new FormLayout();
        layout.setCaption("Login form");

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        Button submit= new Button("Login");
        submit.setEnabled(false);
        //Qui l alternativa sarebbe usare un binder con string (vedi:  https://vaadin.com/forum/thread/15426235)
       /* EmailValidator validator= new EmailValidator("Message");
        emailField.addValueChangeListener(event -> {
           if(validator.apply(emailField.getValue(),null).isError()){
               emailField.setComponentError(new UserError("Email non valida"));
           }else{
               emailField.setComponentError(null);
               Notification.show(event.getValue());
           }
        });
*/
        Binder<Account> binder= new Binder<>();
        binder.forField(emailField)
                .asRequired("Email may not be empty")
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(Account::getEmail, Account::setEmail);

        binder.forField(passwordField)
                .asRequired("Password may not be empty")
//                .withValidator(new StringLengthValidator(
  //                      "Password must be at least 7 characters long", 7, null))
                .bind(Account::getPassword, Account::setPassword);

        binder.addStatusChangeListener(
                event -> submit.setEnabled(binder.isValid()));

        submit.addClickListener(clickEvent -> {
            //TODO: fare il controllo su se esiste un account con quell email e passowrd

            //DONE: settare un flag in sessione
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);
            //TODO:questo un giorno avrà un path decente alla home privata
            Page.getCurrent().setLocation("./");

        });


        layout.addComponents(emailField, passwordField, submit);
        setContent(layout);
    }
}
