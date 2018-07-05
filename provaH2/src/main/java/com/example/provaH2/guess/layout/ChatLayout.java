package com.example.provaH2.guess.layout;

import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;

public class ChatLayout extends VerticalLayout {
    HashMap<String, Embedded> playerImages;
    VerticalLayout layoutMessaggi;

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
        sendBar.addComponents(chatField,send);
        //setComponentAlignment(sendBar, Alignment.BOTTOM_CENTER);

        layoutMessaggi= new VerticalLayout();
        layoutMessaggi.setWidth(200,Unit.PIXELS);
        //layoutMessaggi.setHeight(100, Unit.PERCENTAGE);
        Panel panel= new Panel();

        panel.setHeight(100, Unit.PERCENTAGE);
        panel.setWidth(200,Unit.POINTS);
        panel.setContent(layoutMessaggi);

        //panel.addStyleName("panelChat");
        this.addComponents(panel,sendBar);
        this.setExpandRatio(panel, 2.0f);

       // this.addComponents(layoutMessaggi,sendBar);
        this.setHeight(100,Unit.PERCENTAGE);
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
        layoutMessaggi.addComponent(new Label("[" + name+ "]: " + message));
        HorizontalLayout horizontalLayout= new HorizontalLayout();
        horizontalLayout.setWidth(100,Unit.PERCENTAGE);
        horizontalLayout.addComponent(new Label(message));
        layoutMessaggi.addComponent(horizontalLayout);
    }

}
