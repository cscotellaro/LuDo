package com.example.provaH2.guess.layout;

import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;

public class ChatLayout extends VerticalLayout {
    private HashMap<String, Embedded> playerImages;
    private VerticalLayout layoutMessaggi;
    private Panel panel;

    public ChatLayout(PuoSuggerire puoSuggerire, HashMap<String, Embedded> playerImg){
        playerImages=playerImg;
        TextField chatField= new TextField();
        Button send= new Button( VaadinIcons.PAPERPLANE_O);
        send.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        send.addStyleName("bottoneInvio");
        send.addClickListener(clickEvent -> {
            if (!chatField.isEmpty()){
                puoSuggerire.sendChat(chatField.getValue());
                chatField.clear();
            }
        });
        HorizontalLayout sendBar= new HorizontalLayout();
        sendBar.setWidth(100, Unit.PERCENTAGE);
        chatField.setWidth(100,Unit.PERCENTAGE);
        sendBar.addComponents(chatField,send);
        sendBar.setExpandRatio(chatField, 2f);

        layoutMessaggi= new VerticalLayout();
        layoutMessaggi.setWidth(100,Unit.PERCENTAGE);
        //layoutMessaggi.addComponent(new Label("label di prova per veder se sta cosa funziona"));
        //layoutMessaggi.setHeight(100, Unit.PERCENTAGE);
        panel= new Panel();

        panel.setHeight(100, Unit.PERCENTAGE);
        panel.setWidth(100,Unit.PERCENTAGE);
        panel.setContent(layoutMessaggi);
        panel.setScrollTop(Integer.MAX_VALUE);

        //panel.addStyleName("panelChat");
        this.addComponents(panel,sendBar);
        this.setExpandRatio(panel, 2.0f);

       // this.addComponents(layoutMessaggi,sendBar);
        this.setHeight(100,Unit.PERCENTAGE);
        this.setWidth(100, Unit.PERCENTAGE);
        this.setMargin(false);

     /*   playerImg.forEach((s, embedded) -> {
            this.addComponent(new Label(s));
            Image image= new Image(s,embedded.getSource());
            image.setWidth("100");
            image.setHeight("100");
            this.addComponent(image);
        });
*/
    }

    public void riceviMessaggio(String name, String message){
       // layoutMessaggi.addComponent((Embedded)playerImages.get(name));

        HorizontalLayout messageLayout=new HorizontalLayout();
        messageLayout.setWidth(100, Unit.PERCENTAGE);
        Image image= new Image(null,playerImages.get(name).getSource());
        image.setWidth(25, Unit.POINTS);
        image.setHeight(25,Unit.POINTS);
        image.addStyleName("pallinoChat");

        VerticalLayout baloon= new VerticalLayout();
        baloon.addStyleName("baloonChat");
        baloon.setMargin(false);
        baloon.setSpacing(false);
        baloon.setWidth(100, Unit.PERCENTAGE);
        Label nameLabel= new Label(name);
        nameLabel.addStyleName("nameLabelChat");
        Label messageLabel= new Label(message);
        messageLabel.setWidth(100, Unit.PERCENTAGE);
        baloon.addComponents(nameLabel, messageLabel);
        baloon.setExpandRatio(messageLabel,2f);

        messageLayout.addComponents(image, baloon);
        messageLayout.setComponentAlignment(image, Alignment.BOTTOM_LEFT);
        messageLayout.setExpandRatio(baloon, 2f);

        layoutMessaggi.addComponent(messageLayout);
        panel.setScrollTop(Integer.MAX_VALUE);
       /* Label chat=new Label("[" + name+ "]: " + message);
        chat.setWidth(100,Unit.PERCENTAGE);
        layoutMessaggi.addComponent(chat);
        /*HorizontalLayout horizontalLayout= new HorizontalLayout();
        horizontalLayout.setWidth(100,Unit.PERCENTAGE);
        horizontalLayout.addComponent(new Label(message));
        layoutMessaggi.addComponent(horizontalLayout);
        */
    }

}
