package com.example.provaH2.gestioneGioco;

public class DuplicateGameNameException extends RuntimeException {
    public DuplicateGameNameException(String message) {
        super(message);
    }
}
