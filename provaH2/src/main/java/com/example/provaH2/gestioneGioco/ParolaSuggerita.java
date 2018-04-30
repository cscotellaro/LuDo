package com.example.provaH2.gestioneGioco;

import java.io.Serializable;

public class ParolaSuggerita implements Serializable {
    private int voti;
    private String parola;
    private boolean alreadyVoted;

    public ParolaSuggerita(int voti, String parola) {
        this.voti = voti;
        this.parola = parola;
        alreadyVoted=false;
    }

    public ParolaSuggerita(String parola) {
        this.parola = parola;
        voti=1;
        alreadyVoted=false;
    }

    public boolean isAlreadyVoted() {
        return alreadyVoted;
    }

    public void setAlreadyVoted(boolean alreadyVoted) {
        this.alreadyVoted = alreadyVoted;
    }

    public int getVoti() {
        return voti;
    }

    public void setVoti(int voti) {
        this.voti = voti;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public void incrementaVoto(){
        voti++;
    }

    @Override
    public String toString() {
        return "ParolaSuggerita{" +
                "voti=" + voti +
                ", parola='" + parola + '\'' +
                '}';
    }
}
