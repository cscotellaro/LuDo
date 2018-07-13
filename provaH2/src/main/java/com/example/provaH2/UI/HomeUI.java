package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.LoginLayout;
import com.example.provaH2.UI.Layout.RegistrazioneLayout;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringUI(path = "/")
//@Theme("hometheme")
@Theme("materialProva")
//@Theme("material")
@Title("Cinzia")
@Viewport("width=device-width, initial-scale=1")
@com.vaadin.annotations.JavaScript({ "https://www.gstatic.com/charts/loader.js", "BarChart.js" })
public class HomeUI extends UI {

    @Autowired
    private AccountRepository repositoryA;
    private VerticalLayout mainlayout;
    private String cod;
    private String uri;
    private String confermaReg=null;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainlayout= new VerticalLayout();
        mainlayout.setMargin(true);
        mainlayout.setSizeFull();
        mainlayout.addStyleName("publicHomeMainLayout");

        String loginParam = vaadinRequest.getParameter("Login");
        cod=vaadinRequest.getParameter("cod");
        uri=vaadinRequest.getParameter("uri");
        if(loginParam!=null && loginParam.equals("true")){
            addWindow(creaWindow(0));
        }

        confermaReg=vaadinRequest.getParameter("confermaRegistrazione");
        if(confermaReg!=null){
            addWindow(creaWindow(1));
        }
        Embedded logoEmb= loadImg("src/main/resources/logo.png", "image/png");
        Image logo= new Image(null, logoEmb.getSource());
        logo.addStyleName("logo");

        HorizontalLayout header= new HorizontalLayout();
        header.setWidth(100, Unit.PERCENTAGE);
        header.setMargin(false);
        header.addComponent(logo);
        header.setComponentAlignment(logo, Alignment.TOP_LEFT);

        VerticalLayout ludovert= new VerticalLayout();
        ludovert.setMargin(false);
        ludovert.setSpacing(false);
        Label ludo=new Label("LuDo");
        ludo.addStyleName("LuDo");
        ludo.addStyleName(ValoTheme.LABEL_H2);
        ludo.addStyleName(ValoTheme.LABEL_COLORED);
        ludovert.addComponent(ludo);
        ludo.setWidth(100, Unit.POINTS);

        Label sottotitolo= new Label("Ludendo Docere");
        ludovert.addComponent(sottotitolo);
        header.addComponent(ludovert);
        header.setExpandRatio(ludovert, 1f);
        header.setComponentAlignment(ludovert, Alignment.MIDDLE_LEFT);


        Button login= new Button("login");
        login.addClickListener(clickEvent -> {
            WrappedSession session=vaadinRequest.getWrappedSession();
            //Boolean logged= (Boolean) session.getAttribute("loggato");
            Boolean logged= (Boolean)VaadinSession.getCurrent().getAttribute("loggato");
            if(logged!=null && logged==true){
                Page.getCurrent().setLocation("private/home");
            }
            getUI().addWindow(creaWindow(0));
        });

        Button registrati= new Button("register");
        registrati.addClickListener(clickEvent -> {
            getUI().addWindow(creaWindow(1));
        });

        header.addComponents(login,registrati);
        header.setComponentAlignment(login, Alignment.MIDDLE_RIGHT);
        header.setExpandRatio(login, 2f);
        header.setComponentAlignment(registrati, Alignment.MIDDLE_RIGHT);
        mainlayout.addComponent(header);

        HorizontalLayout body= new HorizontalLayout();
        body.setWidth(100,Unit.PERCENTAGE);
        body.setMargin(false);

        Label descr= new Label("\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean tempor aliquet aliquet. Nunc non semper felis. Nam in lacus mauris."+
                " Integer id posuere tortor, sit amet ultricies elit. Quisque sodales pretium placerat. Sed sed pharetra lectus. Nulla hendrerit tempus sapien ");
        descr.setWidth(100, Unit.PERCENTAGE);
        body.addComponent(descr);

        Embedded pcEmb= loadImg("src/main/resources/pc.png", "image/png");
        Image pc= new Image(null, pcEmb.getSource());
        pc.addStyleName("pcImg");
        pc.setWidth(100, Unit.PERCENTAGE);
        body.addComponent(pc);

        mainlayout.addComponent(body);
        mainlayout.setExpandRatio(body,2f);
        setContent(mainlayout);

    }

    private Window creaWindow(int tabIndex){
        final Window window = new Window();
        //window.setWidth(300.0f, Unit.PIXELS);
        //window.setSizeUndefined();

        LoginLayout layoutLogin = new LoginLayout(repositoryA, cod,uri);
        RegistrazioneLayout registrazioneLayout= new RegistrazioneLayout(repositoryA,confermaReg);
        TabSheet tabSheet= new TabSheet();
        tabSheet.addTab(layoutLogin, "Login");
        tabSheet.addTab(registrazioneLayout, "Register");
        tabSheet.setSelectedTab(tabIndex);
        tabSheet.addSelectedTabChangeListener(selectedTabChangeEvent -> {
            window.center();
        });

        tabSheet.setSizeUndefined();
        window.setContent(tabSheet);

        window.center();
        window.setModal(true);
        window.setResizable(false);
        return window;
    }

    private Embedded loadImg(String pathImg, String mimeType){
        ByteArrayOutputStream bas= new ByteArrayOutputStream();
        try {
            Path path = Paths.get(pathImg);
            byte[] data = Files.readAllBytes(path);
            bas.write(data,0, data.length);
        }catch (IOException e){
            //TODO: qui che ci mettiamo? serve qualcosa tipo oh c'è qualche problema
            //e se ci stanno problemi nn posso manco fare il pezzo di dopo di settare l'immagine
        }
        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, "img");
        resource.setMIMEType(mimeType);
        byte[] array = bas.toByteArray();

        // show the file contents - images only for now
        final Embedded logo = new Embedded(null, resource);
        logo.setMimeType(mimeType);
        return logo;
        // Embedded img= getProfileImg("nuscenness", bas);
    }
/*
    private void provaDrop(){
        final Label infoLabel = new Label("QUI devi droppare il file");
        infoLabel.setWidth(240.0f, Unit.PIXELS);

        final VerticalLayout dropPane = new VerticalLayout(infoLabel);
        dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
        dropPane.addStyleName("drop-area");
        dropPane.setSizeUndefined();

        //*****riga da decommentare per aggiungere il drop pane****
       // mainlayout.addComponent(dropPane);

        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        dropPane.addComponent(progress);

        new FileDropTarget<>(dropPane, fileDropEvent -> {
            final int fileSizeLimit = 2 * 1024 * 1024; // 2MB

            fileDropEvent.getFiles().forEach(html5File -> {
                final String fileName = html5File.getFileName();

                System.out.println(html5File.getType());

                if (html5File.getFileSize() > fileSizeLimit) {
                    Notification.show(
                            "File rejected. Max 2MB files are accepted by Sampler",
                            Notification.Type.WARNING_MESSAGE);
                } else {
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
                            showFile(fileName, bas);
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


    private void showComponent(final Component c, final String name) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.setMargin(true);
        final Window w = new Window(name, layout);
        w.addStyleName("dropdisplaywindow");
        w.setSizeUndefined();
        w.setResizable(false);
        c.setSizeUndefined();
        layout.addComponent(c);
        UI.getCurrent().addWindow(w);

    }
*/
}
