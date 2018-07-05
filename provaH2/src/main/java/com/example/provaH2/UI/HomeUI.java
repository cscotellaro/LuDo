package com.example.provaH2.UI;

import com.example.provaH2.UI.Layout.LoginLayout;
import com.example.provaH2.UI.Layout.RegistrazioneLayout;
import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import com.vaadin.addon.charts.Chart;
import com.vaadin.annotations.Theme;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.FileDropTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.leif.headertags.Viewport;

@SpringUI(path = "/")
//@Theme("hometheme")
@Viewport("width=device-width, initial-scale=1")
public class HomeUI extends UI {

    @Autowired
    private AccountRepository repositoryA;
    private HorizontalLayout mainlayout= new HorizontalLayout();
    private String cod;
    private String uri;
    private String confermaReg=null;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        String loginParam = vaadinRequest.getParameter("login");
        cod=vaadinRequest.getParameter("cod");
        uri=vaadinRequest.getParameter("uri");
        if(loginParam!=null && loginParam.equals("true")){
            addWindow(creaWindow(0));
        }

        confermaReg=vaadinRequest.getParameter("confermaRegistrazione");
        if(confermaReg!=null){
            addWindow(creaWindow(1));
        }

        Button login= new Button("login");
        login.addClickListener(clickEvent -> {
            WrappedSession session=vaadinRequest.getWrappedSession();
            Boolean logged= (Boolean) session.getAttribute("loggato");
            if(logged!=null && logged==true){
                Page.getCurrent().setLocation("private/home");
            }
            getUI().addWindow(creaWindow(0));
        });

        mainlayout.setMargin(true);
        mainlayout.setSizeFull();
        mainlayout.addComponent(login);
        mainlayout.setComponentAlignment(login, Alignment.TOP_RIGHT);


       /* Image image= new Image();
        image.setIcon(new ClassResource("/profilo.jpg"));
      //  System.out.println(ClassResource.CONNECTOR_PATH);
        mainlayout.addComponent(image);
*/
      //  provaDrop();

        Chart chart = new Chart();
        mainlayout.addComponent(chart);

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
        return window;
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

    private void showFile(final String name, final ByteArrayOutputStream bas) {
        // resource for serving the file contents
        final StreamResource.StreamSource streamSource = () -> {
            if (bas != null) {
                final byte[] byteArray = bas.toByteArray();
                return new ByteArrayInputStream(byteArray);
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource, name);
        byte[] array = bas.toByteArray();

        for(int i=0; i<300; i++){
            System.out.print(array[i]+ " ");
        }
        // show the file contents - images only for now
        final Embedded embedded = new Embedded(name, resource);
        Account marco = new Account("marco", "marco@gmail.com", "marco");
        marco.setImage(array);
        repositoryA.save(marco);


        showComponent(embedded, name);
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
