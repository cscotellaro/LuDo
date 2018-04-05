package com.example.provaH2.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class Item {

    @Id @GeneratedValue
    private long id;
    private String indizi[];
    private String parola;

    public Item(){}

    public Item(String[] indizi, String parola) {
        this.indizi = indizi;
        this.parola = parola;
    }

    //TODO: Cinzia ma qua va bene il getter e il setter?
    public String[] getIndizi() {
        return indizi;
    }

    public void setIndizi(String[] indizi) {
        this.indizi = indizi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", indizi=" + Arrays.toString(indizi) +
                ", parola='" + parola + '\'' +
                '}';
    }
}
