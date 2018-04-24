package com.example.provaH2.UI.Layout;

import com.example.provaH2.gestioneGioco.Broadcaster;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.HashMap;

public class ParoleSuggeriteLayout extends VerticalLayout {

    private  Broadcaster broadcaster;
    private  TextField parolaField;
    private Button suggerisci;
    private HashMap<String, ParolaLayout> parole;

    public ParoleSuggeriteLayout(Broadcaster broadcaster){
        super();
        this.broadcaster=broadcaster;
        parole= new HashMap<>();
        parolaField= new TextField();
        suggerisci= new Button("Suggerisci");
        suggerisci.addClickListener(clickEvent -> {
            if (!parolaField.isEmpty()){
                broadcaster.suggerisciParola(parolaField.getValue());
                parolaField.clear();
            }
        });
        this.addComponents(parolaField,suggerisci);
    }

    public void aggiornaParole(String parola){
        if(parole.containsKey(parola)) {
            parole.get(parola).aggionaNumero();
        } else {
            ParolaLayout parolaLayout= new ParolaLayout(parola, broadcaster);
            this.addComponent(parolaLayout);
            parole.put(parola, parolaLayout);
        }
    }

    public void ripulisci(){
        parole.forEach((s, components1) -> {
            this.removeComponent(components1);
            parole.remove(components1);
        });
    }
}
