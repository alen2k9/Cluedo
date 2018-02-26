package com.team11.cluedo.cards;

import java.awt.*;

public class Card {
    private Image cardImage, selectedCardImage;
    private String name;

    public Card() {

    }

    public Card(Image cardImage, Image selectedCardImage, String name) {
        this.cardImage = cardImage;
        this.selectedCardImage = selectedCardImage;
        this.name = name;
    }

    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }

    public void setSelectedCardImage(Image selectedCardImage) {
        this.selectedCardImage = selectedCardImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public Image getSelectedCardImage() {
        return selectedCardImage;
    }

    public String getName() {
        return name;
    }
}
