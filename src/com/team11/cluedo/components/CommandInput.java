/*
 * Code to handle the input of user commands.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.board.BoardPos;
import com.team11.cluedo.board.room.RoomData;
import com.team11.cluedo.board.room.TileType;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;

import com.team11.cluedo.ui.components.RollStart;
import com.team11.cluedo.ui.components.OverlayTile;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class CommandInput {
    private MovementHandling movementHandling;

    private GameScreen gameScreen;
    private JTextArea infoOutput;
    private Player currentPlayer;
    private String playerName;

    private int dice, remainingMoves, numPlayers, currentPlayerID;
    private boolean canRoll;
    private boolean canCheat;
    private boolean moveEnabled;
    private boolean mouseEnabled;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayerID = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.canCheat = true;
        this.moveEnabled = false;
        this.mouseEnabled = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        this.currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        this.infoOutput = gameScreen.getInfoOutput();
        movementHandling = new MovementHandling(gameScreen, currentPlayer, this);
        gameScreen.reDrawFrame();
        SwingUtilities.invokeLater(() -> new RollStart(gameScreen, this, infoOutput, currentPlayer, currentPlayerID).execute());
    }

    public void playerTurn() {
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);
        infoOutput.append("It is now " + playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll' to start\n");
    }

    public void runPlayer() {
        this.gameScreen.getCommandInput().addActionListener(e -> {
            String input = this.gameScreen.getCommandInput().getText();
            String[] inputs = input.toLowerCase().split(" ");
            String command = inputs[0];


            this.gameScreen.getCommandInput().setText("");
            if (mouseEnabled) {
                infoOutput.append("> " + input + '\n');

                if (moveEnabled) {
                    switch (command) {
                        case "u":
                            movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)), remainingMoves, moveEnabled);
                            break;
                        case "r":
                            movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)), remainingMoves, moveEnabled);
                            break;
                        case "d":
                            movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)), remainingMoves, moveEnabled);
                            break;
                        case "l":
                            movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)), remainingMoves, moveEnabled);
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
                            this.gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), this.currentPlayer);
                            this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
                            break;
                        default:
                            infoOutput.append("Unknown entry.\n" +
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
                            if (!currentPlayer.getSuspectToken().isInRoom()) {
                                if (this.remainingMoves <= 0) {
                                    infoOutput.append("You have 0 moves remaining.\n");
                                } else if (inputs.length == 1) {
                                    moveEnabled = movementHandling.enableMove(infoOutput);
                                } else if (this.remainingMoves < moveParameters.toString().length()) {
                                    infoOutput.append("You only have " + remainingMoves + " moves remaining.\n" +
                                            "You entered " + moveParameters.toString().length() + " parameters.\n");
                                } else {
                                    movementHandling.playerMovement(movementHandling.inputToDirection(moveParameters.toString(), remainingMoves), remainingMoves, moveEnabled);
                                }
                            } else {
                                infoOutput.append("You can't move when in a room!\nYou must exit first.\n");
                            }
                            break;

                        case "roll":
                            diceRoll();
                            break;

                        case "exit":
                            if (this.remainingMoves > 0) {
                                moveOut(inputs);
                            } else {
                                infoOutput.append("Cannot move out of room.\n");
                                CommandProcessing.printRemainingMoves(remainingMoves, infoOutput);
                            }
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
                            if(inputs.length == 1)
                                help();
                            else
                                helps(inputs[1]);
                            break;

                        case "weapon":
                            weaponMovement();
                            break;

                        case "notes":
                            notes();
                            break;

                        case "cards":
                            cards();
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
            }

            if (!(command.equals("help")||command.equals("notes") || command.equals("cards"))){

                gameScreen.setTab(0);
            }
            gameScreen.reDrawFrame();
        });

        gameScreen.getCommandInput().addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent key)
            {
                if (moveEnabled) {
                    if (key.getKeyCode() == KeyEvent.VK_DOWN) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)), remainingMoves, moveEnabled);
                    } else if (key.getKeyCode() == KeyEvent.VK_UP) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)), remainingMoves, moveEnabled);
                    } else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)), remainingMoves, moveEnabled);
                    } else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
                        movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)), remainingMoves, moveEnabled);
                    }
                }
            }
        });
    }

    private void nextPlayer() {
        this.gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), this.currentPlayer);
        this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);

        this.canRoll = true; this.canCheat = true;
        this.dice = 0; this.remainingMoves = 0;
        this.currentPlayerID++;
        if(this.currentPlayerID == this.numPlayers)
            this.currentPlayerID = 0;
        this.currentPlayer = this.gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);
        infoOutput.append("\nIt is now " + this.playerName + "'s turn.\n");
        infoOutput.append("Please enter 'roll' to start\n");
        gameScreen.reDraw(currentPlayerID);
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
                if (inputs.length == 2){
                    this.gameScreen.getInfoOutput().append("Exit " + (Integer.parseInt(inputs[1]) ) + " is blocked by another player\n");
                } else if (inputs.length == 1){
                    this.gameScreen.getInfoOutput().append("Exit 1" + " is blocked by another player\n");
                }
                this.remainingMoves++;
            }

            CommandProcessing.printRemainingMoves(remainingMoves, infoOutput);

        } else {
            infoOutput.append("Cannot leave a room when you're not\nin a room!\n");
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
            }
            infoOutput.append(this.playerName + " rolled a " + this.dice + ".\n");
            this.canRoll = false;
            gameScreen.reDrawFrame();
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

    private void cards(){this.gameScreen.setTab(2);}

    private void helps(String command){
        switch (command) {
            case "roll":
                infoOutput.append("- 'roll' : Dice would roll giving you the number\n" +
                        "of movements you are allowed to move\n\n");
                break;

            case "move" :
                infoOutput.append("- 'move': Move player in desired direction \n" +
                        "        l = Left\t\n" +
                        "        r = Right\n" +
                        "        u = Up\n" +
                        "        d = Down\n" +
                        "Example\n" +
                        "'move llddru' : \n" +
                        " move left 2 spaces, move down 2 spaces\n" +
                        " move right once and move up once\n" +
                        "-You can use arrow keys to move\n" +
                        "-User can also click the highlighted \n squares\n" +
                        "-Type 'move' again to toggle movement on and off\n\n");
                break;

            case "exit":
                infoOutput.append("- 'exit' :\nuser can exit a room with this command.\n" +
                        "Exit depends on number of door's in room.\n" +
                        "Doors to choose from are ranged 1-4\n" +
                        "i.e. 1 is leftmost door, 4 is rightmost door\n" +
                        "Example :'exit 2' :\n" +
                        "Player will exit through second door.\n" +
                        "             'exit' :\n" +
                        "Player will exit through first door by \n default.\n\n");
                break;

            case "cards":
                infoOutput.append("- 'cards' : This will display the current \ncards that the user has\n\n");
                break;

            case "passage":
                infoOutput.append("- 'passage' : \nIf player is in a room with secret passage\n" +
                        "this command will let them use the secret passage\n\n");
                break;

            case "notes":
                infoOutput.append("- 'notes' :\n Opens the users notes page.\n" +
                        " You can click on any cell to highlight\n" +
                        " what cards you know are not part of the\n" +
                        " murder.\n\n");
                break;

            case "cheat":
                infoOutput.append("- 'cheat' : \nDisplays the cards in the envelope\n\n");
                break;

            case "done":
                infoOutput.append("- 'done' :\n Current player will end their turn.\n" +
                        "Next Player's turn will start.\n\n");
                break;

            case "back":
                infoOutput.append("- 'back' :\n Brings you back to the game log.\n\n");
                break;

            case  "quit":
                infoOutput.append("- 'quit' :\n Player can quit/stop entire game.\n\n");
                break;

            default:
                infoOutput.append("Unknown command\nUse command 'help' + 'type command' for instructions.\n");
                break;
        }

    }

    private void notes(){
        infoOutput.append("You opened your notes.\n");
        this.gameScreen.setTab(3);
    }

    private void cheat() {
        if (canCheat) {
            infoOutput.append(playerName + " looked in the murder envelope!\n");
            new SwingWorker<Integer, String>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    canCheat = false;
                    int x = gameScreen.getBoardPanel().getSize().width / 2 - gameScreen.getGameCards().getMurderEnvelope().getSize().width / 2,
                            y = gameScreen.getBoardPanel().getHeight();
                    int targetY = gameScreen.getBoardPanel().getSize().height - (gameScreen.getGameCards().getMurderEnvelope().getHeight() + 10);


                    int delay = 8, amount = 3;

                    gameScreen.getGameCards().getMurderEnvelope().setLocation(new Point(x, y));
                    gameScreen.getGameCards().getMurderEnvelope().displayMurderEnvelope();
                    process(new ArrayList<>());

                    while (y > targetY) {
                        y -= amount*2;
                        gameScreen.getGameCards().getMurderEnvelope().setLocation(new Point(x, y));
                        Thread.sleep(delay / 4);
                        process(new ArrayList<>());
                    }

                    for (int i = 0; i < 15; i++) {
                        y += amount;
                        gameScreen.getGameCards().getMurderEnvelope().setLocation(new Point(x, y));
                        Thread.sleep(delay);
                        process(new ArrayList<>());
                    }

                    Thread.sleep(3000);

                    for (int i = 0; i < 15; i++) {
                        y -= amount;
                        gameScreen.getGameCards().getMurderEnvelope().setLocation(new Point(x, y));
                        Thread.sleep(delay);
                        process(new ArrayList<>());
                    }

                    while (y < gameScreen.getBoardPanel().getHeight()) {
                        y += amount*2;
                        gameScreen.getGameCards().getMurderEnvelope().setLocation(new Point(x, y));
                        Thread.sleep(delay / 4);
                        process(new ArrayList<>());
                    }

                    gameScreen.getGameCards().getMurderEnvelope().disposeMurderEnvelope();
                    process(new ArrayList<>());
                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    gameScreen.validate();
                    gameScreen.repaint();
                }
            }.execute();
        } else {
            infoOutput.append("Uh oh. Nobody likes a cheater.\n");
        }
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
        } else {
            infoOutput.append("\nReturning to Main Menu\n");
        }
    }

    public void setUpMouseClick(){
        HashMap<Integer, BoardPos> roomPos = new HashMap<>();
        for (BoardPos[] boardPosArray : gameScreen.getGameBoard().getBoard()) {
            for (BoardPos boardPos : boardPosArray) {
                boardPos.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (currentPlayer.getSuspectToken().isInRoom()){
                            if (gameScreen.getGameBoard().getBoardPos((int)boardPos.getLocation().getX(), (int)boardPos.getLocation().getY()).getTileType() == TileType.DOOR){
                                if (remainingMoves > 0){
                                    String[] buffer = new String[2];
                                    buffer[1] = Integer.toString(gameScreen.getBoardPanel().checkMoveOut((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX()) + 1);
                                    moveOut(buffer);
                                }
                            }
                        }

                        if (moveEnabled) {
                            if (gameScreen.getBoardPanel().checkPoint((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX())) {
                                movementHandling.mouseClickMove(new Point((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX()), remainingMoves, moveEnabled);
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if(gameScreen.isFocused() && mouseEnabled)
                            if (boardPos.getTileType() == TileType.ROOM || boardPos.getTileType() == TileType.DOOR
                                    || boardPos.getTileType() == TileType.SECRETPASSAGE) {
                                if (!roomPos.isEmpty())
                                    if (boardPos.getRoomType() != roomPos.get(0).getRoomType()) {
                                        while (!roomPos.isEmpty()) {
                                            BoardPos innerPos = roomPos.remove(roomPos.size() - 1);
                                            innerPos.setBorder(new EmptyBorder(0, 0, 0, 0));
                                        }
                                    }
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
                        for (int j = 0 ; j < roomPos.size() ; j++) {
                                roomPos.get(j).setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 15));
                        }
                    }
                });
            }
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

    public void setMouseEnabled(boolean mouseEnabled) {
        this.mouseEnabled = mouseEnabled;
    }

    public void setCurrentPlayerID(int currentPlayerID) {
        this.currentPlayerID = currentPlayerID;
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
