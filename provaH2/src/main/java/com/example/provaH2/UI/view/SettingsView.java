package com.example.provaH2.UI.view;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.data.Binder;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.FileDropTarget;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Objects;

@SpringView(name = "settings")
public class SettingsView extends VerticalLayout implements View {

    @Autowired
    private AccountRepository accountRepository;
    private Account account;
    private Long id;
    private FormLayout form;
    private FormLayout formCambioPassword;
    private FormLayout formChangeImage;
    private FormLayout formelimina;
    private FormLayout secondaForm;

    private String sgamo;
    private byte[] nuovaImmagine=null;

    @PostConstruct
    public void initialize() {
        setWidth(100,Unit.PERCENTAGE);
        account=(Account)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("account");
        id=(Long)  VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountId");
        setSpacing(false);
        setMargin(false);
        addStyleName("SettingsMainLayout");
        Label title = new Label("Settings");
        title.addStyleName(ValoTheme.LABEL_H1);
        addComponent(title);

        VerticalLayout personalInfo= new VerticalLayout();
        personalInfo.addStyleName("veticalSectionSettings");
        personalInfo.setMargin(false);
        personalInfo.setWidth(70, Unit.PERCENTAGE);
        form = new FormLayout();
        form.setMargin(true);
        form.setWidth(100, Unit.PERCENTAGE);
        //form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);


        Label section = new Label("Personal Info");
        section.addStyleName(ValoTheme.LABEL_H3);
        //section.addStyleName(ValoTheme.LABEL_COLORED);
        section.addStyleName("SettingsSectionLabel");
        personalInfo.addComponent(section);
        form.addStyleName("cardsSettings");

        Label name= new Label(account.getFullName());
        name.setWidth(100,Unit.PERCENTAGE);
        name.setCaption("Fullname");
        form.addComponent(name);

        Label emailField= new Label(account.getEmail());
        name.setWidth(100,Unit.PERCENTAGE);
        emailField.setCaption("Email");
        form.addComponent(emailField);
        personalInfo.addComponent(form);

        addComponent(personalInfo);
        setComponentAlignment(personalInfo, Alignment.MIDDLE_CENTER);

        /***********cambio password**********************/
        VerticalLayout layoutCambioPassword=new VerticalLayout();
        layoutCambioPassword.addStyleName("veticalSectionSettings");
        layoutCambioPassword.setWidth(70, Unit.PERCENTAGE);
        layoutCambioPassword.setMargin(false);

        formCambioPassword= new FormLayout();
        formCambioPassword.setWidth(100, Unit.PERCENTAGE);
        formCambioPassword.setMargin(true);
        Label section2 = new Label("Change Password");
        section2.addStyleName(ValoTheme.LABEL_H3);
        section2.addStyleName(ValoTheme.LABEL_COLORED);
        section2.addStyleName("SettingsSectionLabel");
        layoutCambioPassword.addComponent(section2);
        sezioneCambioPassword();
        layoutCambioPassword.addComponent(formCambioPassword);
        addComponent(layoutCambioPassword);
        setComponentAlignment(layoutCambioPassword, Alignment.MIDDLE_CENTER);
        formCambioPassword.addStyleName("cardsSettings");

        /*************************img********************/
        VerticalLayout layoutCambioImg= new VerticalLayout();
        layoutCambioImg.addStyleName("veticalSectionSettings");
        layoutCambioImg.setWidth(70, Unit.PERCENTAGE);
        layoutCambioImg.setMargin(false);

        formChangeImage= new FormLayout();
        formChangeImage.setWidth(100,Unit.PERCENTAGE);
        formChangeImage.setMargin(true);
        Label section3 = new Label("Change Image");
        section3.addStyleName(ValoTheme.LABEL_H3);
        section3.addStyleName(ValoTheme.LABEL_COLORED);
        section3.addStyleName("SettingsSectionLabel");
        layoutCambioImg.addComponents(section3);
        sezioneCambiaImg();
        layoutCambioImg.addComponent(formChangeImage);
        addComponent(layoutCambioImg);
        setComponentAlignment(layoutCambioImg, Alignment.MIDDLE_CENTER);
        formChangeImage.addStyleName("cardsSettings");

        /****************elimina************************/
        VerticalLayout layoutElimina= new VerticalLayout();
        layoutElimina.addStyleName("veticalSectionSettings");
        layoutElimina.setWidth(70, Unit.PERCENTAGE);
        layoutElimina.setMargin(false);

        formelimina= new FormLayout();
        formelimina.setWidth(100, Unit.PERCENTAGE);
        formelimina.setMargin(true);
        Label section4 = new Label("Delete account");
        section4.addStyleName(ValoTheme.LABEL_H3);
        section4.addStyleName(ValoTheme.LABEL_COLORED);
        section4.addStyleName("SettingsSectionLabel");
        layoutElimina.addComponents(section4);
        layoutElimina.addComponent(formelimina);
        sezioneEliminaAccount();
        addComponent(layoutElimina);
        setComponentAlignment(layoutElimina, Alignment.MIDDLE_CENTER);
        formelimina.addStyleName("cardsSettings");

    }

