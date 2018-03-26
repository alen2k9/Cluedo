/*
 * Code to handle the dice and rolling of dice.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Random;
public class Dice extends JPanel{

    private Die leftDice;
    private Die rightDice;
    private int leftVal;
    private int rightVal;


    public Dice() {

        leftDice = new Die();
        rightDice = new Die();


        JPanel dicePanel = new JPanel();
        dicePanel.setLayout(new GridLayout(1, 2, 4, 0));
        dicePanel.add(leftDice);
        dicePanel.add(rightDice);

        this.setLayout(new BorderLayout());

        this.add(dicePanel , BorderLayout.CENTER);
    }

    public int rolldice()
    {
        leftVal = leftDice.roll();
        rightVal = rightDice.roll();
        return leftVal + rightVal;
    }

}

class Die extends JPanel {

    private int val;
    private int d = 9;

    private static Random random = new Random();


    public Die() {
        setBackground(Color.white);

        setPreferredSize(new Dimension(60,60));
        roll();
    }

    public int roll() {
        int val = random.nextInt(6) + 1; // Range 1-6
        setValue(val);
        return val;
    }

    public int getValue() {
        return val;
    }

    public void setValue(int spots) {
        val = spots;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // required
        int w = getWidth();
        int h = getHeight(); // should use to resize spots too.
        switch (val) {
            case 1: drawSpot(g, w/2, h/2);
                break;
            case 3: drawSpot(g, w/2, h/2);

            case 2: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                break;
            case 5: drawSpot(g, w/2, h/2);

            case 4: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                drawSpot(g, 3*w/4, h/4);
                drawSpot(g, w/4, 3*h/4);
                break;
            case 6: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                drawSpot(g, 3*w/4, h/4);
                drawSpot(g, w/4, 3*h/4);
                drawSpot(g, w/4, h/2);
                drawSpot(g, 3*w/4, h/2);
                break;
        }
    }
    private void drawSpot(Graphics g, int x, int y) {
        g.fillOval(x-d/2, y-d/2, d, d);
    }
}
