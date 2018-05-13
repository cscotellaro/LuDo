package com.example.provaH2.UI.Layout;

import com.example.provaH2.UI.PuoSuggerire;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.HashMap;

public class ParoleSuggeriteLayout extends VerticalLayout {

    private PuoSuggerire puoSuggerire;
    private TextField parolaField;
    private Button suggerisci;
    private HashMap<String, ParolaLayout> parole;
    private String parolaCorrentementeVotata;

    public ParoleSuggeriteLayout(PuoSuggerire puoSuggerire){
        super();
        this.puoSuggerire=puoSuggerire;
        parole= new HashMap<>();
        parolaField= new TextField();
        suggerisci= new Button("Suggerisci");
        suggerisci.addClickListener(clickEvent -> {
            if (!parolaField.isEmpty()){
                puoSuggerire.suggerisci(parolaField.getValue());
                parolaField.clear();
            }
        });
        suggerisci.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        this.addComponents(parolaField,suggerisci);
    }

    //questo metodo mi sa che serve se vuoi fare che rigiochi di nnuovo
    public void ripulisci(){
        parole.forEach((s, components1) -> {
            this.removeComponent(components1);
            parole.remove(components1);
        });
    }

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
        this.addComponent(parolaLayout);
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
}
