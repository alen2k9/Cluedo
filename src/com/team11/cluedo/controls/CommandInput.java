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
    private int numPlayers;
    private int currentPlayer;
    private boolean canRoll;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayer = 0;
    }

    private int getDice(){
        return this.dice;
    }

    private void setDice(int d){
        this.dice = d;
    }

    public void initialSetup()
    {
        this.canRoll = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        this.gameScreen.reDraw(this.currentPlayer);
    }

    public void playerTurn()
    {
        String playerName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getPlayerName();
        this.gameScreen.getInfoOutput().append("It is now Player : '" + playerName + "' turn\n");
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
        this.gameScreen.reDraw(this.currentPlayer);
    }

    public void runPlayer()
    {
        this.gameScreen.getCommandInput().addActionListener(e -> {
            String input = this.gameScreen.getCommandInput().getText().toLowerCase();
            String[] inputs = input.split(" ");
            String command = inputs[0];
            this.gameScreen.getCommandInput().setText("");
            this.gameScreen.getInfoOutput().append("> "+ input + '\n')  ;
            switch (command){
                case "move":
                    StringBuilder moveParameters = new StringBuilder();
                    for(int i = 1; i < inputs.length; i++)
                    {
                        moveParameters.append(inputs[i]);
                    }
                    if(this.getDice() == 0)
                    {
                        this.gameScreen.getInfoOutput().append("Too many moves, You only have '" + this.getDice() + "' moves left\n");
                    }
                    else if(this.getDice() > moveParameters.toString().length() - 1) {
                        playerMovement(moveParameters.toString());
                    }
                    break;

                case "roll":
                    diceRoll();
                    break;

                case "exit":
                    moveOut(inputs);

                    //moveOut(inputs[1]);
                    break;

                case "done":
                    nextPlayer();
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
                    this.gameScreen.getInfoOutput().append("Unknown command\nUse command 'help' for instructions\n");
                    break;
            }
            this.gameScreen.reDraw(this.currentPlayer);
        });
    }

    private void nextPlayer()
    {
        this.currentPlayer++;
        this.canRoll = true;
        this.setDice(0);
        if(this.currentPlayer == this.numPlayers)
        {
            this.currentPlayer = 0;
        }
        String playerName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getPlayerName();
        this.gameScreen.getInfoOutput().append("It is now Player : '" + playerName + "' turn\n");


    }

    private void secretPassage()
    {
        if (this.gameScreen.getGamePlayers().useSecretPassageWay(this.gameScreen.getGameBoard(), this.currentPlayer)){
            this.gameScreen.getInfoOutput().append("Used secret passageway\n");
        }
        else{
            this.gameScreen.getInfoOutput().append("No secret passageway to use in this room\n");
        }
    }

    private void moveOut(String[] inputs)
    {
        if (this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoom() != -1) {

            if (inputs.length > 2){
                this.gameScreen.getInfoOutput().append("Too many arguments for leave: Expected 1, Got " + (inputs.length-1));
            }
            else if (inputs.length == 1) {
                this.gameScreen.getInfoOutput().append("Too few arguments: leave requires one argument, type number\n");
            }

            else if (Integer.parseInt(inputs[1]) == 0){
                this.gameScreen.getInfoOutput().append("Exits start at one");
            }
            else{
                if (Integer.parseInt(inputs[1]) > gameScreen.getGameBoard().getRoom(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getExitPoints().size() ||Integer.parseInt(inputs[1]) < 0 ){
                    this.gameScreen.getInfoOutput().append("Exit number entered is too large or too small, please enter a valid exit number\n");
                }
                else{
                    this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().moveOutOfRoom(this.gameScreen.getGameBoard(), Integer.parseInt(inputs[1])-1);
                    this.setDice(this.getDice()-1);
                }
            }

        } else {
            this.gameScreen.getInfoOutput().append("Cannot leave a room when you're not in a room");
        }


    }

    private void diceRoll()
    {
        if(this.canRoll) {
            Dice die = new Dice();
            this.setDice(die.rolldice());
            this.gameScreen.getInfoOutput().append("Player rolled : " + this.dice + "\n");
            this.canRoll = false;
        }
        else {
            this.gameScreen.getInfoOutput().append("Player already rolled a '" + this.dice + "'\n");
        }
    }

    private void help()
    {
        this.gameScreen.getInfoOutput().append("help\n");
        this.gameScreen.setTab(1);
    }

    private void quitGame()
    {
        this.gameScreen.getInfoOutput().append("Exit\n");
        this.gameScreen.closeScreen();
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

        if(this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().move(this.gameScreen.getGameBoard(), list))
        {
            this.gameScreen.getInfoOutput().append("\nSuccess\n");
        }
        else{
            this.gameScreen.getInfoOutput().append("\nerror\n");
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
