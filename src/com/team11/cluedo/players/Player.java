package com.team11.cluedo.players;


import com.team11.cluedo.suspects.Suspect;

import java.awt.*;

public class Player {
    private String playerName;
    private Suspect suspectToken;
    private Image cardImage, selectedCardImage;

    public Player(String playerName, Suspect suspectToken, Image cardImage, Image selectedCardImage) {
        this.playerName = playerName;
        this.suspectToken = suspectToken;
        this.cardImage = cardImage;
        this.selectedCardImage = selectedCardImage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Suspect getSuspectToken() {
        return suspectToken;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public Image getSelectedCardImage() {
        return selectedCardImage;
    }
}