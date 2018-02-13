/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.Team11.Cluedo.Controls;

import javax.swing.*;

public class ChoiceOption {

    private String weapon;
    private String room;

    public ChoiceOption()
    {
        makeChoice();
    }

    private void makeChoice()
    {
        String[] weaponChoice = { "Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner" };
        String[] roomChoice = { "Kitchen", "Ballroom", "Conservatory", "Billiard Room", "Library", "Study", "Hall", "Lounge", "Dining Room", "Cellar" };

        weapon = (String) JOptionPane.showInputDialog(null, "Choose the Weapon you want to move",
                "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, weaponChoice, weaponChoice[0]);


        if (weapon != null){
            room = (String) JOptionPane.showInputDialog(null, "Choose the Room you want to move it into",
                    "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, roomChoice, roomChoice[0]);
        }

        else{
            System.out.println("Cancelling weapon movement");
        }

    }

    public String getRoom() {
        return room;
    }

    public String getWeapon() {
        return weapon;
    }

  }
