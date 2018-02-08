package com.Team11.Cluedo.Controls;

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
