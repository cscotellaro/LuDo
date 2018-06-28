package com.example.provaH2.gestioneGioco;

public interface Game {

    String getNomeGioco();
    Class<?extends Controller > getControllerClass();
    Class <? extends Broadcaster> getBroadcasterClass();
    Class<? extends  PartitaLayout> getParitaLayoutClass();
    String getPathName();

}
