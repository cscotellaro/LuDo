package com.example.provaH2.UI;

public interface PuoSuggerire {

    void suggerisci(String parola);
    void voteParola(String parola);
    void unvoteParola(String parola);

    void sendChat(String message);
}
