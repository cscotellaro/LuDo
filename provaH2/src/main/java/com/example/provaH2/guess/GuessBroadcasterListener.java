package com.example.provaH2.guess;

import java.util.ArrayList;
//TODO: Cinzia la vogliamo lasciare o la leviamo e lo facciamo con un semplice casting?
public interface GuessBroadcasterListener {
    void receiveIndizio(String message);
    void parolaSuggerita(String parola);
    void onVoteParola(String parola);
    void onUnvoteParola(String parola);
    void reciveChatMessage(String name, String message);
    void parolaGiaSuggerita();
}
