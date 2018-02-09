package com.Team11.Cluedo.Controls;

import com.Team11.Cluedo.Suspects.Direction;
import com.Team11.Cluedo.UI.GameScreen;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CommandInput {
    private GameScreen mainPanel;

    public CommandInput(GameScreen mainPanel) {
        this.mainPanel = mainPanel;
        initialSetup();
    }

    private void initialSetup()
    {
        mainPanel.getInfoOutput().append("Welcome to Cluedo\n");
        mainPanel.getInfoOutput().append("Please enter 1 to continue\n");

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
