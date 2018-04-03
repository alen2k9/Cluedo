package com.team11.cluedo.accustations;

public class Bounds {
    private int x;
    private int y;
    private int cardSelectWidth;
    private int cardSelectHeight;
    public Bounds(int x, int y, int cardSelectWidth, int cardSelectHeight)
    {
        this.x = x;
        this.y = y;
        this.cardSelectWidth = cardSelectWidth;
        this.cardSelectHeight = cardSelectHeight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCardSelectWidth() {
        return cardSelectWidth;
    }

    public int getCardSelectHeight() {
        return cardSelectHeight;
    }
}
