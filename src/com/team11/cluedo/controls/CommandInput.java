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
import java.util.ArrayList;

public class CommandInput {
    private GameScreen gameScreen;
    private int dice, remainingMoves;
    private int numPlayers, currentPlayer;
    private boolean canRoll;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayer = 0;
    }

    public void initialSetup() {
        canRoll = true;
        numPlayers = gameScreen.getGamePlayers().getPlayerCount();
        gameScreen.reDraw(currentPlayer);
    }

    public void playerTurn() {
        String playerName = gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName();
        gameScreen.getInfoOutput().append("It is now player '" + playerName + "'s turn.\n");
        runPlayer();
    }

    private void runPlayer() {
        gameScreen.getCommandInput().addActionListener(e -> {
            String input = gameScreen.getCommandInput().getText();
            String[] inputs = input.toLowerCase().split(" ");
            String command = inputs[0];

            gameScreen.getCommandInput().setText("");
            gameScreen.getInfoOutput().append("> "+ input + '\n');
            switch (command){
                case "move":
                    StringBuilder moveParameters = new StringBuilder();
                    for(int i = 1; i < inputs.length; i++) {
                        moveParameters.append(inputs[i]);
                    }
                    if(this.remainingMoves == 0) {
                        gameScreen.getInfoOutput().append("You have '" + this.remainingMoves + "' moves remaining.\n");
                    }
                    else if(this.remainingMoves > moveParameters.toString().length() - 1) {
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
                    gameScreen.getInfoOutput().append("Unknown command\nUse command 'help' for instructions.\n");
                    break;
            }
            if (!command.equals("help"))
                gameScreen.setTab(0);
            gameScreen.reDraw(currentPlayer);
        });
    }

    private void nextPlayer() {
        canRoll = true;
        dice = 0; remainingMoves = 0;
        currentPlayer++;
        if(currentPlayer == numPlayers)
            currentPlayer = 0;
        String playerName = gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName();
        gameScreen.getInfoOutput().append("\nIt is now player '" + playerName + "'s turn.\n");
    }

    private void secretPassage() {
        if (gameScreen.getGamePlayers().useSecretPassageWay(gameScreen.getGameBoard(), currentPlayer)){
            gameScreen.getInfoOutput().append("Used secret passageway.\n");
        } else {
            gameScreen.getInfoOutput().append("There are no secret passageways to use in this room!\n");
        }
    }

    private void moveOut(String[] inputs) {
        String roomName = gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoomName();
        String playerName = gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName();
        if (gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom() != -1) {
            if (inputs.length > 2){
                gameScreen.getInfoOutput().append("Too many arguments for 'Exit'.\nExpected 1, Got " + (inputs.length-1));
            } else if (inputs.length == 1) {
                gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), 0);
                gameScreen.getInfoOutput().append(playerName + " left the " + roomName + ".\n");
                remainingMoves--;
            } else {
                if (Integer.parseInt(inputs[1]) > gameScreen.getGameBoard().getRoom(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getExitPoints().size() ||Integer.parseInt(inputs[1]) < 0 ){
                    gameScreen.getInfoOutput().append("Exit number entered is invalid.\nPlease enter a valid exit number. (1 - " + gameScreen.getGameBoard().getRoom(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getExitPoints().size() + ")\n");
                } else {
                    gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), Integer.parseInt(inputs[1])-1);
                    gameScreen.getInfoOutput().append(playerName + " left the " + roomName + ".\n");
                    remainingMoves--;
                }
            }
        } else {
            gameScreen.getInfoOutput().append("Cannot leave a room when you're not in a room!");
        }
    }

    private void diceRoll() {
        if(canRoll) {
            Dice die = new Dice();
            dice = die.rolldice();
            remainingMoves = dice;
            gameScreen.getInfoOutput().append(gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + " rolled a " + dice + ".\n");
            canRoll = false;
        } else {
            gameScreen.getInfoOutput().append(gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + " already rolled a " + dice + ".\n");
        }
    }

    private void help() {
        gameScreen.getInfoOutput().append("Help\n");
        gameScreen.setTab(1);
    }

    private void quitGame() {
        gameScreen.getInfoOutput().append("Exiting Game.\n");
        gameScreen.closeScreen();
    }

    private void weaponMovement() {
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

    private void playerMovement(String moves) {
        ArrayList<Direction> list = new ArrayList<>();
        int steps = 0;
        for(int i = 0; i < moves.length(); i++) {
            if (moves.charAt(i) == 'u') {
                list.add(Direction.NORTH);
                remainingMoves--;
                steps++;
            } else if (moves.charAt(i) == 'd') {
                list.add(Direction.SOUTH);
                remainingMoves--;
                steps++;
            } else if (moves.charAt(i) == 'l') {
                list.add(Direction.WEST);
                remainingMoves--;
                steps++;
            } else if (moves.charAt(i) == 'r') {
                list.add(Direction.EAST);
                remainingMoves--;
                steps++;
            }
        }

        if(gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().move(gameScreen.getGameBoard(), list)) {
            if (steps == 1)
                gameScreen.getInfoOutput().append("You have moved " + steps + " space.\nYou have " + remainingMoves + " moves remaining.\n");
            else
                gameScreen.getInfoOutput().append("You have moved " + steps + " spaces.\nYou have " + remainingMoves + " moves remaining.\n");
        } else {
            gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + remainingMoves + " moves remaining.\n");
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

            weapon = (String) JOptionPane.showInputDialog(null, "Choose the Weapon you want to move",
                    "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, weaponChoice, weaponChoice[0]);

            if (weapon != null) {
                room = (String) JOptionPane.showInputDialog(null, "Choose the WeaponPoints you want to move it into",
                        "Weapon Movement", JOptionPane.QUESTION_MESSAGE, null, roomChoice, roomChoice[0]);
            } else {
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
