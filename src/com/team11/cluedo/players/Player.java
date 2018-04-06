/*
  Code to handle the player object of each player.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */
package com.team11.cluedo.players;


import com.team11.cluedo.cards.Card;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.Resolution;

import java.awt.*;

public class Player {
    private String playerName;
    private Suspect suspectToken;
    private Image cardImage, selectedCardImage;
    private PlayerHand playerHand;
    private Notes playerNotes;
    private boolean canQuestion;

    public Player(String playerName, Suspect suspectToken, Image cardImage, Image selectedCardImage, Resolution resolution) {
        this.playerName = playerName;
        this.suspectToken = suspectToken;
        this.cardImage = cardImage;
        this.selectedCardImage = selectedCardImage;
        this.playerHand = new PlayerHand();
        this.playerNotes = new Notes(resolution);
        this.canQuestion = false;
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

    public void fillNotes(){
        for (int i = 0; i < this.playerHand.getCardAmount(); i++){
            this.getPlayerNotes().paintCell(getSuspectToken().getSuspectID(), playerHand.getCard(i).getName(), 0);
        }
    }
}