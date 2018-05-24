package com.example.provaH2.UI.Layout;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.example.provaH2.utility.SendMail;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Random;

//TODO: qua dovremmo fare il fatto dell'invio della mail per registrarti (anche in settings)
public class RegistrazioneLayout extends FormLayout {

    private AccountRepository repositoryA;
    private Binder<Account> binder;

    private TextField emailField;

    public RegistrazioneLayout(AccountRepository repositoryA, String confermaReg){
        this.repositoryA= repositoryA;
        setMargin(true);
        setCaption("Registration Form");

        if(confermaReg!=null){
            completaRegistrazione(confermaReg);
        }else{
            setRegistrationForm();
        }

    }

    private void setRegistrationForm(){

        TextField fullNameField = new TextField("Full Name");
        emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        binder = new Binder<>();

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
                return !repositoryA.existsByFullName(fullNameField.getValue());
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

        addComponents(fullNameField, emailField,passwordField,confirmPasswordField, registerButton,validationStatus);

    }

    private void registerNewAccount(Account account){
        /*try {
            repositoryA.save(account);
            //DONE: settare un flag in sessione
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", binder.getBean().getId());
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("account", binder.getBean());
            //TODO:questo un giorno avrà un path decente alla home privata
            //vedi che ce l ho pure da qualche altra parte sta riga di codice
            Page.getCurrent().setLocation("./private/home");
        }catch (Exception e){
            Notification.show("Impossibile effettuare la registrazione");
        }*/

        Random random= new Random();
        int n= random.nextInt(9000)+1000;
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("codiceRegistrazione", ""+n);
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountDaRegistrare", account);
        SendMail.sendMailTLS(emailField.getValue(), "conferma registrazione",
                "segui il link per completare la registrazione " +
                        "http://localhost:8080/?confermaRegistrazione="+n);

        removeAllComponents();
        Label label= new Label("Ti abbiamo mandato un'email");
        Label label1= new Label("Segui il link per completare la registrazione");
        addComponents(label,label1);
    }

    private void completaRegistrazione(String conferma){
        String confermaAttribute=(String) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("codiceRegistrazione");
        if(confermaAttribute==null || !confermaAttribute.equals(conferma)){
            Label label= new Label("Impossibile completare la registrazione");
            addComponent(label);
            return;
        }

        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("codiceRegistrazione", null);
        Account account= (Account) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountDaRegistrare");
        if(account==null){
            Label label= new Label("Account non valido");
            addComponent(label);
            return;
        }
        try{
            repositoryA.save(account);
        }catch (Exception e){
            Label label= new Label("Account non valido");
            addComponent(label);
            return;
        }
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountDaRegistrare", null);
        Label label= new Label("Registrazione effettuata con successo");
        Button b= new Button("Login");
        b.addClickListener(clickEvent -> {
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", account.getId());
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("account", account);
            Page.getCurrent().setLocation("/private/home");
        });
        addComponents(label,b);
    }
}
