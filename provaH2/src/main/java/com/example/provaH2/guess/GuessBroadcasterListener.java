package com.example.provaH2.guess;

import java.util.ArrayList;

public interface GuessBroadcasterListener {
    void receiveIndizio(String message);
    void parolaSuggerita(String parola);
    void onVoteParola(String parola);
    void onUnvoteParola(String parola);
    void reciveChatMessage(String name, String message);
    void parolaGiaSuggerita();
    void countDown(int n);
}
