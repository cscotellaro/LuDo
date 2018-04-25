package com.example.provaH2.prova;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomSpringEventListener /*implements ApplicationListener<CustomSpringEvent> */{
    /*@Override
    public void onApplicationEvent(CustomSpringEvent event) {
        System.out.println("Received spring custom event - " + event.getMessage());
    }*/

    private String cinzia="sono un messaggio";
    public final int mioNumero=4;

    @EventListener(condition = "#event.numero==mioNumero")
    public void listener(CustomSpringEvent event) {
        System.out.println("Received spring custom event- " + event.getMessage());
    }
}