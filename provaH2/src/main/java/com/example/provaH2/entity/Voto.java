package com.example.provaH2.entity;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Embeddable
public class Voto {

    @OneToOne
    private Account account;
    private int punti;

    public Voto (){}

    public Voto(Account account, int punti) {
        this.account = account;
        this.punti = punti;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "account=" + account +
                ", punti=" + punti +
                '}';
    }
}
