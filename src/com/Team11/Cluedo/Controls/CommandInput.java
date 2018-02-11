package com.Team11.Cluedo.Controls;

import com.Team11.Cluedo.Suspects.Direction;
import com.Team11.Cluedo.UI.GameScreen;
import com.Team11.Cluedo.Weapons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CommandInput {
    private GameScreen mainPanel;
    private Weapons weaponGame;

    public CommandInput(GameScreen mainPanel, Weapons weaponGame) {
        this.mainPanel = mainPanel;
        this.weaponGame = weaponGame;
        mainPanel.getInfoOutput().append("Welcome to Cluedo\n");
        initialSetup();
        introduction();
    }

    private void initialSetup()
    {
        mainPanel.getInfoOutput().append("Please enter 1 to move player\n");
        mainPanel.getInfoOutput().append("Please enter 2 to move Weapons\n");
    }



    private void introduction()
    {
        mainPanel.getTestButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainPanel.setInput( mainPanel.getCommandInput().getText() + '\n');

                if(mainPanel.getCommandInput().getText().equals("1"))
                {
                    mainPanel.reDraw();
                    mainPanel.getInfoOutput().append(mainPanel.getInput());
                    mainPanel.getInfoOutput().append("Welcome\n");
                    mainPanel.getInfoOutput().append("Please enter the following\n ");
                    mainPanel.getInfoOutput().append("l - Left\n r - Right\n u - Up\n d - Down ");
                    playerMovement();
                    mainPanel.getCommandInput().setText("");

                }

                else if(mainPanel.getCommandInput().getText().equals("2"))
                {
                    mainPanel.reDraw();
                    mainPanel.getInfoOutput().append(mainPanel.getInput());
                    mainPanel.getInfoOutput().append("Welcome\n");
                    mainPanel.getInfoOutput().append("Please enter the following\n ");
                    mainPanel.getInfoOutput().append("Rooms to choose from:\n- Kitchen\n- Ballroom\n- Conservatory\n- Biliard Room\n- Library\n" +
                            "- Study\n- Hall \n- Lounge\n- Dining Room \n- Cellar\n\n");
                    mainPanel.getInfoOutput().append("Weapons to choose from\n");
                    mainPanel.getInfoOutput().append("- Candlestick\n- Dagger\n- Lead Pipe\n- Revolver\n- Rope\n- Spanner");
                    weaponMovement();


                }
                mainPanel.getCommandInput().setText("");
            }
        });

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

    public void weaponMovement()
    {
        ChoiceOption choice = new ChoiceOption();
        mainPanel.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
        int weapon = 0;
        int room = 0;


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
            room = 8;
        }
        else if (choice.getRoom().equals("Billiard Room"))
        {
            room = 3;
        }
        else if (choice.getRoom().equals("Library"))
        {
            room = 4;
        }
        else if (choice.getRoom().equals("Study"))
        {
            room = 7;
        }
        else if (choice.getRoom().equals("Hall"))
        {
            room = 6;
        }
        else if (choice.getRoom().equals("Lounge"))
        {
            room = 5;
        }
        else if (choice.getRoom().equals("Dining Room"))
        {
            room = 2;
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


        weaponGame.moveWeaponToRoom(weapon, room);
        mainPanel.reDraw();

    }

    public void playerMovement()
    {
        /*
        Player movement function found in: this.mainPanel.getGamePlayers().playerMove(PLAYER, DIRECTION, AMUONT);
         */
        mainPanel.getTestButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while(mainPanel.getCommandInput().getText().equals("u") || mainPanel.getCommandInput().getText().equals("d")
                        || mainPanel.getCommandInput().getText().equals("l") || mainPanel.getCommandInput().getText().equals("r"))
                {
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



                }
                mainPanel.getCommandInput().setText("");
            }
        });
    }
}