    private void sezioneCambioPassword(){

        PasswordField originalPassword= new PasswordField("Original password");
        PasswordField newPassword= new PasswordField("New password");
        PasswordField repeatPassword= new PasswordField("Repeat password");
        Label error= new Label("Wrong password");
        Label notMatch= new Label("Entered password and confirmation password must match");
        Button submit= new Button("change");
        formCambioPassword.addComponent(originalPassword);
        formCambioPassword.addComponent(newPassword);
        formCambioPassword.addComponent(repeatPassword);
        VerticalLayout placeholder= new VerticalLayout();
        placeholder.setMargin(false);
        placeholder.addComponent(submit);
        formCambioPassword.addComponents(placeholder);
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

    private void sezioneCambiaImg(){
        final Label infoLabel = new Label("Drop your image here");
        infoLabel.setCaption("Your image:");
        infoLabel.setWidth(240.0f, Unit.PIXELS);

        final VerticalLayout dropPane = new VerticalLayout(/*infoLabel*/);
        dropPane.setSpacing(false);
     //   dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
        dropPane.addStyleName("drop-area");
        dropPane.setSizeUndefined();
       /// dropPane.setCaption("Your image:");
        dropPane.setMargin(false);
        final Embedded newImage= (Embedded) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("accountImg");
        newImage.setCaption(null);
        newImage.setHeight("150px");
        newImage.setWidth("150px");
        dropPane.addComponent(newImage);


        Button change= new Button("save");
        change.addClickListener(clickEvent -> {
            if(nuovaImmagine!=null){
                accountRepository.updateImage(account.getId(), nuovaImmagine);
                VaadinService.getCurrentRequest().getWrappedSession().setAttribute("accountImg", newImage);
                Notification.show("Image changed");
            }
        });
        formChangeImage.addComponent(infoLabel);
        formChangeImage.addComponents(dropPane,change);

        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        dropPane.addComponent(progress);
        new FileDropTarget<>(dropPane, fileDropEvent -> {
            final int fileSizeLimit = 1 * 1024 * 1024; // 2MB

            fileDropEvent.getFiles().forEach(html5File -> {
                final String fileName = html5File.getFileName();


                System.out.println(html5File.getType());

                if (html5File.getFileSize() > fileSizeLimit) {
                    Notification.show("File rejected. Max 1MB files are accepted", Notification.Type.WARNING_MESSAGE);
                } else if(!html5File.getType().equals("image/jpg") && !html5File.getType().equals("image/jpeg")){
                    Notification.show("File rejected.Just jpg files are accepted",
                            Notification.Type.WARNING_MESSAGE);
                } else{
                    final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    final StreamVariable streamVariable = new StreamVariable() {

                        @Override
                        public OutputStream getOutputStream() {
                            return bas;
                        }

                        @Override
                        public boolean listenProgress() {
                            return false;
                        }

                        @Override
                        public void onProgress(final StreamingProgressEvent event) {
                        }

                        @Override
                        public void streamingStarted( final StreamingStartEvent event) {}

                        @Override
                        public void streamingFinished( final StreamingEndEvent event) {
                            progress.setVisible(false);
                            showFile(fileName, bas, newImage);
                        }

                        @Override
                        public void streamingFailed( final StreamingErrorEvent event) {
                            progress.setVisible(false);
                        }

                        @Override
                        public boolean isInterrupted() {
                            return false;
                        }
                    };
                    html5File.setStreamVariable(streamVariable);
                    progress.setVisible(true);
                }
            });
        });

    }

    private void showFile(final String name, final ByteArrayOutputStream bas, final Embedded embedded) {
        // resource for serving the file contents
        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, name);

        embedded.setSource(resource);
        embedded.setAlternateText(name);
        nuovaImmagine=bas.toByteArray();
    }

    private void sezioneEliminaAccount(){
        Button elimina= new Button("delete");
        elimina.addClickListener(clickEvent -> {
            Window window= new Window("Delete");
            window.setWidth(300, Unit.POINTS);
            window.setResizable(false);
            window.setModal(true);
            //window.setHeight(20, Unit.POINTS);
            VerticalLayout layout= new VerticalLayout();
            layout.setWidth(300,Unit.POINTS);
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
            Label deleteLabel=new Label("Are you sure you want ot delete this account?");
            deleteLabel.setWidth(100,Unit.PERCENTAGE);
            layout.addComponent(deleteLabel);
            Button delete= new Button("delete");
            //delete.setCaption("Dete your account");
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
       // elimina.setCaption("Delete your account");
        VerticalLayout layoutElimina= new VerticalLayout();
        layoutElimina.setMargin(false);
        layoutElimina.addComponent(elimina);
        layoutElimina.setCaption("Delete your account:");
        formelimina.addComponent(layoutElimina);

        //formelimina.addComponent(elimina);
    }

    //TODO: questo metodo non serve più ma l ho lasciata perchè non si sa mai
    private void sezioneCambiaEmail(){
        TextField email= new TextField("New email");
        Button changeEmail= new Button("Change");
        changeEmail.addClickListener(clickEvent -> {
            //TODO: qua si deve mandare la mail e poi mandare il tutto su una pag specifica che nn so qual è
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

}
