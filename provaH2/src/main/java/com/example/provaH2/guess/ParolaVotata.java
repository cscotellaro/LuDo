package com.example.provaH2.guess;

import java.util.ArrayList;

public class ParolaVotata {

    private String parolaSuggerita;
    private ArrayList<Long> chiHaVotato;
    private Long chiHaSuggerito;

    public ParolaVotata(String parolaSuggerita, Long chiHaSuggerito) {
        this.parolaSuggerita = parolaSuggerita;
        this.chiHaSuggerito = chiHaSuggerito;
        chiHaVotato= new ArrayList<>();
    }

    public String getParolaSuggerita() {
        return parolaSuggerita;
    }

    public void setParolaSuggerita(String parolaSuggerita) {
        this.parolaSuggerita = parolaSuggerita;
    }

    public Long getChiHaSuggerito() {
        return chiHaSuggerito;
    }

    public void setChiHaSuggerito(Long chiHaSuggerito) {
        this.chiHaSuggerito = chiHaSuggerito;
    }

    public void addVoto(Long id){
        chiHaVotato.add(id);
    }

    public  void removeVoto(Long id){
        chiHaVotato.remove(id);
    }

    public int getNumeroVoti(){
        return chiHaVotato.size();
    }
}
