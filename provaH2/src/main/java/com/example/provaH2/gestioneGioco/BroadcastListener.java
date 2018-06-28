package com.example.provaH2.gestioneGioco;

import com.vaadin.ui.Embedded;

import java.util.ArrayList;
import java.util.HashMap;

public interface BroadcastListener {
    void countUser(int i, ArrayList<String> utenti);
    void gameStarted(Class<? extends  PartitaLayout> partitaClass,int numPlayers , HashMap<String ,Embedded> playersImg);
    void fineDellaPartita(boolean haiVinto, Object parola);
    void registratoDiNuovo();
    String getName();
    Embedded getProfileImage();
}
