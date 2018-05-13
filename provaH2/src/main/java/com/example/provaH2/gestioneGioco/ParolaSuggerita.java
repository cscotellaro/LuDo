package com.example.provaH2.gestioneGioco;

import java.io.Serializable;

public class ParolaSuggerita implements Serializable {
    private int voti;
    private String parola;

    public ParolaSuggerita(int voti, String parola) {
        this.voti = voti;
        this.parola = parola;

    }

    public ParolaSuggerita(String parola) {
        this.parola = parola;
        voti=0;

    }

    public  synchronized int getVoti() {
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

    public synchronized void incrementaVoto(){
        voti++;
    }

    public synchronized void decrementaVoto(){
        if(voti>0){
            voti--;
        }
    }

    @Override
    public String toString() {
        return "ParolaSuggerita{" +
                "voti=" + voti +
                ", parola='" + parola + '\'' +
                '}';
    }
}
