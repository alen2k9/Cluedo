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
