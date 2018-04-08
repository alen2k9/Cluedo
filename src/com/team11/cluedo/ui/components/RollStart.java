/*
 * Code to initial rolling of players, and visual update of who's rolling.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui.components;

import com.team11.cluedo.components.CommandInput;
import com.team11.cluedo.components.Dice;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.ui.GameScreen;

import javax.swing.*;
import java.util.*;

public class RollStart extends SwingWorker<Integer, String> {
    private Player currentPlayer;
    private GameScreen gameScreen;
    private CommandInput commandInput;
    private JTextArea infoOutput;

    private int currentPlayerID;

    public RollStart(GameScreen gameScreen, CommandInput commandInput, JTextArea infoOutput, Player currentPlayer, int currentPlayerID){
        this.gameScreen = gameScreen;
        this.commandInput = commandInput;
        this.infoOutput = infoOutput;
        this.currentPlayer = currentPlayer;
        this.currentPlayerID = currentPlayerID;

        this.gameScreen.getGameDice().setLeftDice(gameScreen.getGameBoard().getWidth()/2 - gameScreen.getGameDice().getLeftDice().getWidth()
                ,gameScreen.getHeight()/2 - gameScreen.getGameDice().getLeftDice().getHeight());
        this.gameScreen.getGameDice().setRightDice(gameScreen.getGameBoard().getWidth()/2 + (int)(gameScreen.getResolution().getScalePercentage()*10)
                ,gameScreen.getHeight()/2 - gameScreen.getGameDice().getLeftDice().getHeight());
    }


    @Override
    protected Integer doInBackground() throws Exception {
        int diceNumber;
        Dice highRoller = gameScreen.getGameDice();
        String playerName;
        ArrayList<Integer> rolledNumbers;

        HashMap<Integer, Integer> players = new HashMap<>();
        for (int i = 0 ; i < gameScreen.getGamePlayers().getPlayerCount() ; i++) {
            players.put(i,i);
        }

        HashMap<Integer, Integer> highRollers;
        do {
            highRollers = new HashMap<>();
            highRollers.put(0,0);
            rolledNumbers = new ArrayList<>();

            for(int i = 0; i < players.size() ; i++) {
                diceNumber = gameScreen.getGameDice().rollDice();
                playerName = gameScreen.getGamePlayers().getPlayer(players.get(i)).getPlayerName();
                infoOutput.append(playerName + " rolled a " + diceNumber + ".\n");
                currentPlayerID = players.get(i);
                rolledNumbers.add(i, diceNumber);

                try {
                    process(new ArrayList<>());
                    Thread.sleep(1000);
                } catch (InterruptedException ex) { ex.printStackTrace(); }

                if (i >= 1) {
                    if (rolledNumbers.get(highRollers.get(0)) < diceNumber) {
                        highRollers = new HashMap<>();
                        highRollers.put(0, i);
                        highRoller = gameScreen.getGameDice();
                    } else if (rolledNumbers.get(highRollers.get(0)).equals(diceNumber)) {
                        highRollers.put(highRollers.size(), i);
                    }
                }
            }

            if(highRollers.size() > 1) {
                infoOutput.append("Players with same roll!\n");
                infoOutput.append("\n");
                HashMap<Integer, Integer> tmpplayers = new HashMap<>();
                for (int i = 0 ; i < highRollers.size() ; i++) {
                    tmpplayers.put(i, players.get(highRollers.get(i)));
                }
                players = new HashMap<>();
                for (int i = 0 ; i < tmpplayers.size() ; i++) {
                    players.put(i, tmpplayers.get(i));
                }
            }
        } while(highRollers.size() != 1);

        Thread.sleep(500);

        gameScreen.getGameDice().setDoAnimation(true);
        currentPlayerID = players.get(highRollers.get(0));
        currentPlayer = gameScreen.getGamePlayers().getPlayer(currentPlayerID);
        playerName = currentPlayer.getPlayerName();
        process(new ArrayList<>());
        infoOutput.setText("");
        infoOutput.append(playerName + " rolled a " + rolledNumbers.get(highRollers.get(0)) + ", the highest\nnumber!\n\n");
        commandInput.getGameLog().append(playerName)
                .append(" rolled a ")
                .append(rolledNumbers.get(highRollers.get(0)))
                .append(", the highest\nnumber!\n");

        commandInput.setCurrentPlayerID(currentPlayerID);
        commandInput.setUpMouseClick();

        gameScreen.getGameDice().setLeftDice((int)gameScreen.getGameDice().getLeftLocation().getX(), (int)gameScreen.getGameDice().getLeftLocation().getY());
        gameScreen.getGameDice().setRightDice((int)gameScreen.getGameDice().getRightLocation().getX(), (int)gameScreen.getGameDice().getRightLocation().getY());

        commandInput.playerTurn();
        gameScreen.getCommandInput().requestFocus();
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        gameScreen.reDraw(currentPlayerID);
    }
}
