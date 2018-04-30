package com.example.provaH2.UI.Layout;

import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.ParolaSuggerita;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ParolaLayout extends HorizontalLayout {

    private ParolaSuggerita parolaSuggerita;
    private Button plus;
    private Label parolaLabel;
    private Label numeroLabel;
    private Broadcaster broadcaster;

    public ParolaLayout(String parola, Broadcaster broadcaster){
        super();
        this.parolaSuggerita=new ParolaSuggerita(parola);
        this.broadcaster=broadcaster;
        plus= new Button("+1");
        parolaLabel= new Label(parolaSuggerita.getParola());
        numeroLabel= new Label(""+parolaSuggerita.getVoti());
        plus.addClickListener(clickEvent -> {
            //TODO:
            if(!parolaSuggerita.isAlreadyVoted()){
                broadcaster.suggerisciParola(parola);
                plus.setEnabled(false);
            }
        });
        addComponents(parolaLabel, numeroLabel, plus);
    }

    //TODO: NOTA: questo potrebbe non funzionare per lo stesso motivo per cui non andava la label di Cinzia1
    public void aggionaNumero(){
        removeComponent(numeroLabel);
        parolaSuggerita.incrementaVoto();
        numeroLabel.setValue(""+parolaSuggerita.getVoti());
        addComponent(numeroLabel);
    }
}
