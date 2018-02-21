/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */package com.team11.cluedo.controls;

import java.util.Random;

public class Dice {

    private int dice;

    public Dice()
    {
        Random rand = new Random();
        dice = rand.nextInt(11) + 2;
    }

    public int getdice()
    {
        return dice;
    }
}
