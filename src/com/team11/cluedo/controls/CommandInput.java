/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.controls;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.weapons.Weapons;

import javax.swing.*;
import java.util.ArrayList;


public class CommandInput {
    private GameScreen mainPanel;
    private Weapons gameWeapons;
    private Board gameBoard;

    private int currentPlayer;

    public CommandInput(GameScreen mainPanel, Weapons gameWeapons, Board gameBoard) {
        this.mainPanel = mainPanel;
        this.gameWeapons = gameWeapons;
        this.gameBoard = gameBoard;
        currentPlayer = 0;
    }

    public void initialSetup()
    {
        mainPanel.getInfoOutput().append("\nPlease enter 1 to move player\n");
        mainPanel.getInfoOutput().append("Please enter 2 to move Weapons\n");
    }

    public void introduction()
    {
        boolean doOnce = false;

        mainPanel.getCommandInput().addActionListener(e -> {
            mainPanel.setInput(mainPanel.getCommandInput().getText() + '\n');
            //initialSetup();
                if (mainPanel.getCommandInput().getText().equals("1")) {
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getInfoOutput().append(mainPanel.getInput());
                    //mainPanel.getInfoOutput().append("Welcome\n");
                    mainPanel.getInfoOutput().append("Please enter the following\n ");
                    mainPanel.getInfoOutput().append("l - Left\n r - Right\n u - Up\n d - Down\n 'back' - Return to Options ");
                    playerMovement();
                    mainPanel.getCommandInput().setText("");

                } else if (mainPanel.getCommandInput().getText().equals("2")) {
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getInfoOutput().append(mainPanel.getInput());
                    //mainPanel.getInfoOutput().append("Welcome\n");
                    mainPanel.getInfoOutput().append("Please enter the following\n ");
                    mainPanel.getInfoOutput().append("Rooms to choose from:\n- Kitchen\n- Ballroom\n- Conservatory\n- Biliard WeaponPoints\n- Library\n" +
                            "- Study\n- Hall \n- Lounge\n- Dining WeaponPoints \n- Cellar\n\n");
                    mainPanel.getInfoOutput().append("Weapons to choose from\n");
                    mainPanel.getInfoOutput().append("- Candlestick\n- Dagger\n- Lead Pipe\n- Revolver\n- Rope\n- Spanner");
                    weaponMovement();
                } else if (mainPanel.getCommandInput().getText().equals("4")){
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getInfoOutput().append((mainPanel.getInput()));
                    playerMovement();
                }

                else if(mainPanel.getCommandInput().getText().equals("")){
                    mainPanel.reDraw(currentPlayer);
                }

                mainPanel.getCommandInput().setText("");
        });

        if (!doOnce){
            //mainPanel.getInfoOutput().act
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
        this.mainPanel.reDraw(currentUser);
    }

    public /*int*/ void Diceroll(){
        //Roll dice and animate using Dice class
        //return number of dice roll for playerMovement function
    }

    private class ChoiceOption {

        private String weapon;
        private String room;

        private ChoiceOption()
        {
            makeChoice();
        }

        private void makeChoice()
        {
            String[] weaponChoice = { "Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner" };
            String[] roomChoice = { "Kitchen", "Ballroom", "Conservatory", "Billiard", "Library", "Study", "Hall", "Lounge", "Dining WeaponPoints", "Cellar" };

            weapon = (String) JOptionPane.showInputDialog(null, "Choose the Weapon you want to move",
                    "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, weaponChoice, weaponChoice[0]);


            if (weapon != null){
                room = (String) JOptionPane.showInputDialog(null, "Choose the WeaponPoints you want to move it into",
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

    private void weaponMovement()
    {
        ChoiceOption choice = new ChoiceOption();
        int weapon = 0;
        int room = 0;

        if (choice.getWeapon() != null){
            /*
             * Moving Weapon
             */
            if(choice.getRoom().equals("Kitchen"))      //move weapon based on user choice as chosen on joption Pane
            {
                room = 0;
            }
            else if (choice.getRoom().equals("Ballroom"))
            {
                room = 1;
            }
            else if (choice.getRoom().equals("Conservatory"))
            {
                room = 2;
            }
            else if (choice.getRoom().equals("Dining"))
            {
                room = 3;
            }
            else if (choice.getRoom().equals("Billiard"))
            {
                room = 4;
            }
            else if (choice.getRoom().equals("Library"))
            {
                room = 5;
            }
            else if (choice.getRoom().equals("Lounge"))
            {
                room = 6;
            }
            else if (choice.getRoom().equals("Hall"))
            {
                room = 7;
            }
            else if (choice.getRoom().equals("Study"))
            {
                room = 8;
            }

            //private String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};

            if (choice.getWeapon().equals("Candlestick"))
            {
                weapon = 0;
            }
            else if(choice.getWeapon().equals("Dagger"))
            {
                weapon = 1;
            }
            else if(choice.getWeapon().equals("Lead Pipe"))
            {
                weapon = 2;
            }
            else if(choice.getWeapon().equals("Revolver"))
            {
                weapon = 3;
            }
            else if(choice.getWeapon().equals("Rope"))
            {
                weapon = 4;
            }
            else if(choice.getWeapon().equals("Spanner"))
            {
                weapon = 5;
            }

            gameWeapons.moveWeaponToRoom(weapon, room);
            mainPanel.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            mainPanel.reDraw(currentPlayer);

        }

        else{
            mainPanel.getInfoOutput().append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }

    private void playerMovement()
    {

        ArrayList<Direction> list = new ArrayList<>();
        list.add(Direction.NORTH);
        list.add(Direction.NORTH);
        list.add(Direction.NORTH);
        list.add(Direction.NORTH);
        list.add(Direction.NORTH);
        list.add(Direction.NORTH);
        list.add(Direction.WEST);
        list.add(Direction.SOUTH);
        /*
        Player movement function found in: this.mainPanel.getGamePlayers().playerMove(PLAYER, DIRECTION, AMUONT);
         */
        mainPanel.getCommandInput().addActionListener(e -> {
                if(mainPanel.getCommandInput().getText().equals("u"))
                {
                    mainPanel.getInfoOutput().append("\nUp");
                    //playerMove character up
                    //mainPanel.getGamePlayers().playerMove(gameBoard, currentPlayer, Direction.NORTH);
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getCommandInput().setText("");
                    playerMovement();
                }
                else if(mainPanel.getCommandInput().getText().equals("d"))
                {
                    mainPanel.getInfoOutput().append("\nDown");
                    //playerMove character down
                    //mainPanel.getGamePlayers().playerMove(gameBoard,currentPlayer, Direction.SOUTH, 1);
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getCommandInput().setText("");
                    playerMovement();
                }
                else if(mainPanel.getCommandInput().getText().equals("l"))
                {
                    mainPanel.getInfoOutput().append("\nLeft");
                    //playerMove character left
                    //mainPanel.getGamePlayers().playerMove(gameBoard, currentPlayer, Direction.WEST);
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getCommandInput().setText("");
                    playerMovement();
                }
                else if(mainPanel.getCommandInput().getText().equals("r"))
                {
                    mainPanel.getInfoOutput().append("\nRight");
                    //playerMove character right
                    //mainPanel.getGamePlayers().playerMove(gameBoard, currentPlayer, Direction.EAST, 1);
                    mainPanel.reDraw(currentPlayer);
                    mainPanel.getCommandInput().setText("");
                    playerMovement();
                }
                else if (mainPanel.getCommandInput().getText().equals("back")){
                    mainPanel.getInfoOutput().append("\n\nReturning to Main Menu\nEnter 1 to Move\nEnter 2 to move a weapon\n");
                    mainPanel.getCommandInput().setText("");
                    System.out.println("Returning to main menu");
                    introduction();
                }
                else if (mainPanel.getCommandInput().getText().equals("test")){
                    mainPanel.getGamePlayers().playerMove(gameBoard, currentPlayer, list);
                }
            mainPanel.getCommandInput().setText("");
        });
    }
}
