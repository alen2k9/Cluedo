/*
 * Code to handle the input of user commands.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.Pathfinder.AStarFinder;
import com.team11.cluedo.Pathfinder.Path;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.ui.GameScreen;

import com.team11.cluedo.ui.components.OverlayTile;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.HashSet;

public class CommandInput {
    private GameScreen gameScreen;
    private int dice, remainingMoves;
    private int numPlayers, currentPlayer;
    private String playerName;
    private boolean canRoll;
    private AStarFinder finder;

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
        rollStart();
        gameScreen.reDraw(currentPlayer);
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
                    this.gameScreen.getMoveOverlay().setValidMoves(new ArrayList<>());
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
                case "pathfind":
                    testFinder();
                    break;

                case "cheat":
                    gameScreen.getGameCards().getMurderEnvelope().displayMurderEnvelope();
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
            this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves());
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
            ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
            Dice die = new Dice();
            this.dice = die.rolldice();
            this.remainingMoves = this.dice;
            this.gameScreen.getInfoOutput().append(this.playerName + " rolled a " + this.dice + ".\n");
            this.canRoll = false;

            if (this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().isInRoom()){
                System.out.println("Is in room");
                for (Point point : this.gameScreen.getGameBoard().getRoom(this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getCurrentRoom()).getEntryPoints()){
                    overlayTiles.add(new OverlayTile(point));
                }
                System.out.println("Valid Moves" + this.gameScreen.getMoveOverlay().getValidMoves());
            }
            else{
                this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves());
            }

            this.gameScreen.reDraw(currentPlayer);

            this.gameScreen.reDraw(currentPlayer);

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

            this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves());

        } else {
            this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
        }
    }

    private ArrayList<OverlayTile> findValidMoves() {
        ArrayList<OverlayTile> validMoves = new ArrayList<>();

        Point currentPosition = new Point(this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc());
        Point startPoint;
        Point endPoint;

        if (!this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().isInRoom()) {
            startPoint = new Point((int) this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getX() - remainingMoves,
                    (int) this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getY() - remainingMoves);

            if (startPoint.getX() < 1) {
                startPoint.setLocation(1, startPoint.getY());
            } else if (startPoint.getX() > 25) {
                startPoint.setLocation(25, startPoint.getY());
            }

            if (startPoint.getY() < 1) {
                startPoint.setLocation(startPoint.getX(), 1);
            } else if (startPoint.getY() > 25) {
                startPoint.setLocation(startPoint.getX(), 25);
            }

            endPoint = new Point((int) this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getX() + remainingMoves,
                    (int) this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getY() + remainingMoves);


            if (endPoint.getX() < 1) {
                endPoint.setLocation(1, endPoint.getY());
            } else if (endPoint.getX() > 25) {
                endPoint.setLocation(25, endPoint.getY());
            }

            if (endPoint.getY() < 1) {
                endPoint.setLocation(endPoint.getX(), 1);
            } else if (endPoint.getY() > 25) {
                endPoint.setLocation(endPoint.getX(), 25);
            }

            Point tmpPoint;
            this.gameScreen.getGameBoard().clearVisited();
            AStarFinder finder = new AStarFinder(this.gameScreen.getGameBoard(), 500, false);
            Path path;
            //Have start and exit points now so search through and add the valid points to the return list
            for (int i = (int) startPoint.getY(); i <= (int) endPoint.getY(); i++) {

                for (int j = (int) startPoint.getX(); j <= (int) endPoint.getX(); j++) {

                    tmpPoint = new Point(j, i);

                    path = finder.findPath(this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken(),
                            (int)this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getY(),
                            (int)this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getX(),
                            (int)tmpPoint.getY(), (int)tmpPoint.getX());
                    

                    if (path != null && path.getLength() <= remainingMoves){
                        validMoves.add(new OverlayTile(tmpPoint));
                    }
                }
            }
        }
        ArrayList<OverlayTile> found = new ArrayList<>();

        for (OverlayTile ov : validMoves){
            if (this.gameScreen.getGameBoard().getBoardPos((int)ov.getLocation().getY(), (int)ov.getLocation().getX()).isOccupied()){
                System.out.println("Found a tile with someone on it");
                found.add(ov);
            }
        }

        validMoves.removeAll(found);
        return validMoves;
    }


    public void testFinder(){
        finder = new AStarFinder(this.gameScreen.getGameBoard(), 100, false);
        System.out.println("Info: " + this.gameScreen.getGameBoard().getBoardPos(23,8));

        System.out.println("Current Loc: " + this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc());

        Path path = finder.findPath(this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken(),
                (int)this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getY(),
                (int)this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getLoc().getX(),
                23,8);

        //System.out.println("Path length: " + path.getLength());
        if (path == null){
            System.out.println("null");
        }

        else{
            System.out.println("Not null");
            for (Object o : path.getSteps()){
                System.out.println(o);
            }
        }
    }

    public void rollStart() {
        Dice die = new Dice();
        int diceNumber, highRoller = 0;
        ArrayList<Integer> dice = new ArrayList<>();
        for(int  i = 0; i < numPlayers; i++) {
            diceNumber = die.rolldice();
            currentPlayer = i;
            gameScreen.getInfoOutput().append(gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + " rolled a " + diceNumber + ".\n");
            dice.add(diceNumber);
        }

        for(int j = 0; j < numPlayers; j++) {
            if(dice.get(highRoller) < dice.get(j)){
                highRoller = j;
            }
        }

        currentPlayer = highRoller;
        gameScreen.getInfoOutput().append(gameScreen.getGamePlayers().getPlayer(currentPlayer).getPlayerName() + " rolled the highest number\n");
    }

    public class ChoiceOption {
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
