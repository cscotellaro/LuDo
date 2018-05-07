package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@SpringView(name = "settings")
public class SettingsView extends VerticalLayout implements View {

    @Autowired
    private AccountRepository accountRepository;
    private Account account;
    private Long id;
    private FormLayout form;
    private FormLayout secondaForm;

    private String sgamo;

    public SettingsView() {
        account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        id=(Long)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        setSpacing(false);
        setMargin(false);

        Label title = new Label("Settings");
        title.addStyleName(ValoTheme.LABEL_H1);
        addComponent(title);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        //form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        addComponent(form);

        Label section = new Label("Personal Info");
        section.addStyleName(ValoTheme.LABEL_H3);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(section);

        Label name= new Label(account.getFullName());
        name.setCaption("fullname");
        form.addComponent(name);

        Label emailField= new Label(account.getEmail());
        emailField.setCaption("Email");
        form.addComponent(emailField);

        Label section2 = new Label("Change Password");
        section2.addStyleName(ValoTheme.LABEL_H3);
        section2.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(section2);
        sezioneCambioPassword();
        //addComponent(form);

        Label section3 = new Label("Change Email");
        section3.addStyleName(ValoTheme.LABEL_H3);
        section3.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponents(section3);
        sezioneCambiaEmail();
        //addComponent(form);

        Label section4 = new Label("Elimina accunt");
        section4.addStyleName(ValoTheme.LABEL_H3);
        section4.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponents(section4);
        sezioneEliminaAccount();

    }

    private void sezioneCambioPassword(){
        PasswordField originalPassword= new PasswordField("original password");
        PasswordField newPassword= new PasswordField("new password");
        PasswordField repeatPassword= new PasswordField("repeat password");
        Label error= new Label("wrong password");
        Label notMatch= new Label("Entered password and confirmation password must match");
        Button submit= new Button("Change");
        form.addComponent(originalPassword);
        form.addComponent(newPassword);
        form.addComponent(repeatPassword);
        VerticalLayout placeholder= new VerticalLayout();
        placeholder.setMargin(false);
        placeholder.addComponent(submit);
        form.addComponents(placeholder);
        //form.addComponent(submit);


        submit.setEnabled(false);
        submit.addClickListener(clickEvent -> {
            if(account.getPassword().equals(originalPassword.getValue())){
                Notification.show("Password changed");
                accountRepository.updatePassword(id, newPassword.getValue());
            }else{
                //TODO:problema ahhhhhhh
                placeholder.addComponent(error);
            }
        });

        Binder<Account> binder= new Binder();
        binder.setBean(new Account());

        binder.forField(originalPassword)
                .asRequired("Must enter password")
                .bind(Account::getPassword, (person, password) -> {});

        binder.forField(newPassword)
                .asRequired("Password may not be empty")
                .withValidator(new StringLengthValidator(
                        "Password must be at least 7 characters long", 7, null))
                .bind(Account::getPassword, Account::setPassword);

        binder.forField(repeatPassword)
                .asRequired("Must confirm password")
                .bind(Account::getPassword, (person, password) -> {});

        binder.withValidator(Validator.from(account -> {
            if (newPassword.isEmpty() || repeatPassword.isEmpty()) {
                getUI().access(() -> placeholder.removeComponent(notMatch));
                return true;
            } else {
                if(Objects.equals(newPassword.getValue(), repeatPassword.getValue())){
                    getUI().access(() -> placeholder.removeComponent(notMatch));
                    return true;
                } else {
                    getUI().access(() -> placeholder.addComponent(notMatch));
                    return false;
                }
            }
        }, "Entered password and confirmation password must match"));

        binder.addStatusChangeListener(
                event -> getUI().access(() -> {
                    submit.setEnabled(binder.isValid());
                }));

        originalPassword.addValueChangeListener(valueChangeEvent -> placeholder.removeComponent(error));
        newPassword.addValueChangeListener(valueChangeEvent -> placeholder.removeComponent(error));
        repeatPassword.addValueChangeListener(valueChangeEvent -> placeholder.removeComponent(error));
    }

    private void sezioneCambiaEmail(){
        TextField email= new TextField("new email");
        Button changeEmail= new Button("Change");
        changeEmail.addClickListener(clickEvent -> {
            //TODO: implementa
        });
        changeEmail.setEnabled(false);

        Binder<String> binder = new Binder<>();
        binder.forField(email)
                .asRequired("Must insert email")
                .withValidator(new EmailValidator("Not a valid email address"))
                .bind(s -> sgamo, (s, v) -> sgamo = v);
        binder.addStatusChangeListener(
                event -> changeEmail.setEnabled(binder.isValid()));
        form.addComponents(email);
        form.addComponents(changeEmail);

    }

    private void sezioneEliminaAccount(){
        Button elimina= new Button("elimina");
        elimina.addClickListener(clickEvent -> {
            Window window= new Window("Elimina");
            VerticalLayout layout= new VerticalLayout();
            layout.addComponent(new Label("Are you sure?"));
            Button delete= new Button("Delete");
            delete.addClickListener(clickEvent1 -> {
                accountRepository.deleteById(id);
                Notification.show("Your account has been deleted");
                window.close();
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", false);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", null);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("account", null);
                Page.getCurrent().setLocation("/");
            });
            layout.addComponent(delete);
            window.setContent(layout);
            window.setSizeUndefined();
            window.center();
            getUI().addWindow(window);
        });
        form.addComponent(elimina);
    }
}
