package com.example.provaH2.prova;

import org.springframework.context.ApplicationEvent;

public class CustomSpringEvent extends ApplicationEvent {
    private String message;
    private int numero;

    public CustomSpringEvent(Object source, String message, int numero) {
        super(source);
        this.message = message;
        this.numero=numero;
    }
    public String getMessage() {
        return message;
    }

    public int getNumero(){
        return numero;
    }
}
