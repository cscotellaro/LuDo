package com.example.provaH2.UI.Layout;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginLayout extends FormLayout {

    private AccountRepository repositoryA;

    public LoginLayout(AccountRepository accountRepository){
        repositoryA=accountRepository;

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        Button submit = new Button("Login");
        submit.setEnabled(false);
        submit.setWidth(9, Unit.EM);
        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        Label error = new Label("email o password non corretta");

        addComponent(error);
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
                //TODO: cambiare questo path
                Page.getCurrent().setLocation("/private/home");
            } else {
                passwordField.clear();
                error.setVisible(true);
            }

        });

        addComponents(emailField, passwordField, submit);
        //setSizeUndefined();
        setMargin(true);

    }
}
