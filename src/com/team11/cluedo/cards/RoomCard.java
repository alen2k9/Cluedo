package com.team11.cluedo.cards;

import java.awt.*;

public class RoomCard extends Card {
    public RoomCard(Image cardImage, Image selectedCardImage, String name) {
        super.setCardImage(cardImage);
        super.setSelectedCardImage(selectedCardImage);
        super.setName(name);
    }

    @Override
    public Image getCardImage() {
        return super.getCardImage();
    }

    @Override
    public Image getSelectedCardImage() {
        return super.getSelectedCardImage();
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
