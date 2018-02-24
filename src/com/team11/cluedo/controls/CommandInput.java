/**
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.controls;


import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.util.ArrayList;



public class CommandInput {
    private GameScreen gameScreen;
    private int dice;

    private int currentPlayer;
    private boolean canRoll;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayer = 0;
    }

    public void initialSetup()
    {
        canRoll = true;
        String playerName = gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName();
        gameScreen.getInfoOutput().append("It is now Player :" + playerName + "turn\n");
    }



    public void playerTurn()
    {

        runPlayer();
        Boolean endTurn = false;
        while (!endTurn) {
            if(/* SOME CONDITION FROM COMPLETING MOVE this.gameScreen.getGameSuspects().getRemainingPlayers() < 2 */true) {
                endTurn = true;
            }
            /*
            TO DO command input, then push into each options below
             */
        }
        this.gameScreen.reDraw(currentPlayer);
    }

    public void runPlayer()
    {
        gameScreen.getCommandInput().addActionListener(e -> {
            String input = gameScreen.getCommandInput().getText().toLowerCase();
            String[] inputs = input.split(" ");
            String command = inputs[0];
            gameScreen.getCommandInput().setText("");
            gameScreen.getInfoOutput().append("> "+ input + '\n')  ;
            switch (command){
                case "move":
                    StringBuilder moveParameters = new StringBuilder();
                    for(int i = 1; i < inputs.length; i++)
                    {
                        moveParameters.append(inputs[i]);
                    }
                    playerMovement(moveParameters.toString());
                    break;

                case "roll":
                    diceRoll();
                    break;

                case "exit":
                    moveOut(inputs[1]);
                    break;

                case "done":
                    gameScreen.getInfoOutput().append("Done\n");
                    break;

                case "quit":
                    quitGame();
                    break;

                case "passage":
                    secretPassage();
                    break;

                case "help":
                    help();
                    break;

                default:
                    gameScreen.getInfoOutput().append("Unknown command\nUse command 'help' for instructions\n");
                    break;
            }
            gameScreen.reDraw(currentPlayer);
        });
    }

    private void secretPassage()
    {
        if (gameScreen.getGamePlayers().useSecretPassageWay(gameScreen.getGameBoard(), currentPlayer)){
            gameScreen.getInfoOutput().append("Used secret passageway\n");
        }
        else{
            gameScreen.getInfoOutput().append("No secret passageway to use in this room\n");
        }
    }

    private void moveOut(String input)
    {
        gameScreen.getGamePlayers().moveOutOfRoom(gameScreen.getGameBoard(), currentPlayer, Integer.parseInt(input));
    }

    private void diceRoll()
    {
        if(canRoll) {
            Dice die = new Dice();
            dice = die.rolldice();
            gameScreen.getInfoOutput().append("Player rolled : " + dice + "\n");
            canRoll = false;
        }
        else {
            gameScreen.getInfoOutput().append("Player already rolled a '" + dice + "'\n");
        }
    }

    private void help()
    {
        gameScreen.getInfoOutput().append("help\n");
        gameScreen.setTab(1);
    }

    private void quitGame()
    {
        gameScreen.getInfoOutput().append("Exit\n");
        gameScreen.closeScreen();
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
            WeaponData weaponData = new WeaponData();
            String[] weaponChoice = new String[gameScreen.getGameWeapons().getNumWeapons()];
            String[] roomChoice = { "Kitchen", "Ballroom", "Conservatory", "Billiard", "Library", "Study", "Hall", "Lounge", "Dining WeaponPoints", "Cellar" };

            for (int i = 0; i < gameScreen.getGameWeapons().getNumWeapons() ; i++) {
                weaponChoice[i] = weaponData.getWeaponName(i);
                System.out.print(weaponChoice[i]);
            }

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
            WeaponData weaponData = new WeaponData();

            if (choice.getWeapon().equals(weaponData.getWeaponName(0)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 0;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(1)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 1;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(2)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 2;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(3)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 3;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(4)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 4;
            }
            else if(choice.getWeapon().equals(weaponData.getWeaponName(5)))
            {
                System.out.println("Found " + choice.getWeapon());
                weapon = 5;
            }

            System.out.println("Moving "+ weapon + choice.getWeapon() + " to " + room +choice.getRoom());
            gameScreen.getGameWeapons().moveWeaponToRoom(weapon, room);
            gameScreen.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            gameScreen.reDraw(currentPlayer);

        }

        else{
            gameScreen.getInfoOutput().append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }



    private void playerMovement(String moves)
    {
        ArrayList<Direction> list = new ArrayList<>();
        for(int i = 0; i < moves.length(); i++) {
            if(dice == 0) {
                if(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().move(gameScreen.getGameBoard(), list))
                {
                    gameScreen.getInfoOutput().append("\nSuccess\n");
                }
                else{
                    gameScreen.getInfoOutput().append("\nerror\n");
                }
                runPlayer();
            }
            else {
                if (moves.charAt(i) == 'u') {
                    list.add(Direction.NORTH);
                } else if (moves.charAt(i) == 'd') {
                    list.add(Direction.SOUTH);
                } else if (moves.charAt(i) == 'l') {
                    list.add(Direction.WEST);
                } else if (moves.charAt(i) == 'r') {
                    list.add(Direction.EAST);
                }
                dice--;
            }
        }


        if(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().move(gameScreen.getGameBoard(), list))
        {
            gameScreen.getInfoOutput().append("\nSuccess\n");
        }
        else{
            gameScreen.getInfoOutput().append("\nerror\n");
        }
        /*

                else if (gameScreen.getCommandInput().getText().equals("test")){
                    gameScreen.getGamePlayers().playerMove(gameScreen.getGameBoard(), currentPlayer, list);
                }

                else if (gameScreen.getCommandInput().getText().equals("room")){
                    gameScreen.getGamePlayers().moveOutOfRoom(gameScreen.getGameBoard(), currentPlayer, 2);
                }

                else if (gameScreen.getCommandInput().getText().equals("secret")){
                    if (gameScreen.getGamePlayers().useSecretPassageWay(gameScreen.getGameBoard(), currentPlayer)){
                        gameScreen.getInfoOutput().append("Used secret passageway");
                    }

                    else{
                        gameScreen.getInfoOutput().append("No secret passageway to use in this room");
                    }

                }
                else if(gameScreen.getCommandInput().getText().equals("reverse")){
                    gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().reverseMoves();
                }

                else if (gameScreen.getCommandInput().getText().equals("testtwo")){
                    gameScreen.getGamePlayers().playerMove(gameScreen.getGameBoard(), currentPlayer, tList);
                }
            gameScreen.getCommandInput().setText("");

        });
        */
    }
}
