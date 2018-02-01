package com.Team11.Cluedo.Controls;

import java.util.Random;

public class Dice {

    int dice;

    public void roll()
    {
        Random rand = new Random();
        this.dice = rand.nextInt(10) + 2;
    }

    public int getdice()
    {
        return this.dice;
    }





}
