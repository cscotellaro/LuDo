package com.example.provaH2.UI.view;


import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.codecamp.vaadin.webnotifications.WebNotifications;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import java.security.Key;
import java.util.Objects;

@SpringView(name = "dashboard")
public class DashboardView extends VerticalLayout implements View {

    @Autowired
    private AccountRepository accountRepository;
    private HorizontalLayout changePasswordLayout;
    @PostConstruct
    protected  void  initialize(){
        addComponent(new Label("Dashboard"));

        Button b= new Button("Logout");
        b.addClickListener(clickEvent -> {
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", false);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", null);
            Page.getCurrent().setLocation("/Login");
        });
        addComponent(b);
        /*
        * RELEASE NOTES
        *    Ensure that notification is shown with UI access.
        *    Make the callback Runnables serializable.
        *    Add option to focus browser and tab on click.
        * */
        Button b1= new Button("Mostra notifica");
        b1.addClickListener(clickEvent -> {
            WebNotifications.create(b1, "Title")
                    .body("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam hendrerit mi posuere lectus. Vestibulum enim wisi, viverra nec, fringilla in, laoreet vitae, risus.")
                    /*.icon("theme://img/logo.png")*/.tag("sometag")
                    .onClick(() -> {
                        Notification.show("onClick");
                        //TODO: qua devo fare in modo di mettere che va su una pagina
                    }).show();

        });
        addComponent(b1);

        Button link=new Button("prova link");
        link.addClickListener(clickEvent -> {
           getUI().getNavigator().navigateTo("customers");
        });
        link.addStyleName(ValoTheme.BUTTON_LINK);
        link.addStyleName("linkProva");
        addComponent(link);

        Long id=(Long)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        Account account= accountRepository.findOneById(id);
        GridLayout infoAccount= new GridLayout();
        infoAccount.setRows(2);
        infoAccount.setColumns(2);

        infoAccount.addComponent(new Label("username"));
        infoAccount.addComponent(new Label(account.getFullName()));
        infoAccount.addComponent(new Label("email"));
        infoAccount.addComponent(new Label(account.getEmail()));
        infoAccount.setSpacing(true);
        addComponent(infoAccount);

        changePasswordLayout= new HorizontalLayout();
        Button changePassword= new Button("Change password");
        changePassword.addClickListener(clickEvent -> {
            formChangePassword();
        });
        changePasswordLayout.addComponent(changePassword);
        addComponent(changePasswordLayout);
    }

    private void formChangePassword(){
        FormLayout formLayout= new FormLayout();
        PasswordField originalPassword= new PasswordField("original password");
        PasswordField newPassword= new PasswordField("new password");
        PasswordField repeatPassword= new PasswordField("repeat password");
        Label error= new Label("wrong password");
        Label notMatch= new Label("Entered password and confirmation password must match");
        Button submit= new Button("Change");
        formLayout.addComponent(originalPassword);
        formLayout.addComponent(newPassword);
        formLayout.addComponent(repeatPassword);

        formLayout.addComponent(submit);

        submit.setEnabled(false);
        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        submit.addClickListener(clickEvent -> {
            Long id=(Long)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
            Account account= accountRepository.findOneById(id);
            if(account.getPassword().equals(originalPassword.getValue())){
                Notification.show("Password changed");
                accountRepository.updatePassword(id, newPassword.getValue());
                getUI().access(() -> changePasswordLayout.removeComponent(formLayout) );
            }else{
                formLayout.addComponent(error);
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
                getUI().access(() -> formLayout.removeComponent(notMatch));
                return true;
            } else {
                if(Objects.equals(newPassword.getValue(), repeatPassword.getValue())){
                    getUI().access(() -> formLayout.removeComponent(notMatch));
                    return true;
                } else {
                    getUI().access(() -> formLayout.addComponent(notMatch));
                    return false;
                }
            }
        }, "Entered password and confirmation password must match"));

        binder.addStatusChangeListener(
                event -> getUI().access(() -> {
                    submit.setEnabled(binder.isValid());
                }));

        originalPassword.addValueChangeListener(valueChangeEvent -> formLayout.removeComponent(error));
        newPassword.addValueChangeListener(valueChangeEvent -> formLayout.removeComponent(error));
        repeatPassword.addValueChangeListener(valueChangeEvent -> formLayout.removeComponent(error));
        getUI().access(() -> {
            changePasswordLayout.addComponent(formLayout);
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
