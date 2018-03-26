/*
 * Code to handle the animated movement of the tokens.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.components.CommandInput;
import com.team11.cluedo.components.CommandProcessing;
import com.team11.cluedo.components.MovementHandling;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AnimateToken extends SwingWorker<Integer, String> {
    private static void failIfInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Interrupted while searching files");
        }
    }

    private GameScreen gameScreen;
    private MovementHandling movementHandling;
    private CommandInput commandInput;
    private Suspect token;

    private int resolutionScalar, drawX, drawY;
    private double percentageScalar;

    private ArrayList<Direction> moves;
    private int remainingMoves;
    private boolean moveEnabled;

    public AnimateToken(GameScreen gameScreen, int remainingMoves,
                        boolean moveEnabled, CommandInput commandInput, MovementHandling movementHandling) {
        this.gameScreen = gameScreen;
        this.resolutionScalar = (int)(gameScreen.getResolution().getScalePercentage() * Board.TILE_SIZE);
        this.percentageScalar = gameScreen.getResolution().getScalePercentage();
        this.remainingMoves = remainingMoves;
        this.moveEnabled = moveEnabled;
        this.commandInput = commandInput;
        this.movementHandling = movementHandling;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        int distance = 2, delay = (int)(4*percentageScalar);

        commandInput.setMouseEnabled(false);
        commandInput.setMoveEnabled(false);
        Player currentPlayer = movementHandling.getCurrentPlayer();
        process(new ArrayList<>());
        int steps = moves.size();
        if(remainingMoves > 0){
            if(currentPlayer.getSuspectToken().checkMove(gameScreen.getGameBoard(), moves)){
                while (!moves.isEmpty()) {
                    AnimateToken.failIfInterrupted();
                    Direction direction = moves.remove(0);
                    drawX = (int) (token.getBoardLocation().getX() * resolutionScalar);
                    drawY = (int) (token.getBoardLocation().getY() * resolutionScalar);
                    token.move(gameScreen.getGameBoard(), direction);

                    while (drawX != (int) (token.getBoardLocation().getX() * resolutionScalar) || drawY != (int) (token.getBoardLocation().getY() * resolutionScalar)) {
                        if (drawX < token.getBoardLocation().getX() * resolutionScalar)
                            drawX += distance;
                        if (drawX > token.getBoardLocation().getX() * resolutionScalar)
                            drawX -= distance;
                        if (drawY < token.getBoardLocation().getY() * resolutionScalar)
                            drawY += distance;
                        if (drawY > token.getBoardLocation().getY() * resolutionScalar)
                            drawY -= distance;

                        token.setDrawX(drawX);
                        token.setDrawY(drawY);
                        try {
                            Thread.sleep(delay);
                            process(new ArrayList<>());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                commandInput.setMoveEnabled(true);
                commandInput.setMouseEnabled(true);
                remainingMoves -= steps;
                if (steps == 1) {
                    CommandProcessing.printRemainingMoves(remainingMoves, gameScreen.getInfoOutput());
                } else {
                    gameScreen.getInfoOutput().append("You have moved " + steps + " spaces.\n");
                    CommandProcessing.printRemainingMoves(remainingMoves, gameScreen.getInfoOutput());
                }

                if (currentPlayer.getSuspectToken().isInRoom()) {
                    String roomName = currentPlayer.getSuspectToken().getCurrentRoomName();
                    remainingMoves = 0;
                    gameScreen.getInfoOutput().append(currentPlayer.getPlayerName() + " is now in the " + roomName + "\n");
                    CommandProcessing.printRemainingMoves(remainingMoves, gameScreen.getInfoOutput());
                }
                if (commandInput.isMoveEnabled()) {
                    gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
                }
            } else {
                commandInput.setMouseEnabled(true);
                commandInput.setMoveEnabled(true);
                gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + remainingMoves + " moves remaining.\n");
                gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
            }
        } else {
            gameScreen.getInfoOutput().append("You have " + remainingMoves + " moves remaining.\n");
        }

        if (remainingMoves == 0 && moveEnabled) {
            commandInput.setMoveEnabled(movementHandling.disableMove());
        }
        commandInput.setRemainingMoves(remainingMoves);
        process(new ArrayList<>());

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        gameScreen.reDrawFrame();
    }

    public void setToken(Suspect token) {
        this.token = token;
    }

    public void setMoves(ArrayList<Direction> moves){
        this.moves = moves;
    }
}
