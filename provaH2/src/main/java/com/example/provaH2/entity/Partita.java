package com.example.provaH2.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//TODO: tesò ma vogliamo mettere anche un riferimeto a chi ha creato la partita?

@Entity
public class Partita {
    @Id @GeneratedValue
    private Long id;
    private Timestamp timestamp;
    private boolean vinta;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Voto> array;

    public Partita(){
        array=new ArrayList<Voto>();
    }

    public Partita(Timestamp timestamp, boolean vinta) {
        this.timestamp = timestamp;
        this.vinta = vinta;
        array= new ArrayList<>();
    }

    public Partita(Timestamp timestamp) {
        this.timestamp = timestamp;
        array= new ArrayList<>();
    }

    public Partita(Timestamp timestamp, boolean vinta, List<Voto> array) {
        this.timestamp = timestamp;
        this.vinta = vinta;
        this.array = array;
    }

    public void addVoto(Voto voto){
        array.add(voto);
    }

    public List<Voto> getArray() {
        return array;
    }

    public void setArray(List<Voto> array) {
        this.array = array;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVinta() {
        return vinta;
    }

    public void setVinta(boolean vinta) {
        this.vinta = vinta;
    }

    @Override
    public String toString() {
        return "Partita{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", vinta=" + vinta +
                ", array=" + array +
                '}';
    }
}
