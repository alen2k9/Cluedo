package com.team11.cluedo.players;


import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.Resolution;

import java.awt.*;

public class Player {
    private String playerName;
    private Suspect suspectToken;
    private Image cardImage, selectedCardImage;
    private PlayerHand playerHand;
    private Notes playerNotes;

    public Player(String playerName, Suspect suspectToken, Image cardImage, Image selectedCardImage, Resolution resolution) {
        this.playerName = playerName;
        this.suspectToken = suspectToken;
        this.cardImage = cardImage;
        this.selectedCardImage = selectedCardImage;
        this.playerHand = new PlayerHand();
        this.playerNotes = new Notes(resolution);
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

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public Notes getPlayerNotes(){
        return this.playerNotes;
    }
}