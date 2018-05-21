package com.example.provaH2.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Partita {
    @Id @GeneratedValue
    private Long id;
    private String nome;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Voto> array;

    public Partita(){
        array=new ArrayList<Voto>();
    }

    public Partita(String nome) {
        this.nome = nome;
        array=new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void add(Voto voto){
        array.add(voto);
    }

    @Override
    public String toString() {
        return "Partita{" +
                "nome='" + nome + '\'' +
                ", array=" + array +
                '}';
    }
}
