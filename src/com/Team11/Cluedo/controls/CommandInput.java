package com.Team11.Cluedo.controls;

import com.Team11.Cluedo.suspects.Direction;
import com.Team11.Cluedo.ui.GameScreen;
import com.Team11.Cluedo.weapons.Weapons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CommandInput {
    private GameScreen mainPanel;
    private Weapons weaponGame;

    public CommandInput(GameScreen mainPanel, Weapons weaponGame) {
        this.mainPanel = mainPanel;
        this.weaponGame = weaponGame;
        mainPanel.getInfoOutput().append("Welcome to Cluedo\nEnter 1 to move Players\nEnter 2 to move Weapons\n");
        //initialSetup();
        introduction();
    }

    private void initialSetup()
    {
        mainPanel.getInfoOutput().append("\nPlease enter 1 to move player\n");
        mainPanel.getInfoOutput().append("Please enter 2 to move Weapons\n");
    }

    private void introduction()
    {
        boolean doOnce = false;

        mainPanel.getTestButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainPanel.setInput(mainPanel.getCommandInput().getText() + '\n');
                //initialSetup();

                    if (mainPanel.getCommandInput().getText().equals("1")) {
                        mainPanel.reDraw();
                        mainPanel.getInfoOutput().append(mainPanel.getInput());
                        //mainPanel.getInfoOutput().append("Welcome\n");
                        mainPanel.getInfoOutput().append("Please enter the following\n ");
                        mainPanel.getInfoOutput().append("l - Left\n r - Right\n u - Up\n d - Down\n 'back' - Return to Options ");
                        playerMovement();
                        mainPanel.getCommandInput().setText("");

                    } else if (mainPanel.getCommandInput().getText().equals("2")) {
                        mainPanel.reDraw();
                        mainPanel.getInfoOutput().append(mainPanel.getInput());
                        //mainPanel.getInfoOutput().append("Welcome\n");
                        mainPanel.getInfoOutput().append("Please enter the following\n ");
                        mainPanel.getInfoOutput().append("Rooms to choose from:\n- Kitchen\n- Ballroom\n- Conservatory\n- Biliard Room\n- Library\n" +
                                "- Study\n- Hall \n- Lounge\n- Dining Room \n- Cellar\n\n");
                        mainPanel.getInfoOutput().append("Weapons to choose from\n");
                        mainPanel.getInfoOutput().append("- Candlestick\n- Dagger\n- Lead Pipe\n- Revolver\n- Rope\n- Spanner");
                        weaponMovement();
                    }

                    else if(mainPanel.getCommandInput().getText().equals("")){
                        mainPanel.reDraw();
                    }

                    mainPanel.getCommandInput().setText("");
            }
        });

        if (!doOnce){
            mainPanel.getTestButton().doClick();
            doOnce = true;
        }

    }

    public void playerTurn(int currentUser)
    {
        Boolean endTurn = false;
        while (!endTurn) {
            if(/* SOME CONDITION FROM COMPLETING MOVE*/ this.mainPanel.getGamePlayers().getRemainingPlayers() < 2) {
                endTurn = true;
            }
            /*
            TO DO command input, then push into each options below
             */
        }
        this.mainPanel.reDraw();
    }

    public /*int*/ void Diceroll(){
        //Roll dice and animate using Dice class
        //return number of dice roll for playerMovement function
    }

    private void weaponMovement()
    {
        ChooseWeapon choice = new ChooseWeapon();
        int weapon = 0;
        int room = 0;

        if (choice.getWeapon() != null){
            //Moving Weapons

            switch (choice.getRoom()){
                case ("Kitchen"):
                    room = 0;
                    break;
                case ("Ballroom"):
                    room = 1;
                    break;
                case ("Dining Room"):
                    room = 2;
                    break;
                case ("Billiard Room"):
                    room = 3;
                    break;
                case ("Library"):
                    room = 4;
                    break;
                case("Lounge"):
                    room = 5;
                    break;
                case ("Hall"):
                    room = 6;
                    break;
                case ("Study"):
                    room = 7;
                    break;
                case ("Conservatory"):
                    room = 8;
                    break;
                default:
                    System.out.println("Unknown Room");

            }

            switch (choice.getWeapon()){
                case ("Candlestick"):
                    weapon = 0;
                    break;
                case ("Dagger"):
                    weapon = 1;
                    break;
                case ("Lead Pipe"):
                    weapon = 2;
                    break;
                case ("Revolver"):
                    weapon = 3;
                    break;
                case ("Rope"):
                    weapon = 4;
                    break;
                case ("Spanner"):
                    weapon = 5;
                    break;

                default:
                    System.out.println("Unknown Weapon");
                    break;
            }

            weaponGame.moveWeaponToRoom(weapon, room);
            mainPanel.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            mainPanel.reDraw();

        }

        else{
            mainPanel.getInfoOutput().append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }

    private void playerMovement()
    {
        /*
        Player movement function found in: this.mainPanel.getGamePlayers().playerMove(PLAYER, DIRECTION, AMUONT);
         */
        mainPanel.getTestButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    if(mainPanel.getCommandInput().getText().equals("u"))
                    {
                        mainPanel.getInfoOutput().append("\nUp");
                        //playerMove character up
                        mainPanel.getGamePlayers().playerMove(1, Direction.NORTH, 1);
                        mainPanel.reDraw();
                        mainPanel.getCommandInput().setText("");
                        playerMovement();
                    }
                    else if(mainPanel.getCommandInput().getText().equals("d"))
                    {
                        mainPanel.getInfoOutput().append("\nDown");
                        //playerMove character down
                        mainPanel.getGamePlayers().playerMove(1, Direction.SOUTH, 1);
                        mainPanel.reDraw();
                        mainPanel.getCommandInput().setText("");
                        playerMovement();
                    }
                    else if(mainPanel.getCommandInput().getText().equals("l"))
                    {
                        mainPanel.getInfoOutput().append("\nLeft");
                        //playerMove character left
                        mainPanel.getGamePlayers().playerMove(1, Direction.WEST, 1);
                        mainPanel.reDraw();
                        mainPanel.getCommandInput().setText("");
                        playerMovement();
                    }
                    else if(mainPanel.getCommandInput().getText().equals("r"))
                    {
                        mainPanel.getInfoOutput().append("\nRight");
                        //playerMove character right
                        mainPanel.getGamePlayers().playerMove(1, Direction.EAST, 1);
                        mainPanel.reDraw();
                        mainPanel.getCommandInput().setText("");
                        playerMovement();
                    }

                    else if (mainPanel.getCommandInput().getText().equals("back")){
                        mainPanel.getInfoOutput().append("\n\nReturning to Main Menu\nEnter 1 to Move\nEnter 2 to move a weapon\n");
                        mainPanel.getCommandInput().setText("");
                        System.out.println("Returning to main menu");
                        introduction();
                    }




                mainPanel.getCommandInput().setText("");
            }
        });
    }


    private class ChooseWeapon{
        private String weapon;
        private String room;

        private ChooseWeapon()
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

        private String getRoom() {
            return room;
        }

        private String getWeapon() {
            return weapon;
        }
    }
}
