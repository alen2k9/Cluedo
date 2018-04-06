/*
 * Code to handle the input of user commands.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.board.BoardPos;
import com.team11.cluedo.board.room.TileType;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.questioning.QuestionListener;
import com.team11.cluedo.questioning.QuestionMouseListener;
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
    private HelpCommand helpCommand;
    private JTextArea infoOutput;
    private Player currentPlayer;
    private String playerName;
    private StringBuilder gameLog;

    private int dice, remainingMoves, numPlayers, currentPlayerID, gameState;
    private boolean canRoll, canCheat, canQuestion, canPassage;
    private boolean moveEnabled;
    private boolean gameEnabled;
    private HashSet<Integer> removedPlayer = new HashSet<>();

    public CommandInput(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        currentPlayerID = 0;
    }

    public void initialSetup() {
        this.canRoll = true;
        this.canCheat = true;
        this.canQuestion = true;
        this.canPassage = true;
        this.moveEnabled = false;
        this.gameEnabled = true;
        this.numPlayers = this.gameScreen.getGamePlayers().getPlayerCount();
        this.currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        this.infoOutput = gameScreen.getInfoOutput();
        this.helpCommand = new HelpCommand(infoOutput);
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
        gameEnabled = false;
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

        if (gameEnabled) {
            infoOutput.append("> " + input + '\n');
            switch (gameState) {
                case 1: //  Pre-Roll
                    switch (command) {
                        case "roll":
                            diceRoll();
                            incrementGamestate(2);
                            break;

                        case "accuse":
                            accuse();
                            break;

                        case "godroll":
                            godRoll();
                            incrementGamestate(2);
                            break;

                        case "done":
                            this.gameScreen.getQuestionPanel().hideQuestionPanel();
                            gameScreen.reDraw(currentPlayerID);
                            nextPlayer();
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
                            if (currentPlayer.getSuspectToken().isInRoom()) {
                                incrementGamestate(4);
                                question(inputs);
                            } else {
                                infoOutput.append("Cannot question, must be in a room");
                            }
                            break;

                        case "hide":
                            this.gameScreen.getQuestionPanel().hideQuestionPanel();
                            //this.gameScreen.getQuestionPanel().removeKeyListener(gameScreen.getQuestionPanel().getKeyListeners()[0]);
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
                                    incrementGamestate(2);
                                }
                                break;
                            case "r":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.EAST)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate(2);
                                }
                                break;
                            case "d":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.SOUTH)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate(2);
                                }
                                break;
                            case "l":
                                movementHandling.playerMovement(new ArrayList<>(Collections.singletonList(Direction.WEST)), remainingMoves, moveEnabled);
                                if (remainingMoves == 0) {
                                    incrementGamestate(2);
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
                                question(inputs);
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

            }
        } else {
            infoOutput.append("> " + input + '\n');
            switch (gameState) {
                case 0:
                    switch (command) {
                        case "done":
                            incrementGamestate(1);
                            setGameEnabled(true);
                            break;
                        default:
                            infoOutput.append("When passed to " + playerName + ",\nType 'Done'.\n");
                            break;
                    }
                    break;
                case 4: // Question
                    switch (command){
                        case "done":
                            gameScreen.getqPanel().hideQuestionPanel();
                            setGameEnabled(true);
                            canQuestion = false;
                            if (canRoll){
                                incrementGamestate(1);
                            } else{
                                incrementGamestate(3);
                            }

                            break;
                        case "white":
                            gameScreen.getqPanel().textSelectCard("white");
                            break;
                        case "green":
                            gameScreen.getqPanel().textSelectCard("green");
                            break;
                        case "peacock":
                            gameScreen.getqPanel().textSelectCard("peacock");
                            break;
                        case "plum":
                            gameScreen.getqPanel().textSelectCard("plum");
                            break;
                        case "scarlett":
                            gameScreen.getqPanel().textSelectCard("scarlett");
                            break;
                        case "mustard":
                            gameScreen.getqPanel().textSelectCard("mustard");
                            break;
                        case "hatchet":
                            gameScreen.getqPanel().textSelectCard("hatchet");
                            break;
                        case "dagger":
                            gameScreen.getqPanel().textSelectCard("dagger");
                            break;
                        case "poison":
                            gameScreen.getqPanel().textSelectCard("poison");
                            break;
                        case "revolver":
                            gameScreen.getqPanel().textSelectCard("revolver");
                            break;
                        case "rope":
                            gameScreen.getqPanel().textSelectCard("rope");
                            break;
                        case "wrench":
                            gameScreen.getqPanel().textSelectCard("wrench");
                            break;

                        default :
                            infoOutput.append("Unknown command entered\n");

                    }

                    break;
                case 5: //  Accuse
                    break;
            }
        }

        if (!(command.equals("help") || command.equals("notes"))) {
            gameScreen.setTab(0);
        }
        gameScreen.reDrawFrame();
    }

    private void keyInput() {
        gameScreen.getCommandInput().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent key) {
                if (moveEnabled && !currentPlayer.getSuspectToken().isInRoom()) {
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
        this.currentPlayerID++;
        if(this.currentPlayerID == this.numPlayers)
            this.currentPlayerID = 0;
        if(removedPlayer.contains(this.currentPlayerID)) {
            nextPlayer();
            return;
        }

        this.gameState = 0;
        this.gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), this.currentPlayer);
        this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
        if (!currentPlayer.getSuspectToken().isInRoom()) {
            currentPlayer.getSuspectToken().setPreviousRoom(TileType.AVOID);
        }
        this.canRoll = true; this.canCheat = true; this.canPassage = true;
        this.dice = 0; this.remainingMoves = 0;
        setGameEnabled(false);
        canQuestion = true;

        this.currentPlayer = this.gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        this.playerName = currentPlayer.getPlayerName();
        movementHandling.setCurrentPlayer(currentPlayer);

        gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
        gameScreen.getButtonPanel().getAccuseButton().setEnabled(false);
        gameScreen.getButtonPanel().getRollButton().setEnabled(false);
        gameScreen.getButtonPanel().getDoneButton().setEnabled(false);

        gameScreen.getPlayerChange().setPlayerCard(currentPlayer);
        gameScreen.getPlayerChange().setVisible(true);

        infoOutput.setText("Pass to " + playerName + " and then type 'Done'.\n");
        gameScreen.reDraw(currentPlayerID);
    }

    private void secretPassage() {
        ArrayList<OverlayTile> overlayTiles = new ArrayList<>();
        if (currentPlayer.getSuspectToken().isInRoom()) {
            if (canPassage && gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).hasSecretPassage()){
                currentPlayer.getSuspectToken().useSecretPassageWay(this.gameScreen.getGameBoard());
                String roomName = currentPlayer.getSuspectToken().getCurrentRoomName();
                remainingMoves--;

                infoOutput.append(this.playerName + " used secret passageway.\n" + this.playerName + " is now in the " + roomName + ".\n");
                currentPlayer.getSuspectToken().setPreviousRoom(
                        gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getRoomType()
                );
                if (remainingMoves > 0) {
                    for (Point point : this.gameScreen.getGameBoard().getRoom(this.currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()) {
                        overlayTiles.add(new OverlayTile(point));
                    }
                    this.gameScreen.getDoorOverlay().setExits(overlayTiles, this.currentPlayer);
                    canPassage = false;
                }
            } else if (!canPassage) {
                infoOutput.append("You've already used a passage this turn!\n");
            } else {
                infoOutput.append("There are no secret passages to use in this room!\n");
            }
        } else {
            infoOutput.append("You're not in a room.\n");
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
                gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
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
        helpCommand.output(currentPlayer, gameState);
    }

    private void cards() {
        gameScreen.getCardsPanel().animate();
    }

    private void help(String command){
        helpCommand.output(command);
    }

    private void notes(){
        infoOutput.append("You opened your notes.\n");
        gameScreen.getNotesPanel().animate();
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

    public void incrementGamestate(int gameState) {
        this.gameState = gameState;
        switch (gameState) {
            case 1: //  Roll
                gameScreen.getPlayerChange().setVisible(false);
                infoOutput.setText("It is now " + playerName + "'s turn.\n");
                if (currentPlayer.getSuspectToken().isInRoom() && canQuestion) {
                    infoOutput.append("Type 'Question' to question,\nOr 'roll' to start moving.\n");
                    gameScreen.getButtonPanel().getQuestionButton().setEnabled(true);
                } else {
                    infoOutput.append("Type 'Roll' to begin your turn.\n");
                }
                gameScreen.getButtonPanel().getRollButton().setEnabled(true);
                gameScreen.getButtonPanel().getDoneButton().setEnabled(true);
                break;
            case 2: //  Moving
                gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                movementHandling.setCurrentPlayer(currentPlayer);
                setMoveEnabled(movementHandling.enableMove(this.infoOutput));
                gameScreen.getButtonPanel().getDoneButton().setEnabled(true);
                break;
            case 3: //  After move
                if (currentPlayer.getSuspectToken().isInRoom() && canQuestion) {
                    infoOutput.append("Type 'Question' to question,\nOr 'Done' to finish your turn.\n");
                    gameScreen.getButtonPanel().getQuestionButton().setEnabled(true);
                } else {
                    infoOutput.append("Type 'Done' to finish your turn.\n");
                }
                gameScreen.getButtonPanel().getDoneButton().setEnabled(true);
                break;
            case 4:
                break;
        }
    }

    private void question(String[] inputs) {
        SuspectData suspectData = new SuspectData();
        WeaponData weaponData = new WeaponData();
        boolean validInput = false;
        //One extra input, determine if weapon or player

        if (inputs.length == 1){
            validInput = true;
        }
        else if (inputs.length == 2){

            System.out.println(inputs[1]);
            //is a player
            if (suspectData.getSuspectID().containsValue(inputs[1])){
                System.out.println("suspect data contains");
                this.gameScreen.getqPanel().setSelectedPlayer(inputs[1]);
                validInput = true;
            }
            //is a weapon
            else if (weaponData.getWeaponID().containsValue(inputs[1])){
                System.out.println("Is not player");
                this.gameScreen.getqPanel().setSelectedWeapon(inputs[1]);
                validInput = true;
            } else {
                infoOutput.append("Enter a valid player/weapon");
            }
        } else if (inputs.length == 3) {
            if ((suspectData.getSuspectID().containsValue(inputs[1]) || suspectData.getSuspectID().containsValue(inputs[2])) &&
                    (weaponData.getWeaponID().containsValue(inputs[1]) || weaponData.getWeaponID().containsValue(inputs[2]))) {

                if (suspectData.getSuspectID().containsValue(inputs[1])) {
                    this.gameScreen.getqPanel().setSelectedPlayer(inputs[1]);
                } else {
                    this.gameScreen.getqPanel().setSelectedPlayer(inputs[2]);
                }

                if (weaponData.getWeaponID().containsValue(inputs[1])) {
                    this.gameScreen.getqPanel().setSelectedWeapon(inputs[1]);
                } else {
                    this.gameScreen.getqPanel().setSelectedWeapon(inputs[2]);
                }
                validInput = true;
            } else {
                infoOutput.append("Enter a valid player and weapon");
            }
        } else {
            infoOutput.append("Invalid number of arguments");
        }


        if (currentPlayer.getSuspectToken().isInRoom() && canQuestion && validInput) {
            gameEnabled = false;
            incrementGamestate(4);
            this.gameScreen.getQuestionPanel().displayQuestionPanel(currentPlayer.getSuspectToken().getCurrentRoom(), currentPlayerID);

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
                                    incrementGamestate(2);
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if(gameScreen.isFocused() && gameEnabled)
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
            setGameEnabled(true);
        });


        gameScreen.getPlayerChange().getDoneButton().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.WHITE);
            }
        });

        gameScreen.getButtonPanel().getRollButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (canRoll) {
                    gameScreen.getCommandInput().setText("ROLL");
                    processCommand();
                    gameScreen.getButtonPanel().getRollButton().setEnabled(false);
                }
            }
        });

        gameScreen.getButtonPanel().getDoneButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gameScreen.getButtonPanel().getDoneButton().isEnabled()) {
                    gameScreen.getCommandInput().setText("DONE");
                    processCommand();
                    gameScreen.getButtonPanel().getDoneButton().setEnabled(false);
                }
            }
        });

        gameScreen.getButtonPanel().getQuestionButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gameScreen.getButtonPanel().getQuestionButton().isEnabled()) {
                    gameScreen.getCommandInput().setText("QUESTION");
                    processCommand();
                    gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                }
            }
        });


        gameScreen.getAccusations().getDoneButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                infoOutput.append(playerName + " " + gameScreen.getAccusations().getAccusation());
                gameLog.append(playerName).append(" ").append(gameScreen.getAccusations().getAccusation());

                boolean correct = gameScreen.getAccusations().isCorrectGuess();
                gameScreen.getAccusations().dispose();
                if (correct) {
                    infoOutput.setText("Hoorah!\n" + playerName + " solved the murder\nand won the game!\n" + gameLog.toString());
                } else {
                    gameLog.append(playerName).append(" was found murdered\nin the cellar!\n");
                    removeCurrentPlayer();
                    nextPlayer();
                }
            }
        });
    }

    public void setMoveEnabled(boolean moveEnabled) {
        this.moveEnabled = moveEnabled;
    }

    public void setGameEnabled(boolean gameEnabled) {
        this.gameEnabled = gameEnabled;
        gameScreen.setGameEnabled(gameEnabled);
    }

    public boolean isGameEnabled() {
        return gameEnabled;
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

    public void accuse(){
        gameScreen.getAccusations().accuse();
        setGameEnabled(false);
    }

    /*
    public void afterAccuse(){
        Boolean guess = gameScreen.getAccusations().test();
        if (guess) {
            infoOutput.append("Congratulations, " + currentPlayer.getPlayerName() + "is the winner\n");
            infoOutput.append("Please type 'exit' to finish\n");
        } else if (!guess) {
            infoOutput.append(currentPlayer.getPlayerName() + "Accused wrong and their turn shall be skipped and removed\n");
            gameScreen.getAccusations().disableAccusation();
            setGameEnabled(true);
            removeCurrentPlayer();
        }
    }
    //*/

    public void removeCurrentPlayer(){
        removedPlayer.add(currentPlayerID);
    }
}
