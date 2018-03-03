/*
 * Code to handle the creation of the room cards.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

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
