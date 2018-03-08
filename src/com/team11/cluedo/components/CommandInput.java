/*
 * Code to handle the input of user commands.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.players.Player;
import com.team11.cluedo.pathfinder.AStarFinder;
import com.team11.cluedo.pathfinder.Path;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;

import com.team11.cluedo.ui.components.OverlayTile;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class CommandInput {
    private GameScreen gameScreen;
    private JTextArea infoOutput;
    private Player currentPlayer;
    private String playerName;

    private int dice, remainingMoves, numPlayers, currentPlayerID;
    private boolean canRoll;
    private AStarFinder finder;

    private int resolutionScalar;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayerID = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        setUpMouseClick();
        this.gameScreen.reDraw(this.currentPlayerID);
        this.currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        this.infoOutput = gameScreen.getInfoOutput();
        this.resolutionScalar = (int)(30*this.gameScreen.getResolution().getScalePercentage());

    }

    public void playerTurn() {
        rollStart();
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = this.currentPlayer.getPlayerName();
        gameScreen.reDraw(currentPlayerID);
        infoOutput.append("It is now " + this.playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll' to start\n");
        runPlayer();
    }

    private void runPlayer() {
        this.gameScreen.getCommandInput().addActionListener(e -> {
            String input = this.gameScreen.getCommandInput().getText();
            String[] inputs = input.toLowerCase().split(" ");
            String command = inputs[0];
            
            this.gameScreen.getCommandInput().setText("");
            infoOutput.append("> "+ input + '\n');

            switch (command){
                case "move":
                    StringBuilder moveParameters = new StringBuilder();
                    for(int i = 1; i < inputs.length; i++) {
                        moveParameters.append(inputs[i]);
                    }
                    if (this.remainingMoves <= 0) {
                        infoOutput.append("You have 0 moves remaining.\n");
                    } else if (this.remainingMoves < moveParameters.toString().length()) {
                        infoOutput.append("You only have " + remainingMoves + " moves remaining.\n" +
                                "You entered " + moveParameters.toString().length() + " parameters.\n");
                    } else {
                        playerMovement(inputToDirection(moveParameters.toString()));
                    }
                    break;

                case "roll":
                    diceRoll();
                    break;

                case "exit":
                    if (this.remainingMoves > 0) {
                        moveOut(inputs);
                    } else {
                      infoOutput.append("Cannot move out of room\n");
                      printRemainingMoves();
                    }
                    break;

                case "done":
                    nextPlayer();
                    this.gameScreen.getMoveOverlay().setValidMoves(new ArrayList<>(), this.currentPlayer);
                    this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
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

                case "weapon":
                    weaponMovement();
                    break;

                case "notes":
                    notes();
                    break;

                case "cheat":
                    cheat();
                    break;

                case "godroll":
                    godRoll();
                    break;

                case "back":
                    break;

                default:
                    infoOutput.append("Unknown command\nUse command 'help' for instructions.\n");
                    break;
            }
            if (!(command.equals("help")||command.equals("notes"))){
                gameScreen.setTab(0);
            }
            gameScreen.reDraw(currentPlayerID);
        });

        gameScreen.getCommandInput().addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent key)
            {

                if(key.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    arrowPlayerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)));
                    gameScreen.reDraw(currentPlayerID);
                }
                else if(key.getKeyCode() == KeyEvent.VK_UP)
                {
                    arrowPlayerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)));
                    gameScreen.reDraw(currentPlayerID);
                }
                else if(key.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    arrowPlayerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)));
                    gameScreen.reDraw(currentPlayerID);
                }
                else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    arrowPlayerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)));
                    gameScreen.reDraw(currentPlayerID);

                }
            }
        });
    }

    private void nextPlayer() {
        this.canRoll = true;
        this.dice = 0; this.remainingMoves = 0;
        this.currentPlayerID++;
        if(this.currentPlayerID == this.numPlayers)
            this.currentPlayerID = 0;
        this.currentPlayer = this.gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        infoOutput.append("\nIt is now player " + this.playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll'  to start\n");

    }

    private void secretPassage() {

        ArrayList<OverlayTile> overlayTiles = new ArrayList<>();

        if (!(currentPlayer.getSuspectToken().getCurrentRoom() == -1) && this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).hasSecretPassage() ) {
            if (this.gameScreen.getGamePlayers().useSecretPassageWay(this.gameScreen.getGameBoard(), this.currentPlayerID)){
                String roomName = currentPlayer.getSuspectToken().getCurrentRoomName();
                infoOutput.append(this.playerName + " used secret passageway.\n" + this.playerName + " is now in the " + roomName + ".\n");
                for (Point point : this.gameScreen.getGameBoard().getRoom(this.currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()){
                    overlayTiles.add(new OverlayTile(point));
                }
                this.gameScreen.getDoorOverlay().setExits(overlayTiles, this.currentPlayer);
            } else {
                infoOutput.append("There are no secret passageways to use in this room!\n");
            }

        } else {
            infoOutput.append("No secret passage to use");
        }
    }

    private void moveOut(String[] inputs) {
        int returnValue = 2 ; // 1 for success, 0 for blocked, 2 is default
        String roomName = currentPlayer.getSuspectToken().getCurrentRoomName();

        if (currentPlayer.getSuspectToken().getCurrentRoom() != -1) {
            if (inputs.length > 2) {
                infoOutput.append("Too many arguments for 'Exit'.\nExpected 1, Got " + (inputs.length - 1) + ".\n");
            } else if (inputs.length == 1) {
                returnValue = currentPlayer.getSuspectToken().moveOutOfRoom(this.gameScreen.getGameBoard(), 0);
                infoOutput.append(this.playerName + " left the " + roomName + ".\n");
                this.remainingMoves--;
            } else {

                if (Integer.parseInt(inputs[1]) > gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getExitPoints().size() || Integer.parseInt(inputs[1]) <= 0) {
                    infoOutput.append("Exit number entered is invalid.\nPlease enter a valid exit number. (1 - " +gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getExitPoints().size() + ")\n");

                } else {
                    returnValue = currentPlayer.getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), Integer.parseInt(inputs[1]) - 1);
                    infoOutput.append(this.playerName + " left the " + roomName + ".\n");
                    this.remainingMoves--;
                }
            }

            if (returnValue == 1){
                this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), currentPlayer);
                this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(), currentPlayer);
            } else if (returnValue == 0){
                this.gameScreen.getInfoOutput().append("Exit " + (Integer.parseInt(inputs[1]) ) + " is blocked by another player");
            }

            printRemainingMoves();

        } else {
            infoOutput.append("Cannot leave a room when you're not in a room!");
        }
    }

    private void printRemainingMoves(){
        infoOutput.append("You have " + this.remainingMoves + " moves remaining.\n");
    }

    private void diceRoll() {
        if(this.canRoll) {

            ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
            Dice die = new Dice();
            this.dice = die.rolldice();
            this.remainingMoves = this.dice;

            infoOutput.append(this.playerName + " rolled a " + this.dice + ".\n");
            this.canRoll = false;

            if (currentPlayer.getSuspectToken().isInRoom()){
                System.out.println("Is in room");
                for (Point point : this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()){
                    overlayTiles.add(new OverlayTile(point));
                }

                this.gameScreen.getDoorOverlay().setExits(overlayTiles, currentPlayer);

            } else {
                this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(), currentPlayer);
            }
            this.gameScreen.reDraw(currentPlayerID);

        } else {
            infoOutput.append(this.playerName + " already rolled a " + this.dice + ".\n");
        }
    }

    private void godRoll() {
        this.dice = 1000;
        this.remainingMoves = this.dice;
        this.canRoll = false;
    }

    private void help() {
        this.gameScreen.setTab(1);
    }

    private void notes(){
        infoOutput.append("You opened your notes.\n");
        this.gameScreen.setTab(3);
    }

    private void cheat() {
        infoOutput.append(playerName + " looked in the murder envelope!\n");
        gameScreen.getGameCards().getMurderEnvelope().displayMurderEnvelope();
    }

    private void quitGame() {
        infoOutput.append("Exit\n");
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
            switch (choice.getRoom()) {
                case "Kitchen":
                    room = 0;
                    break;
                case "Ballroom":
                    room = 1;
                    break;
                case "Conservatory":
                    room = 2;
                    break;
                case "Dining":
                    room = 3;
                    break;
                case "Billiard":
                    room = 4;
                    break;
                case "Library":
                    room = 5;
                    break;
                case "Lounge":
                    room = 6;
                    break;
                case "Hall":
                    room = 7;
                    break;
                case "Study":
                    room = 8;
                    break;
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
            infoOutput.append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            gameScreen.reDraw(currentPlayerID);
        } else {
            infoOutput.append("\nReturning to Main Menu\n");
            initialSetup();
        }

    }

    private void playerMovement(ArrayList<Direction> moves) {
        int steps = moves.size();

        if(remainingMoves > 0){
            if(this.currentPlayer.getSuspectToken().move(this.gameScreen.getGameBoard(), moves)){
                this.remainingMoves -= steps;
                if (steps == 1) {
                    this.gameScreen.getInfoOutput().append("You have moved " + steps + " space.\n");
                    printRemainingMoves();
                }
                else{
                    this.gameScreen.getInfoOutput().append("You have moved " + steps + " spaces.\n");
                    printRemainingMoves();
                }

                if(this.currentPlayer.getSuspectToken().isInRoom()) {
                    String roomName = this.currentPlayer.getSuspectToken().getCurrentRoomName();
                    this.remainingMoves = 0;
                    this.gameScreen.getInfoOutput().append(this.playerName + " is now in the " + roomName +"\n");
                    printRemainingMoves();
                }

                this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(), this.currentPlayer);

            }
            else {

                this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
            }
        } else {

            this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
        }

    }

    private void arrowPlayerMovement(ArrayList<Direction> moves) {
        int steps = moves.size();

        if(remainingMoves > 0){
            if(this.currentPlayer.getSuspectToken().move(this.gameScreen.getGameBoard(), moves)) {
                this.remainingMoves -= steps;
                if (steps == 1) {

                    printRemainingMoves();
                }
                else{

                    printRemainingMoves();
                }



                this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(), this.currentPlayer);

            }
            else {

                this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
            }
        } else {

            this.gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + this.remainingMoves + " moves remaining.\n");
        }

    }


    private ArrayList<OverlayTile> findValidMoves() {
        ArrayList<OverlayTile> validMoves = new ArrayList<>();

        Point startPoint;
        Point endPoint;

        if (!currentPlayer.getSuspectToken().isInRoom()) {
            startPoint = new Point((int) currentPlayer.getSuspectToken().getLoc().getX() - remainingMoves,
                    (int) currentPlayer.getSuspectToken().getLoc().getY() - remainingMoves);

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

            endPoint = new Point((int) currentPlayer.getSuspectToken().getLoc().getX() + remainingMoves,
                    (int) currentPlayer.getSuspectToken().getLoc().getY() + remainingMoves);


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
            AStarFinder finder = new AStarFinder(this.gameScreen.getGameBoard(), 12, false);
            Path path;
            //Have start and exit points now so search through and add the valid points to the return list
            for (int i = (int) startPoint.getY(); i <= (int) endPoint.getY(); i++) {

                for (int j = (int) startPoint.getX(); j <= (int) endPoint.getX(); j++) {

                    tmpPoint = new Point(j, i);

                    path = finder.findPath(currentPlayer.getSuspectToken(),
                            (int) currentPlayer.getSuspectToken().getLoc().getY(),
                            (int) currentPlayer.getSuspectToken().getLoc().getX(),
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

    private void mouseClickMove(Point target){
        Suspect currentPlayer = this.currentPlayer.getSuspectToken();
        finder = new AStarFinder(this.gameScreen.getGameBoard(), 100, false);

        Path path = finder.findPath(currentPlayer, (int)currentPlayer.getLoc().getY(), (int)currentPlayer.getLoc().getX(), (int)target.getY(), (int)target.getX());
        for (int i = 0; i < path.getLength(); i++){
            System.out.println(path.getStep(i));
        }
        ArrayList<Direction> moveList = pathToDirections(path);

        playerMovement(moveList);
    }

    private void setUpMouseClick(){
        this.gameScreen.getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gameScreen.getBoardPanel().checkPoint(e.getX()/resolutionScalar, e.getY()/resolutionScalar)){

                    mouseClickMove(new Point(e.getX()/resolutionScalar, e.getY()/resolutionScalar));
                    gameScreen.reDraw(currentPlayerID);
                    gameScreen.getMoveOverlay().setValidMoves(findValidMoves(), currentPlayer);

                }

            }
        });
    }



    private ArrayList<Direction> pathToDirections(Path path){
        ArrayList<Direction> directions = new ArrayList<>();
        Suspect currentPlayer = this.currentPlayer.getSuspectToken();

        Point previousPoint = new Point((int)currentPlayer.getLoc().getX(), (int)currentPlayer.getLoc().getY());

        Point nextPoint = new Point(path.getStep(0).getY(), path.getStep(0).getX());


        for (int i = 0; i < path.getLength(); i++){
            System.out.println("i: "+ i );
            System.out.println("Previous Point: " + previousPoint);
            System.out.println("Next Point: " + nextPoint);

            //Check the difference between the x values

            if (nextPoint.getX() == previousPoint.getX()){
                if (nextPoint.getY() > previousPoint.getY()){
                    System.out.println(nextPoint.getY() + " > " + previousPoint.getY());
                    directions.add(Direction.SOUTH);
                }

                else {
                    System.out.println(nextPoint.getY() + " < " + previousPoint.getY());
                    directions.add(Direction.NORTH);
                }
            }

            else if (nextPoint.getY() == previousPoint.getY()){
                if (nextPoint.getX() > previousPoint.getX()){
                    System.out.println(nextPoint.getX() + " > " + previousPoint.getX());
                    directions.add(Direction.EAST);
                }

                else {
                    System.out.println(nextPoint.getX() + " < " + previousPoint.getX());
                    directions.add(Direction.WEST);
                }
            }

            //Update next and previous
            previousPoint = new Point(path.getStep(i).getY(), path.getStep(i).getX());
            if (i < path.getLength()-1){
                nextPoint = new Point(path.getStep(i+1).getY(), path.getStep(i+1).getX());
            }



            System.out.println();
        }

        for (int j = 0; j < directions.size(); j++){
            System.out.println(directions.get(j));
        }
        path.getSteps().remove(0);
        return directions;
    }

    private ArrayList<Direction> inputToDirection(String moves){
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

        return list;
    }

    private void rollStart() {
        Dice die = new Dice();
        int diceNumber, highRoller = 0;
        ArrayList<Integer> dice = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            diceNumber = die.rolldice();
            playerName = gameScreen.getGamePlayers().getPlayer(i).getPlayerName();
            infoOutput.append(playerName + " rolled a " + diceNumber + ".\n");
            dice.add(diceNumber);
        }

        for(int j = 0; j < numPlayers; j++) {
            if(dice.get(highRoller) < dice.get(j)){
                highRoller = j;
            }
        }
        currentPlayerID = highRoller;
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        playerName = currentPlayer.getPlayerName();
        infoOutput.append(playerName + " rolled the highest number\n");
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
