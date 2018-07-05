package com.example.provaH2.guess;

import com.example.provaH2.UI.PlayUI;
import com.example.provaH2.gestioneGioco.Broadcaster;
import com.example.provaH2.gestioneGioco.Game;
import com.example.provaH2.gestioneGioco.PartitaLayout;
import com.example.provaH2.guess.layout.GuessPartitaLayout;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    //DONE: questa va cambiata
    @Override
    public String getPathName() {
        return PlayUI.BASE_PATH+"guess";
    }

    @Override
    public Path getImagePath() {
        return  Paths.get("guess/guess.jpeg");
    }

    @Override
    public String getDescrizioneGioco() {
        return "questa è la descizione del gioco Guess: Indovina le parole dagli indizi che compaiono man mano";
    }

    @Override
    public String getDescrizioneLungaGioco() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                +"Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                +" Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
                +" Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    }
}
