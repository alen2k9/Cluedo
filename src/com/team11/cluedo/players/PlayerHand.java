/*
  Code to handle the player's hand of cards.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
*/

package com.team11.cluedo.players;

import com.team11.cluedo.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {
    private final List<Card> playerHand = new ArrayList<>();

    public PlayerHand() {
    }

    public void addCardToHand(Card card) {
        playerHand.add(card);
    }

    public Card getCard(int index) {
        return playerHand.get(index);
    }

    public int getCardAmount() {
        return playerHand.size();
    }
}
