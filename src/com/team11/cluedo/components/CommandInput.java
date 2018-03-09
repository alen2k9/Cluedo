/*
 * Code to handle the input of user commands.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.board.BoardPos;
import com.team11.cluedo.board.room.DoorData;
import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.board.room.TileType;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;

import com.team11.cluedo.ui.components.OverlayTile;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CommandInput {
    private MovementHandling movementHandling;

    private GameScreen gameScreen;
    private JTextArea infoOutput;
    private Player currentPlayer;
    private String playerName;

    private int dice, remainingMoves, numPlayers, currentPlayerID;
    private boolean canRoll;

    private int resolutionScalar;
    private boolean moveEnabled;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayerID = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.moveEnabled = false;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        setUpMouseClick();
        this.gameScreen.reDraw(this.currentPlayerID);
        this.currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        this.infoOutput = gameScreen.getInfoOutput();
        this.resolutionScalar = (int)(30*this.gameScreen.getResolution().getScalePercentage());
        movementHandling = new MovementHandling(gameScreen, currentPlayer, this);

        runPlayer();
    }

    public void playerTurn() {
        rollStart();
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);
        gameScreen.reDraw(currentPlayerID);
        infoOutput.append("It is now " + playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll' to start\n");
    }

    private void runPlayer() {
        this.gameScreen.getCommandInput().addActionListener(e -> {
            String input = this.gameScreen.getCommandInput().getText();
            String[] inputs = input.toLowerCase().split(" ");
            String command = inputs[0];
            
            this.gameScreen.getCommandInput().setText("");
            infoOutput.append("> "+ input + '\n');

            if (moveEnabled) {
                switch (command) {
                    case "u":
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)),remainingMoves,moveEnabled);
                        break;
                    case "r":
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)),remainingMoves,moveEnabled);
                        break;
                    case "d":
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)),remainingMoves,moveEnabled);
                        break;
                    case "l":
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)),remainingMoves,moveEnabled);
                        break;
                    case "move":
                        moveEnabled = movementHandling.disableMove();
                        break;
                    case "finished":
                        moveEnabled = movementHandling.disableMove();
                        break;
                    case "done":
                        moveEnabled = false;
                        nextPlayer();
                        this.gameScreen.getMoveOverlay().setValidMoves(new ArrayList<>(), this.currentPlayer);
                        this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
                        break;
                    default:
                        infoOutput.append("Unknown entry.\n +" +
                                "Enter 'U', 'R', 'D', or 'L' to move.\n" +
                                "Click on a highlighted square to move.\n" +
                                "Use the arrow keys to move.\n" +
                                "Close move by typing 'move' or 'finished'\n");
                        break;
                }
                this.gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
            } else {
                switch (command) {
                    case "move":
                        StringBuilder moveParameters = new StringBuilder();
                        for (int i = 1; i < inputs.length; i++) {
                            moveParameters.append(inputs[i]);
                        }
                        if (this.remainingMoves <= 0) {
                            infoOutput.append("You have 0 moves remaining.\n");
                        } else if (inputs.length == 1) {
                            moveEnabled = movementHandling.enableMove(infoOutput);
                        }  else if (this.remainingMoves < moveParameters.toString().length()) {
                            infoOutput.append("You only have " + remainingMoves + " moves remaining.\n" +
                                    "You entered " + moveParameters.toString().length() + " parameters.\n");
                        } else {
                            movementHandling.playerMovement(movementHandling.inputToDirection(moveParameters.toString(), remainingMoves),remainingMoves,moveEnabled);
                            this.gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
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
                            CommandProcessing.printRemainingMoves(remainingMoves,infoOutput);
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
            }
            if (!(command.equals("help")||command.equals("notes") || command.equals("fill"))){
                gameScreen.setTab(0);
            }
            gameScreen.reDraw(currentPlayerID);
        });

        gameScreen.getCommandInput().addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent key)
            {
                if (moveEnabled) {
                    if (key.getKeyCode() == KeyEvent.VK_DOWN) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)), remainingMoves, moveEnabled);
                        gameScreen.reDraw(currentPlayerID);
                    } else if (key.getKeyCode() == KeyEvent.VK_UP) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)), remainingMoves, moveEnabled);
                        gameScreen.reDraw(currentPlayerID);
                    } else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)), remainingMoves, moveEnabled);
                        gameScreen.reDraw(currentPlayerID);
                    } else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)), remainingMoves, moveEnabled);
                        gameScreen.reDraw(currentPlayerID);

                    }
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
        movementHandling.setCurrentPlayer(currentPlayer);
        infoOutput.append("\nIt is now " + this.playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll' to start\n");
    }

    private void secretPassage() {
        ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
        if (!(currentPlayer.getSuspectToken().getCurrentRoom() == -1) && this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).hasSecretPassage() ) {
            if (currentPlayer.getSuspectToken().useSecretPassageWay(this.gameScreen.getGameBoard())){
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
            infoOutput.append("No secret passage to use\n");
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
                    infoOutput.append("Exit number entered is invalid.\nPlease enter a valid exit number. (1 - " + gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getExitPoints().size() + ")\n");

                } else {
                    returnValue = currentPlayer.getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), Integer.parseInt(inputs[1]) - 1);
                    infoOutput.append(this.playerName + " left the " + roomName + ".\n");
                    this.remainingMoves--;
                }
            }

            if (returnValue == 1){
                this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), currentPlayer);
            } else if (returnValue == 0){
                this.gameScreen.getInfoOutput().append("Exit " + (Integer.parseInt(inputs[1]) ) + " is blocked by another player\n");
            }

            CommandProcessing.printRemainingMoves(remainingMoves, infoOutput);

        } else {
            infoOutput.append("Cannot leave a room when you're not in a room!\n");
        }
    }

    private void diceRoll() {
        if(this.canRoll) {
            Dice die = new Dice();
            this.dice = die.rolldice();
            this.remainingMoves = this.dice;

            if (currentPlayer.getSuspectToken().isInRoom()) {
                ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
                System.out.println("Is in room");
                for (Point point : this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()) {
                    overlayTiles.add(new OverlayTile(point));
                }
                this.gameScreen.getDoorOverlay().setExits(overlayTiles, currentPlayer);
            } else {
                //gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
            }
            infoOutput.append(this.playerName + " rolled a " + this.dice + ".\n");
            this.canRoll = false;
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

            RoomData roomData = new RoomData();

            if (choice.getRoom().equals(roomData.getRoomName(0))) {
                System.out.println("Found " + choice.getRoom());
                room = 0;
            } else if (choice.getRoom().equals(roomData.getRoomName(1))) {
                System.out.println("Found " + choice.getRoom());
                room = 1;
            } else if (choice.getRoom().equals(roomData.getRoomName(2))) {
                System.out.println("Found " + choice.getRoom());
                room = 2;
            } else if (choice.getRoom().equals(roomData.getRoomName(3))) {
                System.out.println("Found " + choice.getRoom());
                room = 3;
            } else if (choice.getRoom().equals(roomData.getRoomName(4))) {
                System.out.println("Found " + choice.getRoom());
                room = 4;
            } else if (choice.getRoom().equals(roomData.getRoomName(5))) {
                System.out.println("Found " + choice.getRoom());
                room = 5;
            } else if (choice.getRoom().equals(roomData.getRoomName(6))) {
                System.out.println("Found " + choice.getRoom());
                room = 6;
            } else if (choice.getRoom().equals(roomData.getRoomName(7))) {
                System.out.println("Found " + choice.getRoom());
                room = 7;
            } else if (choice.getRoom().equals(roomData.getRoomName(8))) {
                System.out.println("Found " + choice.getRoom());
                room = 8;
            } else if (choice.getRoom().equals("Cellar")) {
                System.out.println("Found " + choice.getRoom());
                room = 9;
            }

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

            System.out.println("Moving "+ weapon + choice.getWeapon() + " to " + room + choice.getRoom());
            gameScreen.getGameWeapons().moveWeaponToRoom(weapon, room);
            infoOutput.append("\n\n" + choice.getWeapon() + " has been moved to " + choice.getRoom() + "\n\n");
            gameScreen.reDraw(currentPlayerID);
        } else {
            infoOutput.append("\nReturning to Main Menu\n");
        }
    }

    private void setUpMouseClick(){
        HashMap<Integer, BoardPos> roomPos = new HashMap<>();
        for (BoardPos[] boardPosArray : gameScreen.getGameBoard().getBoard()) {
            for (BoardPos boardPos : boardPosArray) {
                boardPos.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (moveEnabled) {
                            if (gameScreen.getBoardPanel().checkPoint((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX())) {
                                movementHandling.mouseClickMove(new Point((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX()), remainingMoves, moveEnabled);
                                gameScreen.reDraw(currentPlayerID);
                                gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if (boardPos.getTileType() == TileType.ROOM || boardPos.getTileType() == TileType.DOOR
                                || boardPos.getTileType() == TileType.SECRETPASSAGE) {
                            fillRoom(boardPos.getRoomType());
                            if (boardPos.getTileType() == TileType.DOOR) {
                                boardPos.setBorder(BorderFactory.createLineBorder(new Color(0, 140, 255, 181), 3));
                            }
                        } else {
                            while (!roomPos.isEmpty()) {
                                BoardPos innerPos = roomPos.remove(roomPos.size() - 1);
                                innerPos.setBorder(new EmptyBorder(0, 0, 0, 0));
                            }
                            SuspectData data = new SuspectData();
                            if (boardPos.getTileType() != TileType.BLANK)
                                boardPos.setBorder(BorderFactory.createLineBorder(data.getSuspectColor(currentPlayer.getSuspectToken().getSuspectID()), 3));
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);

                        if (!(boardPos.getTileType() == TileType.ROOM || boardPos.getTileType() == TileType.DOOR
                                || boardPos.getTileType() == TileType.SECRETPASSAGE)) {
                            boardPos.setBorder(new EmptyBorder(0, 0, 0, 0));
                        }
                    }

                    private void fillRoom(TileType currentRoom) {
                        int i = 0;
                        for (BoardPos[] boardPosArray : gameScreen.getGameBoard().getBoard()) {
                            for (BoardPos innerPos : boardPosArray) {
                                if (innerPos.getRoomType() == currentRoom && !roomPos.containsValue(innerPos)) {
                                    roomPos.put(i++, innerPos);
                                }
                            }
                        }
                        for (int j = 0 ; j < i ; j++) {
                            roomPos.get(j).setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 124), 15));
                        }
                    }
                });
            }
        }
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
            RoomData roomData = new RoomData();
            String[] weaponChoice = new String[gameScreen.getGameWeapons().getNumWeapons()];
            String[] roomChoice = new String[roomData.getRoomAmount() + 1];

            for (int i = 0; i < gameScreen.getGameWeapons().getNumWeapons() ; i++) {
                weaponChoice[i] = weaponData.getWeaponName(i);
            }

            for (int i = 0; i < roomData.getRoomAmount() ; i++) {
                roomChoice[i] = roomData.getRoomName(i);
            }
            roomChoice[9] = "Cellar";

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

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public boolean isMoveEnabled() {
        return moveEnabled;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void setRemainingMoves(int remainingMoves) {
        this.remainingMoves = remainingMoves;
    }
}
