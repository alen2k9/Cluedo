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
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.components.OverlayTile;
import com.team11.cluedo.ui.components.RollStart;
import com.team11.cluedo.weapons.WeaponData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class CommandInput {
    private MovementHandling movementHandling;

    private GameScreen gameScreen;
    private HelpCommand helpCommand;
    private JTextArea infoOutput;
    private JTextArea personalInfo;
    private Player currentPlayer;
    private String playerName;
    private StringBuilder gameLog;
    private String[] personal;

    private int dice, remainingMoves, numPlayers, currentPlayerID, gameState;
    private boolean canRoll, canCheat, canQuestion, canPassage;
    private boolean moveEnabled, accusing = false;
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
        this.personalInfo = gameScreen.getPersonal();
        this.helpCommand = new HelpCommand(infoOutput);
        this.gameState = 0;

        this.gameLog = new StringBuilder();
        this.gameLog.append("----- Game Log\n");

        movementHandling = new MovementHandling(gameScreen, currentPlayer, this);

        this.gameScreen.getCommandInput().addActionListener(e -> processCommand());
        keyInput();
        setUpPersonal();
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

        System.out.println("state " + gameState + " command " + command);
        System.out.println("Is in room " +currentPlayer.getSuspectToken().isInRoom());

        if (gameEnabled && gameState != 4) {
            infoOutput.append("> " + input + '\n');
            switch (gameState) {
                case 1: //  Pre-Roll
                    switch (command) {
                        case "roll":
                            diceRoll();
                            incrementGamestate(2);
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
                        case "hide":
                            gameScreen.getqPanel().tempHideQuestionPanel();
                            setGameEnabled(true);
                            break;
                        case "show":
                            gameState = 4;
                            gameScreen.getqPanel().showQuestionPanel();
                            setGameEnabled(false);
                            break;
                        case "done":
                            switch (gameScreen.getqPanel().getQuestionState()) {
                                case (1):   //Showing the player change panel
                                    questionCaseOne();
                                    break;

                                case (2):  //Showing the done button
                                    questionCaseTwo();
                                    break;

                                case (3):   //Showing the cards
                                    //Need to reject done input
                                    infoOutput.append("Cannot exit questioning until card has been shown\n");
                                    break;
                                case (4):   //Finished showing cards
                                    questionCaseFour();
                                    break;
                                case (5):
                                    questionCaseFive();
                                    break;
                            }
                            break;

                        case "log":
                            log();
                            break;
                        case "help":
                            if (inputs.length == 1)
                                help();
                            else
                                help(inputs[1]);
                            break;

                        case "cards":
                            if (gameScreen.getqPanel().displaying()){
                                infoOutput.append("Cannot display your cards right now.\nTry hiding the question menu with 'hide'.\n\n");
                            } else {
                                cards();
                            }
                            break;
                        case "notes":
                            if (gameScreen.getqPanel().displaying()){
                                infoOutput.append("Cannot display your notes right now.\nTry hiding the question menu with 'hide'.\n\n");
                            } else {
                                notes();
                            }
                            break;
                        default:
                            gameScreen.getqPanel().question(input);
                            break;
                    }

                    break;
                case 5: //  Accuse
                    if (gameScreen.getAccusations().isDone()) {
                        switch (command) {
                            case "done":
                                gameScreen.getAccusations().getDoneButton().doClick();
                                break;
                            default:
                                infoOutput.append("Enter 'done' to finish your turn.\n");
                                break;
                        }
                    } else if (accusing) {
                        switch (command) {
                            case "help":
                                infoOutput.append("Enter 'cancel' to review your notes.\n");
                                break;
                            case "cancel":
                                gameScreen.getAccusations().dispose();
                                accusing = false;
                                gameScreen.getButtonPanel().getAccuseButton().setEnabled(true);
                                infoOutput.append("Enter 'accuse' when ready to make\nyour accusation.\n");
                                break;
                            default:
                                gameScreen.getAccusations().accuse(command);
                        }
                    } else {
                        switch (command) {
                            case "help":
                                if (inputs.length == 1)
                                    help();
                                else
                                    help(inputs[1]);
                                break;
                            case "accuse":
                                accuse();
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
                        }
                    }
                    break;
                case 6:
                    quitGame();
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
        addToPersonal();
        if (gameScreen.getGamePlayers().getPlayerCount() - removedPlayer.size() > 0) {
            this.currentPlayerID++;
            if (this.currentPlayerID == this.numPlayers)
                this.currentPlayerID = 0;
            if (removedPlayer.contains(this.currentPlayerID)) {
                nextPlayer();
                return;
            }
            addPersonal();
        }
        this.gameState = 0;
        this.gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), this.currentPlayer);
        this.gameScreen.getDoorOverlay().setExits(new ArrayList<>(), this.currentPlayer);
        if (!currentPlayer.getSuspectToken().isInRoom()) {
            currentPlayer.getSuspectToken().setPreviousRoom(TileType.AVOID);
        }
        this.canRoll = true; this.canCheat = true; this.canPassage = true;
        accusing = false;
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
            gameScreen.getButtonPanel().getRollButton().setEnabled(false);
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
        helpCommand.output(command, gameState);
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

    private void accuse(){
        incrementGamestate(5);
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
                gameScreen.getButtonPanel().getRollButton().setEnabled(false);
                gameScreen.getButtonPanel().getDoneButton().setEnabled(false);
                gameScreen.getButtonPanel().getAccuseButton().setEnabled(false);
                gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                break;
            case 5:
                gameScreen.getAccusations().accuse();
                gameScreen.getAccusations().setInfoOutput(this.infoOutput);
                infoOutput.append("Ready to accuse?\nPick your suspect.\n");
                gameScreen.getButtonPanel().getRollButton().setEnabled(false);
                gameScreen.getButtonPanel().getDoneButton().setEnabled(false);
                gameScreen.getButtonPanel().getAccuseButton().setEnabled(false);
                gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                accusing = true;
                setGameEnabled(false);
                break;
            case 6:
                infoOutput.append("\n\nEnter anything to close the game.\n\n");
        }
    }


    private void setUpPersonal(){
        personal = new String[6];

    }

    private void addToPersonal(){
        String string = personalInfo.getText();
        String S = this.personal[currentPlayerID] + "\n "+ string;
        this.personal[currentPlayerID] = S;
        personalInfo.removeAll();
    }

    private void addPersonal(){
        personalInfo.setText(personal[currentPlayerID]);
    }

    private void question(String[] inputs) {
        SuspectData suspectData = new SuspectData();
        WeaponData weaponData = new WeaponData();
        boolean validInput = false;
        //One extra input, determine if weapon or player
        System.out.println("inputs " + inputs.length);

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
                    //System.out.println("Comparison true");
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
            this.gameScreen.getqPanel().setupLabels();
            this.gameScreen.getqPanel().displayQuestionPanel(currentPlayer.getSuspectToken().getCurrentRoom(), currentPlayerID);
            canQuestion = false;
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
                                System.out.println("Is in room and clicked door");
                                if (remainingMoves > 0){
                                    System.out.println("Greater than zero");
                                    String[] buffer = new String[2];
                                    buffer[1] = Integer.toString(gameScreen.getBoardPanel().checkMoveOut((int)boardPos.getLocation().getY(), (int)boardPos.getLocation().getX()) + 1);
                                    moveOut(buffer);
                                }
                            } else if (gameScreen.getGameBoard().getBoardPos((int)boardPos.getLocation().getX(), (int)boardPos.getLocation().getY()).getTileType() == TileType.SECRETPASSAGE){
                                if (remainingMoves > 0){
                                    secretPassage();
                                } else {
                                    infoOutput.append("Not enough moves to use passageway");
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

        gameScreen.getPlayerChange().getDoneButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //System.out.println("Gamestate : " + gameState + " hasLooped: " + gameScreen.getqPanel().getLooped());

                if (gameState == 4){
                    System.err.println(gameScreen.getqPanel().getQuestionState());
                    switch (gameScreen.getqPanel().getQuestionState()){
                        case (1):   //Showing the player change panel
                           questionCaseOne();
                            break;
                        case (4):   //Finished selecting a card to show and need to return to the game
                            questionCaseFour();
                            break;
                        case (5):
                            questionCaseFive();
                            break;
                        default:
                            break;
                    }
                }
                else {
                    gameScreen.getCommandInput().setText("done");
                    processCommand();
                    gameScreen.getCommandInput().requestFocus();
                    setGameEnabled(true);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.RED);
                gameScreen.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                gameScreen.getPlayerChange().getDoneButton().setForeground(Color.WHITE);
                gameScreen.repaint();
            }
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
                if (gameScreen.getButtonPanel().getRollButton().isEnabled() && gameEnabled) {
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
                if (gameScreen.getButtonPanel().getDoneButton().isEnabled() && gameEnabled) {
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
                if (gameScreen.getButtonPanel().getQuestionButton().isEnabled() && gameEnabled) {
                    gameScreen.getCommandInput().setText("QUESTION");
                    processCommand();
                    gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                }
            }
        });

        gameScreen.getButtonPanel().getAccuseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gameScreen.getButtonPanel().getAccuseButton().isEnabled()) {
                    gameScreen.getCommandInput().setText("ACCUSE");
                    processCommand();
                    gameScreen.getButtonPanel().getQuestionButton().setEnabled(false);
                }
            }
        });


        gameScreen.getAccusations().getDoneButton().addActionListener(e -> {
            infoOutput.append(playerName + " " + gameScreen.getAccusations().getAccusation());
            gameLog.append(playerName).append(" ").append(gameScreen.getAccusations().getAccusation());

            boolean correct = gameScreen.getAccusations().isCorrectGuess();
            gameScreen.getAccusations().dispose();
            if (correct) {
                infoOutput.setText("Hoorah!\n" + playerName + " solved the murder\nand won the game!\n" + gameLog.toString());
                incrementGamestate(6);
                gameScreen.getPlayerChange().setDoneButton("Quit Game");
                gameScreen.getPlayerChange().setText("has solved the murder!");
                gameScreen.getPlayerChange().setEndGame(currentPlayer);
                gameScreen.getPlayerChange().getDoneButton().removeAll();
                setEndGameButton(gameScreen.getPlayerChange().getDoneButton());
                gameScreen.getPlayerChange().setVisible(true);
            } else {
                gameLog.append(playerName).append(" was found murdered\nin the cellar!\n");
                removeCurrentPlayer();
                if (gameScreen.getGamePlayers().getPlayerCount() - removedPlayer.size() == 1) {
                    gameScreen.getPlayerChange().setDoneButton("Quit Game");
                    gameScreen.getPlayerChange().setText("has one by default. Congratulations?");
                    for (int i = 0 ; i < gameScreen.getGamePlayers().getPlayerCount() ; i++) {
                        if (!removedPlayer.contains(i)) {
                           currentPlayer = gameScreen.getGamePlayers().getPlayer(i);
                        }
                    }
                    infoOutput.setText(currentPlayer.getPlayerName() + " was the last member alive,\nand won by default.\nCongratulations?\n" + gameLog.toString());
                    gameScreen.getPlayerChange().setEndGame(currentPlayer);
                    gameScreen.getPlayerChange().getDoneButton().removeAll();
                    setEndGameButton(gameScreen.getPlayerChange().getDoneButton());
                    gameScreen.getPlayerChange().setVisible(true);
                } else {
                    nextPlayer();
                }
            }
        });
    }

    private void questionCaseOne(){
        gameScreen.getPlayerChange().setVisible(false);
        if (gameScreen.getqPanel().getLooped()) {
            //System.out.println("Going to hide the question panel");
            gameScreen.getqPanel().setQuestionState(4); //Have looped therefore close the window and re-enable the game
            gameScreen.getqPanel().hideQuestionPanel();
            gameLog.append(currentPlayer.getPlayerName())
                    .append(" suggested it was\n")
                    .append(gameScreen.getqPanel().getPrevSelectedCards()[0].getCardName())
                    .append(" with the ").append(gameScreen.getqPanel().getPrevSelectedCards()[2].getCardName())
                    .append(" in the ").append(gameScreen.getqPanel().getPrevSelectedCards()[1].getCardName())
                    .append("\n");
            gameLog.append(currentPlayer.getPlayerName()).append(" was not shown a card.\n\n");
            infoOutput.append("No players had the cards you suggested.\n");
            setGameEnabled(true);
            if (canRoll) {
                incrementGamestate(1);
            } else {
                incrementGamestate(3);
            }
        } else {
            gameScreen.getInfoOutput().append("Enter hide/show to hide or show the question panel.\n");
            //gameScreen.getqPanel().findValidCards();
            gameScreen.getqPanel().setupValidCards();
            if (gameScreen.getqPanel().validCardSize() != 0) {   //There is cards to show so the done button state doesn't appear
                gameScreen.getqPanel().setQuestionState(3);
            }
            //Show the done button
            else {
                gameScreen.getqPanel().setQuestionState(2);
            }
        }
    }

    private void questionCaseTwo(){
        if (gameScreen.getqPanel().getLooped()) {
            //hide the panel
            gameScreen.getqPanel().hideQuestionPanel();
            gameScreen.getqPanel().setQuestionState(4);
            setGameEnabled(true);
        } else {
            //Show the player change panel
            gameScreen.getqPanel().setQuestionState(1);
            gameScreen.getqPanel().showNextPlayer();
            gameScreen.getqPanel().setShowingNextPlayer(true);
        }
    }

    private void questionCaseThree(){

    }

    private void questionCaseFour(){
        gameScreen.getPlayerChange().setVisible(false);
        gameLog.append(currentPlayer.getPlayerName() + " asked was it, \n" +
                gameScreen.getqPanel().getPrevSelectedCards()[0].getCardName() + " with the " + gameScreen.getqPanel().getPrevSelectedCards()[2].getCardName() +
                " in the " + gameScreen.getqPanel().getPrevSelectedCards()[1].getCardName() + ".\n\n");
        gameScreen.getqPanel().hideQuestionPanel();

        if (gameScreen.getqPanel().hasShownCard()) {
            gameLog.append(currentPlayer.getPlayerName() + " was shown a card by " + gameScreen.getqPanel().getShower() + "\n\n");
        }
        else{
            gameLog.append(currentPlayer.getPlayerName()).append(" was not shown a card by anyone\n");
        }
        gameScreen.getqPanel().printShower();
        gameScreen.getPlayerChange().setText("showed you the " + gameScreen.getqPanel().getSelectedCard().getCardName());
        gameScreen.getPlayerChange().setSelectedCard(gameScreen.getGamePlayers().getPlayer(gameScreen.getqPanel().getNextPlayer()), gameScreen.getqPanel().getSelectedCard());
        gameScreen.getqPanel().setQuestionState(5);
        gameScreen.getPlayerChange().setVisible(true);
    }

    private void questionCaseFive(){
        gameScreen.getPlayerChange().setVisible(false);
        setGameEnabled(true);
        if (canRoll) {
            incrementGamestate(1);
        } else {
            incrementGamestate(3);
        }
    }

    private void setEndGameButton(JLabel button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                quitGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button.setForeground(Color.WHITE);
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

    private void removeCurrentPlayer(){
        removedPlayer.add(currentPlayerID);
    }
}
