/*
 * Code to handle the superClass card.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.cards;

import java.awt.*;

public class Card {
    private Image cardImage, selectedCardImage;
    private String name, id;

    public Card() {
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

    public void setID(String id) {
        this.id = id;
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

    public String getID() {
        return id;
    }
}
