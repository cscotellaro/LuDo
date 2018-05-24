package com.example.provaH2.UI.Layout;

import com.example.provaH2.UI.PuoSuggerire;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class ChatLayout extends VerticalLayout {

    VerticalLayout layoutMessaggi;

    public ChatLayout(PuoSuggerire puoSuggerire){

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
        layoutMessaggi.setSizeUndefined();
        Panel panel= new Panel();
        //panel.setHeight("100%");
        //panel.setSizeFull();
        panel.setHeight("100%");
        panel.setWidth("200px");
        panel.setContent(layoutMessaggi);
        //panel.addStyleName("panelChat");
        this.addComponents(panel,sendBar);
        this.setExpandRatio(panel, 2.0f);
        this.setHeight("100%");
        this.setMargin(false);
    }

    public void riceviMessaggio(String name, String message){
        layoutMessaggi.addComponent(new Label("[" + name+ "]: " + message));
    }

}
