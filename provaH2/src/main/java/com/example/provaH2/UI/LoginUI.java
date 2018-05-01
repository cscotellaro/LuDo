package com.example.provaH2.UI;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

@Theme("darktheme")
@SpringUI(path = "/Login")
@Viewport("width=device-width, initial-scale=1")
public class LoginUI extends UI {

    @Autowired
    private AccountRepository repositoryA;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Questo serve perchè quando sei già loggaato ti porta già direttamente alla home
        Boolean logged=(Boolean)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("loggato");
        if(logged!= null && logged== true){
            //TODO:questo un giorno avrà un path decente
            String cod = vaadinRequest.getParameter("cod");
            if (cod != null) {
                Page.getCurrent().setLocation("/private/gioco?cod=" + cod);
            } else {
                Page.getCurrent().setLocation("/private/home");
            }
        }

        FormLayout layout = new FormLayout();
        //layout.setCaption("Login form");

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        Button submit = new Button("Login");
        submit.setEnabled(false);
        submit.setWidth(9, Unit.EM);
        submit.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        //submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        Label error = new Label("email o password non corretta");
        layout.addComponent(error);
        error.setVisible(false);

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
            //DONE: fare il controllo su se esiste un account con quell email e passowrd
            Account a = repositoryA.findOneByEmail(emailField.getValue());
            System.out.println("ho trovato " + a);
            if (a != null && a.getPassword().equals(passwordField.getValue())) {
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("loggato", true);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountId", a.getId());

                //TODO:questo un giorno avrà un path decente
                String cod = vaadinRequest.getParameter("cod");
                System.out.println("il codice dalla request è " + cod);
                if (cod != null) {
                    Page.getCurrent().setLocation("/private/gioco?cod=" + cod);
                } else {
                    Page.getCurrent().setLocation("/private/home");
                }
            } else {
                passwordField.clear();
                error.setVisible(true);
            }

        });


        layout.addComponents(emailField, passwordField, submit);
        layout.setSizeUndefined();
        layout.setMargin(false);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        //int h=Page.getCurrent().getBrowserWindowHeight();
        //int w= Page.getCurrent().getBrowserWindowWidth();
        //System.out.println("height: "+ h+ " width: "+w );
        //int w=Page.getCurrent().getWebBrowser().getScreenWidth();
        //int h= Page.getCurrent().getWebBrowser().getScreenHeight();
        //System.out.println("height: "+ h+ " width: "+w );
        //verticalLayout.setHeight("100%");
        //verticalLayout.setWidth("100%");
        //verticalLayout.setHeight(h, Unit.PIXELS);
        //verticalLayout.setWidth(w,Unit.PIXELS);
        //Responsive.makeResponsive(verticalLayout);
        verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //verticalLayout.setResponsive(true);
        VerticalLayout layoutDentro = new VerticalLayout();
        //layoutDentro.setWidth(w, Unit.PIXELS);
        layoutDentro.setSpacing(false);
        layoutDentro.setMargin(false);
        verticalLayout.setMargin(false);
        layoutDentro.addStyleName("provaVertical");
        layoutDentro.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label titoloLabel = new Label("My label");
        titoloLabel.setHeight(4, Unit.EM);
        layoutDentro.addComponent(titoloLabel);
        layoutDentro.addComponent(layout);

        verticalLayout.addComponent(layoutDentro);
        setContent(verticalLayout);

    }
}
