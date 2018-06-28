package com.example.provaH2.guess.layout;

import com.example.provaH2.guess.ParolaSuggerita;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ParolaLayout extends HorizontalLayout {

    private ParolaSuggerita parolaSuggerita;
    private Button plus;
    private Label parolaLabel;
    private Label numeroLabel;
    private PuoSettareLaParolaVotata paroleSuggeriteLayout;

    public ParolaLayout(String parola, PuoSettareLaParolaVotata paroleSuggeriteLayout){
        super();
        this.parolaSuggerita=new ParolaSuggerita(parola);
        this.paroleSuggeriteLayout= paroleSuggeriteLayout;

        plus= new Button("+1");
        parolaLabel= new Label(parolaSuggerita.getParola());
        numeroLabel= new Label(""+parolaSuggerita.getVoti());
        plus.addClickListener(clickEvent -> {
            paroleSuggeriteLayout.setParolaCorrentementeVotata(parola);
            plus.setEnabled(false);

        });
        addComponents(parolaLabel, numeroLabel, plus);
    }

    public void incrementaVoto(){
        removeComponent(numeroLabel);
        parolaSuggerita.incrementaVoto();
        numeroLabel.setValue(""+parolaSuggerita.getVoti());
        addComponent(numeroLabel);
    }

    public void decrementaVoto(){
        removeComponent(numeroLabel);
        parolaSuggerita.decrementaVoto();
        numeroLabel.setValue(""+parolaSuggerita.getVoti());
        addComponent(numeroLabel);
    }

    public void allowVote(){
        plus.setEnabled(true);
    }
}
