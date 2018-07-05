package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Account;
import com.vaadin.ui.Embedded;

import java.util.ArrayList;
import java.util.HashMap;

public interface BroadcastListener {
    void countUser(int i, HashMap<String, Embedded> accountImg);
    void gameStarted(Class<? extends  PartitaLayout> partitaClass,int numPlayers , HashMap<String ,Embedded> playersImg);
    void fineDellaPartita(boolean haiVinto, Object parola);
    void registratoDiNuovo();
    String getName();
    Account getAccount();
    Embedded getProfileImage();
}
