package com.example.provaH2.gestioneGioco;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Punteggio;
import com.vaadin.ui.Embedded;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface BroadcastListener {
    void countUser(int i, HashMap<String, Embedded> accountImg);
    void gameStarted(Class<? extends  PartitaLayout> partitaClass,int numPlayers , HashMap<String ,Embedded> playersImg);
    void fineDellaPartita(boolean haiVinto, List<Punteggio> punteggi, Object parola);
    void registratoDiNuovo();
    String getName();
    Account getAccount();
    Embedded getProfileImage();
    void hostLost();
}
