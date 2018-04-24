package com.example.provaH2.UI;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@SpringUI(path = "/Registrati")
public class RegistrazioneUI extends UI {

    //io l ho fatto direttamente con la repo ma mi sa che si deve fare con il service...quando un giorno capirò i service

    @Autowired
    private AccountRepository repositoryA;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        FormLayout layout = new FormLayout();
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setCaption("Registration Form");

        TextField fullNameField = new TextField("Full Name");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        Binder<Account> binder = new Binder<>();

        binder.forField(fullNameField)
                .asRequired("Name may not be empty")
                .bind(Account::getFullName, Account::setFullName);

        binder.forField(emailField)
                .asRequired("Email may not be empty")
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(Account::getEmail, Account::setEmail);

        binder.forField(passwordField)
                .asRequired("Password may not be empty")
                .withValidator(new StringLengthValidator(
                        "Password must be at least 7 characters long", 7, null))
                .bind(Account::getPassword, Account::setPassword);

        binder.forField(confirmPasswordField)
                .asRequired("Must confirm password")
                .bind(Account::getPassword, (person, password) -> {});

        binder.withValidator(Validator.from(account -> {
            if (passwordField.isEmpty() || confirmPasswordField.isEmpty()) {
                return true;
            } else {
                return Objects.equals(passwordField.getValue(),
                        confirmPasswordField.getValue());
            }
        }, "Entered password and confirmation password must match"));

        //DONE: qua ci vuole il controllo sull'email ma al momento non so ancora farlo
        binder.withValidator(Validator.from(account ->{
            if(emailField.isEmpty()){
                return true;
            }else{
                Account a=repositoryA.findOneByEmail(emailField.getValue());
                if(a!=null){
                    return false;
                }else{
                    return true;
                }
            }

        },"Esiste già un account con quell'email" ));

        binder.withValidator(Validator.from(account -> {
            if(fullNameField.isEmpty()){
                return true;
            }else{
                return !repositoryA.existsById(fullNameField.getValue());
            }

        },"Nome non valido"));
        Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);

        binder.setBean(new Account());

        Button registerButton = new Button("Register");
        registerButton.setEnabled(false);
        registerButton.addClickListener(
                event -> registerNewAccount(binder.getBean()));

        binder.addStatusChangeListener(
                event -> registerButton.setEnabled(binder.isValid()));

        layout.addComponents(fullNameField, emailField,passwordField,confirmPasswordField,validationStatus, registerButton);
        setContent(layout);
    }

    private void registerNewAccount(Account account){
        try {
            repositoryA.save(account);
            //DONE: settare un flag in sessione
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);

            //TODO:questo un giorno avrà un path decente alla home privata
            Page.getCurrent().setLocation("./");

            //TODO aggiusta st eccezione qua
        }catch (Exception e){
            Notification notification= new Notification("Impossibile effettuare la registrazione");
        }
    }
}
