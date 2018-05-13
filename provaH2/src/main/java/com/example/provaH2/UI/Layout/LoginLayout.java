package com.example.provaH2.UI.Layout;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.example.provaH2.utility.SendMail;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class LoginLayout extends VerticalLayout {

    private AccountRepository repositoryA;
    private FormLayout loginForm= new FormLayout();

    public LoginLayout(AccountRepository accountRepository, String cod){
        repositoryA=accountRepository;

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        Button submit = new Button("Login");
        submit.setEnabled(false);
        submit.setWidth(9, Unit.EM);
        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        Label error = new Label("email o password non corretta");

        loginForm.addComponent(error);
        error.setVisible(false);

        Binder<Account> binder = new Binder<>();
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
            String email=emailField.getValue();
            Account a = repositoryA.findOneByEmail(email);
            //System.out.println("ho trovato " + a);
            if (a != null && a.getPassword().equals(passwordField.getValue())) {
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", a.getId());
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("account", a);

                if(cod!=null){
                    Page.getCurrent().setLocation("/private/gioco?cod="+cod);
                }else{
                    Page.getCurrent().setLocation("/private/home");
                }
            } else {
                passwordField.clear();
                error.setVisible(true);
            }

        });

        Button link=new Button("forgot yuor password?");
        link.addClickListener(clickEvent -> {
            cambiaForm();
            Random rand= new Random();
            int n=rand.nextInt(9000)+1000;
            //getUI().getNavigator().navigateTo("customers");
        });
        link.addStyleName(ValoTheme.BUTTON_LINK);
        link.addStyleName(ValoTheme.BUTTON_SMALL);


        loginForm.addComponents(emailField, passwordField,submit,link);
        //setSizeUndefined();
        loginForm.setMargin(true);
        setMargin(false);
        addComponent(loginForm);
    }

    private void cambiaForm(){
        removeComponent(loginForm);
        setMargin(true);

        Label label= new Label("Insert the email. <br/> We'll send you a code ");
        label.setContentMode(ContentMode.HTML);

        //Label label2= new Label("Ti manderemo un codice da usare come password");
        VerticalLayout validationLayout= new VerticalLayout();
        validationLayout.setMargin(false);

        FormLayout form= new FormLayout();
        TextField email= new TextField("Email");
        Button sendMail= new Button("SendMail");
        sendMail.addClickListener(clickEvent -> {
            Random rand= new Random();
            int n= rand.nextInt(9000)+1000;
            repositoryA.updatePassword(email.getValue(), n+"");
            SendMail.sendMailTLS(email.getValue(), "cambio password", "la tua nuova password è " +n);
            Notification.show("mail mandata");
        });

        Binder<String> binder = new Binder<>();
        binder.setBean(sgamo);
        Label validationStatus = new Label();
        binder.setStatusLabel(validationStatus);
        binder.forField(email)
                .asRequired("Must insert email")
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(s -> sgamo, (s, v) -> sgamo = v);

        binder.withValidator(Validator.from(account ->{
            if(email.isEmpty()){
                validationLayout.removeComponent(validationStatus);
                return true;
            }else{
                Account a=repositoryA.findOneByEmail(email.getValue());
                if(a!=null){
                    validationLayout.removeComponent(validationStatus);
                    return true;
                }else{
                    validationLayout.addComponent(validationStatus);
                    return false;
                }
            }

        },"Non esiste un account con quell'email" ));

        binder.addStatusChangeListener(
                event -> sendMail.setEnabled(binder.isValid()));

        form.setMargin(false);
        form.addComponents(email);

        Button back= new Button("back");
        back.addStyleName(ValoTheme.BUTTON_LINK);
        back.addStyleName(ValoTheme.BUTTON_SMALL);
        back.addClickListener(clickEvent -> {
            removeComponent(label);
            removeComponent(form);
            removeComponent(back);
            setMargin(false);
            addComponent(loginForm);
        });


        form.setMargin(true);
        setSpacing(false);
        validationLayout.addComponent(sendMail);
        form.addComponent(validationLayout);
        //form.addComponents(sendMail,validationStatus);
        addComponents(label,form, back);
        setComponentAlignment(back,Alignment.BOTTOM_CENTER);
        setComponentAlignment(form, Alignment.MIDDLE_CENTER);

    }

    String sgamo= "";
}
