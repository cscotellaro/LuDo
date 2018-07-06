package com.example.provaH2.guess.layout;

import com.example.provaH2.guess.ParolaSuggerita;
import com.vaadin.ui.*;

public class ParolaLayout extends VerticalLayout {

    private ParolaSuggerita parolaSuggerita;
    private Button plus;
    private Label parolaLabel;
    private Label numeroLabel;
    private PuoSettareLaParolaVotata paroleSuggeriteLayout;

    public ParolaLayout(String parola, PuoSettareLaParolaVotata paroleSuggeriteLayout){
        super();
        this.parolaSuggerita=new ParolaSuggerita(parola);
        this.paroleSuggeriteLayout= paroleSuggeriteLayout;
        this.addStyleName("parolaSuggLayoyt");
        this.setWidth(100,Unit.PERCENTAGE);

        HorizontalLayout voteLayout=new HorizontalLayout();
        plus= new Button("+1");
        parolaLabel= new Label(parolaSuggerita.getParola());
        parolaLabel.setWidth(100,Unit.PERCENTAGE);
        numeroLabel= new Label(""+parolaSuggerita.getVoti());
        plus.addClickListener(clickEvent -> {
            paroleSuggeriteLayout.setParolaCorrentementeVotata(parola);
            plus.setEnabled(false);

        });

        voteLayout.addComponents(plus, numeroLabel);
        voteLayout.setExpandRatio(numeroLabel,2f);
        voteLayout.setComponentAlignment(numeroLabel, Alignment.MIDDLE_LEFT);
        addComponents(parolaLabel, voteLayout);
        this.setMargin(false);
    }

    public void incrementaVoto(){
        numeroLabel.setEnabled(false);
        parolaSuggerita.incrementaVoto();
        numeroLabel.setValue(""+parolaSuggerita.getVoti());
        numeroLabel.setEnabled(true);
    }

    public void decrementaVoto(){
        numeroLabel.setEnabled(false);
        parolaSuggerita.decrementaVoto();
        numeroLabel.setValue(""+parolaSuggerita.getVoti());
        numeroLabel.setEnabled(true);
    }

    public void allowVote(){
        plus.setEnabled(true);
    }
}
