package com.example.provaH2.guess.layout;

import com.example.provaH2.guess.PuoSuggerire;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;

public class ParoleSuggeriteLayout extends VerticalLayout implements PuoSettareLaParolaVotata {

    private PuoSuggerire puoSuggerire;
    private TextField parolaField;
    private Button suggerisci;
    private HashMap<String, ParolaLayout> parole;
    private String parolaCorrentementeVotata;
    private VerticalLayout layoutParole;

    public ParoleSuggeriteLayout(PuoSuggerire puoSuggerire){
        super();
        this.puoSuggerire=puoSuggerire;
        this.setHeight(100,Unit.PERCENTAGE);
        this.setWidth(100,Unit.PERCENTAGE);
        parole= new HashMap<>();

        HorizontalLayout barra= new HorizontalLayout();
        barra.setWidth(100,Unit.PERCENTAGE);
        parolaField= new TextField();
        suggerisci= new Button("Suggerisci");
        suggerisci.addClickListener(clickEvent -> {
            if (!parolaField.isEmpty()){
                puoSuggerire.suggerisci(parolaField.getValue());
                parolaField.clear();
            }
        });
        parolaField.setWidth(100,Unit.PERCENTAGE);
        barra.addComponents(parolaField,suggerisci);
        barra.setExpandRatio(parolaField,2f);
        suggerisci.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        Panel panel= new Panel();
        panel.setWidth(100,Unit.PERCENTAGE);
        panel.setHeight(100, Unit.PERCENTAGE);
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        layoutParole= new VerticalLayout();
        layoutParole.setWidth(100, Unit.PERCENTAGE);
        layoutParole.setMargin(false);
        panel.setContent(layoutParole);

        this.addComponents(barra, panel);
        this.setExpandRatio(panel, 2f);
    }

    //questo metodo mi sa che serve se vuoi fare che rigiochi di nnuovo
    public void ripulisci(){
        parole.forEach((s, components1) -> {
            this.removeComponent(components1);
            parole.remove(components1);
        });
    }

    @Override
    public void setParolaCorrentementeVotata(String parolaNuova) {
        if(parolaCorrentementeVotata!=null){
            parole.get(parolaCorrentementeVotata).allowVote();
        }
        puoSuggerire.unvoteParola(parolaCorrentementeVotata);
        this.parolaCorrentementeVotata = parolaNuova;
        puoSuggerire.voteParola(parolaNuova);

    }

    public void aggiungiParola(String parola){
        ParolaLayout parolaLayout= new ParolaLayout(parola, this);
        layoutParole.addComponent(parolaLayout);
        parole.put(parola, parolaLayout);
    }

    public void voteParola(String parola){
        ParolaLayout parolaLayout= parole.get(parola);
        if(parolaLayout!=null){
            parolaLayout.incrementaVoto();
        }
    }

    public void unvoteParola(String parola){
        ParolaLayout parolaLayout= parole.get(parola);
        if(parolaLayout!=null){
            parolaLayout.decrementaVoto();
        }
    }

    public void disattivaBottone(){
        suggerisci.setEnabled(false);
    }
}
