package com.example.provaH2.guess;

import com.example.provaH2.UI.PlayUI;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.Game;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.layout.GuessPartitaLayout;
import org.springframework.stereotype.Component;

@Component
public class GuessGioco implements Game {
    @Override
    public String getNomeGioco() {
        return "Guess";
    }

    @Override
    public Class getControllerClass() {
        return GameController.class;
    }

    @Override
    public Class<? extends Broadcaster> getBroadcasterClass() {
        return BroadcasterGuess.class;
    }

    @Override
    public Class<? extends PartitaLayout> getParitaLayoutClass() {
        return GuessPartitaLayout.class;
    }

    //TODO: questa va cambiata
    @Override
    public String getPathName() {
        return PlayUI.BASE_PATH+"guess";
    }
}
