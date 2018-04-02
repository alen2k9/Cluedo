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
import com.team11.cluedo.questioning.QuestionListener;
import com.team11.cluedo.questioning.QuestionMouseListener;
import com.team11.cluedo.questioning.QuestionPanel;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;

import com.team11.cluedo.ui.Resolution;
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
    private StringBuilder gameLog;

    private int dice, remainingMoves, numPlayers, currentPlayerID, gameState;
    private boolean canRoll;
    private boolean canCheat;
    private boolean canQuestion;
    private boolean moveEnabled;
    private boolean mouseEnabled;

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayerID = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.canCheat = true;
        this.canQuestion = false;
        this.moveEnabled = false;
        this.mouseEnabled = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        this.currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        this.infoOutput = gameScreen.getInfoOutput();
        this.gameState = 0;

        this.gameLog = new StringBuilder();
        this.gameLog.append("----- Game Log\n");

        movementHandling = new MovementHandling(gameScreen, currentPlayer, this);

        this.gameScreen.getCommandInput().addActionListener(e -> processCommand());
        keyInput();

        gameScreen.reDrawFrame();
        SwingUtilities.invokeLater(() -> new RollStart(gameScreen, this, infoOutput, currentPlayer, currentPlayerID).execute());
    }

    public void playerTurn() {
        gameState = 0;
        mouseEnabled = false;
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);
        gameScreen.getPlayerChange().setPlayerCard(currentPlayer);
        gameScreen.getPlayerChange().setVisible(true);
        infoOutput.append("Pass to " + playerName + " and then type 'Done'.\n");
    }

    private void processCommand() {
        String input = this.gameScreen.getCommandInput().getText();
        String[] inputs = input.toLowerCase().split(" ");
        String command = inputs[0];

        this.gameScreen.getCommandInput().setText("");

        if (mouseEnabled) {
            infoOutput.append("> " + input + '\n');
            switch (gameState) {
                case 1: //  Pre-Roll
                    switch (command) {
                        case "roll":
                            diceRoll();
                            incrementGamestate();
                            break;

                        case "godroll":
                            godRoll();
                            incrementGamestate();
                            break;

                        case "done":
                            nextPlayer();
                            this.gameScreen.getQuestionPanel().hideQuestionPanel();
                            break;

                        case "quit":
                            quitGame();
                            break;

                        case "help":
                            if (inputs.length == 1)
                                help();
                            else
                                help(inputs[1]);
                            break;
                        case "notes":
                            notes();
                            break;

                        case "cards":
                            cards();
                            break;

                        case "log":
                            log();
                            break;

                        case "cheat":
                            cheat();
                            break;

                        case "question":
                            System.out.println(canQuestion);
                            if (currentPlayer.getSuspectToken().isCanQuestion()) {
                                this.gameScreen.getQuestionPanel().displayQuestionPanel(currentPlayer.getSuspectToken().getCurrentRoom(), currentPlayerID);
                                this.gameScreen.getQuestionPanel().addKeyListener(new QuestionListener(this.gameScreen.getQuestionPanel()));
                                this.gameScreen.getQuestionPanel().addMouseListener(new QuestionMouseListener(this.gameScreen.getQuestionPanel()));
                                this.gameScreen.getQuestionPanel().requestFocus();
                            } else {
                                infoOutput.append("Cannot question, must be in a room");
                            }
                            break;

                        case "hide":
                            this.gameScreen.getQuestionPanel().hideQuestionPanel();
                            this.gameScreen.getQuestionPanel().removeKeyListener(gameScreen.getQuestionPanel().getKeyListeners()[0]);
                            this.gameScreen.getCommandInput().requestFocus();
                            break;

                        case "back":
                            break;

                        default:
                            infoOutput.append("Unknown entry\nUse command 'help' for instructions.\n");
                            break;
                    }
                    break;
                case 2: //  After-Roll
                    if (currentPlayer.getSuspectToken().isInRoom()) {
                        switch (command) {
                            case "passage":
                                if (remainingMoves > 0) {
                                    secretPassage();
                                } else {
                                    infoOutput.append("Cannot use a secret passageway.\n");
                                    CommandProcessing.printRemainingMoves(remainingMoves, infoOutput);
                                }
                                break;
                            case "exit":
                                if (this.remainingMoves > 0) {
                                    moveOut(inputs);
                                } else {
                                    infoOutput.append("Cannot move out of room.\n");
                                    CommandProcessing.printRemainingMoves(remainingMoves, infoOutput);
                                }
                                break;
                            case "question":
                                //question();
                                break;

                            case "help":
                                if (inputs.length == 1)
                                    help();
                                else
                                    help(inputs[1]);
                                break;
                            case "notes":
                                notes();
                                break;

                            case "cards":
                                cards();
                                break;

                            case "log":
                                log();
                                break;

                            case "cheat":
                                cheat();
                                break;

                            case "done":
                                nextPlayer();
                                break;

                            case "quit":
                                quitGame();
                                break;

                            default:
                                infoOutput.append("Unknown entry\nUse command 'help' for instructions.\n");
                                break;
                        }
                    } else {
                        switch (command) {
                            case "u":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.NORTH)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate();
                                }
                                break;
                            case "r":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate();
                                }
                                break;
                            case "d":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate();
                                }
                                break;
                            case "l":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate();
                                }
                                break;

                            case "help":
                                if (inputs.length == 1)
                                    help();
                                else
                                    help(inputs[1]);
                                break;

                            case "notes":
                                notes();
                                break;

                            case "cards":
                                cards();
                                break;

                            case "log":
                                log();
                                break;

                            case "cheat":
                                cheat();
                                break;

                            case "done":
                                nextPlayer();
                                break;

                            case "quit":
                                quitGame();
                                break;

                            default:
                                infoOutput.append("Unknown entry.\n" +
                                        "Enter 'U', 'R', 'D', or 'L' to move.\n" +
                                        "Click on a highlighted square to move.\n" +
                                        "Use the arrow keys to move.\n");
                                break;
                        }
                    }
                    break;
                case 3: //  After move
                    if (currentPlayer.getSuspectToken().isInRoom()) {
                        switch (command) {
                            case "question":
                                //question();
                                break;

                            case "help":
                                if (inputs.length == 1)
                                    help();
                                else
                                    help(inputs[1]);
                                break;
                            case "notes":
                                notes();
                                break;

                            case "cards":
                                cards();
                                break;

                            case "log":
                                log();
                                break;

                            case "cheat":
                                cheat();
                                break;

                            case "done":
                                nextPlayer();
                                break;

                            case "quit":
                                quitGame();
                                break;

                            default:
                                infoOutput.append("Unknown entry\nUse command 'help' for instructions.\n");
                                break;
                        }
                    } else {
                        switch (command) {
                            case "help":
                                if (inputs.length == 1)
                                    help();
                                else
                                    help(inputs[1]);
                                break;
                            case "notes":
                                notes();
                                break;

                            case "cards":
                                cards();
                                break;

                            case "log":
                                log();
                                break;

                            case "cheat":
                                cheat();
                                break;

                            case "done":
                                nextPlayer();
                                break;

                            case "quit":
                                quitGame();
                                break;

                            default:
                                infoOutput.append("Unknown entry\nUse command 'help' for instructions.\n");
                                break;
                        }
                    }
                    break;
                case 4: //  Questioning
                    //question(command);
                    break;
                case 5: //  Accusation
                    break;
            }
        } else {
            switch (command) {
                case "done":
                    incrementGamestate();
                    setMouseEnabled(true);
                    infoOutput.setText("It is now " + playerName + "'s turn.\n");
                    break;
                default:
                    infoOutput.append("When passed to " + playerName + ",\nType 'Done'.\n");
                    break;
            }
        }

        if (!(command.equals("help") || command.equals("notes") || command.equals("cards"))) {
            gameScreen.setTab(0);
        }
        gameScreen.reDrawFrame();
    }

    private void keyInput() {
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
        this.gameState = 0;
        this.gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), this.currentPlayer);
        this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
        if (!currentPlayer.getSuspectToken().isInRoom()) {
            currentPlayer.getSuspectToken().setPreviousRoom(TileType.AVOID);
        }
        this.canRoll = true; this.canCheat = true;
        this.dice = 0; this.remainingMoves = 0;
        setMouseEnabled(false);

        this.currentPlayerID++;
        if(this.currentPlayerID == this.numPlayers)
            this.currentPlayerID = 0;
        this.currentPlayer = this.gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);

        gameScreen.getPlayerChange().setPlayerCard(currentPlayer);
        gameScreen.getPlayerChange().setVisible(true);

        infoOutput.setText("Pass to " + playerName + " and then type 'Done'.\n");
        gameScreen.reDraw(currentPlayerID);
    }

    private void secretPassage() {
        ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
        if (!(currentPlayer.getSuspectToken().getCurrentRoom() == -1) && this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).hasSecretPassage() ) {
            if (currentPlayer.getSuspectToken().useSecretPassageWay(this.gameScreen.getGameBoard())){
                String roomName = currentPlayer.getSuspectToken().getCurrentRoomName();
                remainingMoves--;

                infoOutput.append(this.playerName + " used secret passageway.\n" + this.playerName + " is now in the " + roomName + ".\n");
                if (remainingMoves > 0){
                    for (Point point : this.gameScreen.getGameBoard().getRoom(this.currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()){
                        overlayTiles.add(new OverlayTile(point));
                    }
                    this.gameScreen.getDoorOverlay().setExits(overlayTiles, this.currentPlayer);
                }

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
                this.remainingMoves--;
            } else {
                if (Integer.parseInt(inputs[1]) > gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getExitPoints().size() || Integer.parseInt(inputs[1]) <= 0) {
                    infoOutput.append("Exit number entered is invalid.\nPlease enter a valid exit number. (1 - " + gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getExitPoints().size() + ")\n");
                } else {
                    returnValue = currentPlayer.getSuspectToken().moveOutOfRoom(gameScreen.getGameBoard(), Integer.parseInt(inputs[1]) - 1);
                    this.remainingMoves--;
                }
            }

            if (returnValue == 1){
                gameScreen.getDoorOverlay().setExits(new ArrayList<>(), currentPlayer);
                infoOutput.append(this.playerName + " left the " + roomName + ".\n");
                infoOutput.append("Enter 'U', 'R', 'D', or 'L' to move.\n" +
                        "Click on a highlighted square to move.\n" +
                        "Use the arrow keys to move.\n");
                gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
                gameScreen.repaint();
            } else if (returnValue == 0){
                if (inputs.length == 2){
                    this.gameScreen.getInfoOutput().append("Exit " + (Integer.parseInt(inputs[1]) ) + " is blocked by another player\n");
                } else {
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
            gameScreen.getGameDice().setVisible(true);
            this.dice = gameScreen.getGameDice().rollDice();
            this.remainingMoves = this.dice;

            if (currentPlayer.getSuspectToken().isInRoom()) {
                ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
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

    private void cards() {
        this.gameScreen.setTab(2);
    }

    private void help(String command){
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

    private void log() {
        infoOutput.append(gameLog.toString());
        infoOutput.append("-----\n\n");
    }

    private void cheat() {
        if (canCheat) {
            infoOutput.append(playerName + " looked in the murder envelope!\n");
            gameLog.append(playerName)
                    .append(" looked in the murder envelope!\n");
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

    public void incrementGamestate() {
        gameState++;
        switch (gameState) {
            case 1: //  Roll
                gameScreen.getPlayerChange().setVisible(false);
                break;
            case 2: //  Moving
                movementHandling.setCurrentPlayer(currentPlayer);
                setMoveEnabled(movementHandling.enableMove(this.infoOutput));
                break;
            case 3: //  After move
                if (currentPlayer.getSuspectToken().isInRoom()) {
                    infoOutput.append("Type 'Question' or 'Done'\n");
                } else {
                    infoOutput.append("Type 'Done' to finish your turn.\n");
                }
                break;
            case 4:
                break;
        }
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
                        } if (moveEnabled) {
                            if (gameScreen.getBoardPanel().checkPoint((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX())) {
                                movementHandling.mouseClickMove(new Point((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX()), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate();
                                }
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

        gameScreen.getPlayerChange().getDoneButton().addActionListener(e ->{
            gameScreen.getCommandInput().setText("done");
            processCommand();
            gameScreen.getCommandInput().requestFocus();
            setMouseEnabled(true);
        });


        gameScreen.getPlayerChange().getDoneButton().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.WHITE);
            }
        });
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

    public StringBuilder getGameLog() {
        return gameLog;
    }
}
