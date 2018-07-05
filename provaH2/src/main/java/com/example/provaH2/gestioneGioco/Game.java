package com.example.provaH2.gestioneGioco;

import java.nio.file.Path;

public interface Game {

    String getNomeGioco();
    Class<?extends Controller > getControllerClass();
    Class <? extends Broadcaster> getBroadcasterClass();
    Class<? extends  PartitaLayout> getParitaLayoutClass();
    /**questo è il path della ui*/
    String getPathName();
    /**la path va data a  partire dal nome del package di gioco tipo guess/<nomeImagine>*/
    Path getImagePath();
    String getDescrizioneGioco();
    String getDescrizioneLungaGioco();
}
