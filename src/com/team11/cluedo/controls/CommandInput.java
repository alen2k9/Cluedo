/*
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
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.ArrayList;

public class CommandInput {
    private GameScreen gameScreen;
    private int dice, remainingMoves;
    private int numPlayers, currentPlayer;
    private String playerName;
    private boolean canRoll;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayer = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        this.gameScreen.reDraw(this.currentPlayer);
    }

    public void playerTurn() {
        this.playerName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getPlayerName();
        this.gameScreen.getInfoOutput().append("It is now player " + this.playerName + "'s turn.\n");
        gameScreen.getInfoOutput().append("Please enter 'roll'  to start\n");
        runPlayer();
    }

    public void runPlayer() {
        this.gameScreen.getCommandInput().addActionListener(e -> {

            String input = this.gameScreen.getCommandInput().getText();
            String[] inputs = input.toLowerCase().split(" ");
            String command = inputs[0];
            
            this.gameScreen.getCommandInput().setText("");
            this.gameScreen.getInfoOutput().append("> "+ input + '\n');

            switch (command){
                case "move":
                    StringBuilder moveParameters = new StringBuilder();
                    for(int i = 1; i < inputs.length; i++) {
                        moveParameters.append(inputs[i]);
                    }
                    if (this.remainingMoves <= 0) {
                        this.gameScreen.getInfoOutput().append("You have 0 moves remaining.\n");
                    } else if (this.remainingMoves < moveParameters.toString().length()) {
                        this.gameScreen.getInfoOutput().append("You only have " + remainingMoves + " moves remaining.\n" +
                                "You entered " + moveParameters.toString().length() + " parameters.\n");
                    } else {
                        playerMovement(moveParameters.toString());
                    }
                    break;

                case "roll":
                    diceRoll();
                    break;

                case "exit":
                    if (this.remainingMoves > 0) {
                        moveOut(inputs);
                    } else{
                      this.gameScreen.getInfoOutput().append("Cannot move out of room\n");
                      printRemainingMoves();
                    }

                    //moveOut(inputs[1]);
                    break;

                case "done":
                    nextPlayer();
                    break;

                case "quit":
                    quitGame();
                    break;

                case "passage":
                    if (!(this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoom() == -1) && this.gameScreen.getGameBoard().getRoom(this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).hasSecretPassage() ) {
                        secretPassage();
                    }
                    else{
                        this.gameScreen.getInfoOutput().append("No secret passage to use");
                    }
                    break;

                case "help":
                    help();
                    break;

                case "weapon":
                    weaponMovement();
                    break;
                default:
                    gameScreen.getInfoOutput().append("Unknown command\nUse command 'help' for instructions.\n");
                    break;
            }
            if (!command.equals("help"))
                gameScreen.setTab(0);
            gameScreen.reDraw(currentPlayer);
        });
    }

    private void nextPlayer() {
        this.canRoll = true;
        this.dice = 0; this.remainingMoves = 0;
        this.currentPlayer++;
        if(this.currentPlayer == this.numPlayers)
            this.currentPlayer = 0;
        this.playerName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getPlayerName();
        this.gameScreen.getInfoOutput().append("\nIt is now player " + this.playerName + "'s turn.\n");
        gameScreen.getInfoOutput().append("Please enter 'roll'  to start\n");
    }

    private void secretPassage() {
        if (this.gameScreen.getGamePlayers().useSecretPassageWay(this.gameScreen.getGameBoard(), this.currentPlayer)){
            String roomName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoomName();
            this.gameScreen.getInfoOutput().append(this.playerName + " used secret passageway.\n" + this.playerName + " is now in the " + roomName + ".\n");
        } else {
            this.gameScreen.getInfoOutput().append("There are no secret passageways to use in this room!\n");
        }
    }

    private void moveOut(String[] inputs) {
        String roomName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoomName();

        if (this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoom() != -1) {
            if (inputs.length > 2) {
                this.gameScreen.getInfoOutput().append("Too many arguments for 'Exit'.\nExpected 1, Got " + (inputs.length - 1) + ".\n");
            } else if (inputs.length == 1) {
                this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().moveOutOfRoom(this.gameScreen.getGameBoard(), 0);
                this.gameScreen.getInfoOutput().append(this.playerName + " left the " + roomName + ".\n");
                this.remainingMoves--;
            } else {
                if (Integer.parseInt(inputs[1]) > gameScreen.getGameBoard().getRoom(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getExitPoints().size() || Integer.parseInt(inputs[1]) < 0) {
                    this.gameScreen.getInfoOutput().append("Exit number entered is invalid.\nPlease enter a valid exit number. (1 - " + gameScreen.getGameBoard().getRoom(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getExitPoints().size() + ")\n");
                } else {
                    this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), Integer.parseInt(inputs[1]) - 1);
                    this.gameScreen.getInfoOutput().append(this.playerName + " left the " + roomName + ".\n");
                    this.remainingMoves--;
                }
            }

            printRemainingMoves();

        } else {
            this.gameScreen.getInfoOutput().append("Cannot leave a room when you're not in a room!");
        }
    }

    private void printRemainingMoves(){
        this.gameScreen.getInfoOutput().append("You have " + this.remainingMoves + " moves remaining.\n");
    }

    private void diceRoll() {
        if(this.canRoll) {
            Dice die = new Dice();
            this.dice = die.rolldice();
            this.remainingMoves = this.dice;
            this.gameScreen.getInfoOutput().append(this.playerName + " rolled a " + this.dice + ".\n");
            this.canRoll = false;
        } else {
            this.gameScreen.getInfoOutput().append(this.playerName + " already rolled a " + this.dice + ".\n");
        }
    }

    private void help() {
        this.gameScreen.getInfoOutput().append("help\n");
        this.gameScreen.setTab(1);
    }

    private void quitGame() {
        this.gameScreen.getInfoOutput().append("Exit\n");
        this.gameScreen.closeScreen();
    }

    private void weaponMovement() {
        ChoiceOption choice = new ChoiceOption();
        int weapon = 0;
        int room = 0;

        if (choice.getWeapon() != null){
            /*
             * Moving Weapon
             */
            if(choice.getRoom().equals("Kitchen")) {
                room = 0;
            } else if (choice.getRoom().equals("Ballroom")) {
                room = 1;
            } else if (choice.getRoom().equals("Conservatory")) {
                room = 2;
            } else if (choice.getRoom().equals("Dining")) {
                room = 3;
            } else if (choice.getRoom().equals("Billiard")) {
                room = 4;
            } else if (choice.getRoom().equals("Library")) {
                room = 5;
            } else if (choice.getRoom().equals("Lounge")) {
                room = 6;
            } else if (choice.getRoom().equals("Hall")) {
                room = 7;
            } else if (choice.getRoom().equals("Study")) {
                room = 8;
            }

            //private String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
            WeaponData weaponData = new WeaponData();

            if (choice.getWeapon().equals(weaponData.getWeaponName(0))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 0;
            } else if(choice.getWeapon().equals(weaponData.getWeaponName(1))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 1;
            } else if(choice.getWeapon().equals(weaponData.getWeaponName(2))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 2;
            } else if(choice.getWeapon().equals(weaponData.getWeaponName(3))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 3;
            } else if(choice.getWeapon().equals(weaponData.getWeaponName(4))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 4;
            } else if(choice.getWeapon().equals(weaponData.getWeaponName(5))) {
                System.out.println("Found " + choice.getWeapon());
                weapon = 5;
            }

            System.out.println("Moving "+ weapon + choice.getWeapon() + " to " + room +choice.getRoom());
            gameScreen.getGameWeapons().moveWeaponToRoom(weapon, room);
            gameScreen.getInfoOutput().append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            gameScreen.reDraw(currentPlayer);
        } else {
            gameScreen.getInfoOutput().append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }

    private void playerMovement(String moves) {
        ArrayList<Direction> list = new ArrayList<>();
        int steps = 0;
        for(int i = 0; i < moves.length() && (remainingMoves-steps > 0) ; i++) {
            if (moves.charAt(i) == 'u') {
                list.add(Direction.NORTH);
                steps++;
            } else if (moves.charAt(i) == 'd') {
                list.add(Direction.SOUTH);
                steps++;
            } else if (moves.charAt(i) == 'l') {
                list.add(Direction.WEST);
                steps++;
            } else if (moves.charAt(i) == 'r') {
                list.add(Direction.EAST);
                steps++;
            }
        }

        if(this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().move(this.gameScreen.getGameBoard(), list)) {
            this.remainingMoves -= steps;
            if (steps == 1) {
                this.gameScreen.getInfoOutput().append("You have moved " + steps + " space.\n");
                printRemainingMoves();
            }
            else{
                this.gameScreen.getInfoOutput().append("You have moved " + steps + " spaces.\n");
                printRemainingMoves();
            }

            if(this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().isInRoom()) {
                String roomName = this.gameScreen.getGamePlayers().getPlayer(this.currentPlayer).getSuspectToken().getCurrentRoomName();
                this.gameScreen.getInfoOutput().append(this.playerName + " is now in the " + roomName + ".\n");
            }
        } else {
            this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
        }
    }

    private class ChoiceOption {
        private String weapon;
        private String room;

        private ChoiceOption() {
            makeChoice();
        }

        private void makeChoice() {
            WeaponData weaponData = new WeaponData();
            String[] weaponChoice = new String[gameScreen.getGameWeapons().getNumWeapons()];
            String[] roomChoice = { "Kitchen", "Ballroom", "Conservatory", "Billiard", "Library", "Study", "Hall", "Lounge", "Dining WeaponPoints", "Cellar" };

            for (int i = 0; i < gameScreen.getGameWeapons().getNumWeapons() ; i++) {
                weaponChoice[i] = weaponData.getWeaponName(i);
                System.out.print(weaponChoice[i]);
            }

            this.weapon = (String) JOptionPane.showInputDialog(null, "Choose the Weapon you want to move",
                    "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, weaponChoice, weaponChoice[0]);

            if (this.weapon != null) {
                this.room = (String) JOptionPane.showInputDialog(null, "Choose the WeaponPoints you want to move it into",
                        "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, roomChoice, roomChoice[0]);
            } else {
                System.out.println("Cancelling weapon movement");
            }
        }

        private String getRoom() {
            return this.room;
        }

        private String getWeapon() {
            return this.weapon;
        }
    }
}
