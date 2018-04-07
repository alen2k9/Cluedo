/*
 * Code to handle the animated movement of the tokens.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.board.room.TileType;
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

    private GameScreen gameScreen;
    private MovementHandling movementHandling;
    private CommandInput commandInput;
    private Suspect token;

    private int resolutionScalar;
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
        int delay = 2;

        commandInput.setGameEnabled(false);
        commandInput.setMoveEnabled(false);
        Player currentPlayer = movementHandling.getCurrentPlayer();
        process(new ArrayList<>());
        int steps = moves.size();
        if(remainingMoves > 0){
            if(currentPlayer.getSuspectToken().checkMove(gameScreen.getGameBoard(), moves)){
                while (!moves.isEmpty()) {
                    Direction direction = moves.remove(0);
                    int initialX = (int) (token.getBoardLocation().getX() * resolutionScalar);
                    int initialY = (int) (token.getBoardLocation().getY() * resolutionScalar);
                    token.move(gameScreen.getGameBoard(), direction);
                    int targetX = (int) (token.getBoardLocation().getX() * resolutionScalar);
                    int targetY = (int) (token.getBoardLocation().getY() * resolutionScalar);

                    int drawX = initialX;
                    int drawY = initialY;

                    while (drawX != (int) (token.getBoardLocation().getX() * resolutionScalar) || drawY != (int) (token.getBoardLocation().getY() * resolutionScalar)) {
                        drawX += ((targetX-initialX)/(Board.TILE_SIZE*percentageScalar));
                        drawY += ((targetY-initialY)/(Board.TILE_SIZE*percentageScalar));

                        token.setDrawX(drawX);
                        token.setDrawY(drawY);
                        try {
                            Thread.sleep(delay);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                commandInput.setMoveEnabled(true);
                commandInput.setGameEnabled(true);
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
                    gameScreen.getInfoOutput().append("\n" + currentPlayer.getPlayerName() + " is now in the " + roomName + "\n");
                }
                if (commandInput.isMoveEnabled()) {
                    gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
                }
            } else {
                commandInput.setGameEnabled(true);
                commandInput.setMoveEnabled(true);
                gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + remainingMoves + " moves remaining.\n");
                gameScreen.getMoveOverlay().setValidMoves(movementHandling.findValidMoves(remainingMoves), currentPlayer);
            }
        } else {
            gameScreen.getInfoOutput().append("You have " + remainingMoves + " moves remaining.\n");
        }

        if (remainingMoves == 0 && moveEnabled) {
            commandInput.setMoveEnabled(movementHandling.disableMove());
            commandInput.incrementGamestate(3);
        }

        if(currentPlayer.getSuspectToken().getPreviousRoom() == TileType.CELLAR) {
            commandInput.incrementGamestate(5);
        }
        commandInput.setRemainingMoves(remainingMoves);
        process(new ArrayList<>());

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        gameScreen.repaint();
    }

    public void setToken(Suspect token) {
        this.token = token;
    }

    public void setMoves(ArrayList<Direction> moves){
        this.moves = moves;
    }
}
