/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */package com.team11.cluedo.controls;

import java.util.Random;

public class Dice {



    public Dice()
    {
        rolldice();

    }


    public int rolldice()
    {
        Random rand = new Random();
        return rand.nextInt(11) + 2;
    }
}
