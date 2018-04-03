package com.team11.cluedo.components;

import com.team11.cluedo.pathfinder.AStarFinder;
import com.team11.cluedo.pathfinder.Path;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.components.AnimateToken;
import com.team11.cluedo.ui.components.OverlayTile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class MovementHandling {
    private CommandInput commandInput;
    private GameScreen gameScreen;
    private Player currentPlayer;

    public MovementHandling(GameScreen gameScreen, Player currentPlayer, CommandInput commandInput) {
        this.gameScreen = gameScreen;
        this.currentPlayer = currentPlayer;
        this.commandInput = commandInput;
    }

    public void playerMovement(ArrayList<Direction> moves, int remainingMoves, boolean moveEnabled) {
        gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), currentPlayer);
        gameScreen.getDoorOverlay().setExits(new ArrayList<>(), currentPlayer);
        AnimateToken animateToken = new AnimateToken(gameScreen, remainingMoves, moveEnabled, commandInput,this);
        animateToken.setMoves(moves);
        animateToken.setToken(currentPlayer.getSuspectToken());
        animateToken.execute();
    }

    public HashSet<OverlayTile> findValidMoves(int remainingMoves) {
        HashSet<OverlayTile> validMoves = new HashSet<>();

        Point startPoint;
        Point endPoint;

        if (!currentPlayer.getSuspectToken().isInRoom()) {
            startPoint = new Point((int) currentPlayer.getSuspectToken().getBoardLocation().getX() - remainingMoves,
                    (int) currentPlayer.getSuspectToken().getBoardLocation().getY() - remainingMoves);

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

            endPoint = new Point((int) currentPlayer.getSuspectToken().getBoardLocation().getX() + remainingMoves,
                    (int) currentPlayer.getSuspectToken().getBoardLocation().getY() + remainingMoves);

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
                            (int) currentPlayer.getSuspectToken().getBoardLocation().getY(),
                            (int) currentPlayer.getSuspectToken().getBoardLocation().getX(),
                            (int)tmpPoint.getY(), (int)tmpPoint.getX());

                    if (path != null && path.getLength() <= remainingMoves){
                        validMoves.add(new OverlayTile(tmpPoint));
                    }
                }
            }
        }


        HashSet<OverlayTile> invalidTiles = new HashSet<>();
        for (OverlayTile ov : validMoves){
            if (this.gameScreen.getGameBoard().getBoardPos((int)ov.getLocation().getY(), (int)ov.getLocation().getX()).isOccupied()){
                invalidTiles.add(ov);
            }
            if (this.gameScreen.getGameBoard().getBoardPos((int)ov.getLocation().getY(),
                    (int)ov.getLocation().getX()).getRoomType() == currentPlayer.getSuspectToken().getPreviousRoom()){
                invalidTiles.add(ov);
            }
        }

        validMoves.removeAll(invalidTiles);
        return validMoves;
    }

    public void mouseClickMove(Point target, int remainingMoves, boolean moveEnabled){
        Suspect suspectToken = currentPlayer.getSuspectToken();
        AStarFinder finder = new AStarFinder(gameScreen.getGameBoard(), 12, false);
        Path path = finder.findPath(suspectToken, (int)suspectToken.getBoardLocation().getY(), (int)suspectToken.getBoardLocation().getX(), (int)target.getY(), (int)target.getX());
        ArrayList<Direction> moveList = pathToDirections(path);
        playerMovement(moveList, remainingMoves, moveEnabled);
    }

    public boolean enableMove(JTextArea infoOutput) {
        this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(commandInput.getRemainingMoves()), currentPlayer);
        if (currentPlayer.getSuspectToken().isInRoom()) {
            infoOutput.append("Type 'Exit' followed by a number to exit.\n" +
                    "Type 'Passage' to use the secret passage if available.\n" +
                    "Click on a highlighted square to exit.\n");
        } else {
            infoOutput.append("Enter 'U', 'R', 'D', or 'L' to move.\n" +
                    "Click on a highlighted square to move.\n" +
                    "Use the arrow keys to move.\n");
        }
        return true;
    }

    public boolean disableMove() {
        gameScreen.getInfoOutput().append("\n");
        gameScreen.getMoveOverlay().setValidMoves(new HashSet<>(), currentPlayer);
        return false;
    }

    private ArrayList<Direction> pathToDirections(Path path){
        ArrayList<Direction> directions = new ArrayList<>();
        Suspect currentPlayer = this.currentPlayer.getSuspectToken();

        Point previousPoint = new Point((int)currentPlayer.getBoardLocation().getX(), (int)currentPlayer.getBoardLocation().getY());

        Point nextPoint = new Point(path.getStep(0).getY(), path.getStep(0).getX());

        for (int i = 0; i < path.getLength(); i++){
            if (nextPoint.getX() == previousPoint.getX()){
                if (nextPoint.getY() > previousPoint.getY()){
                    directions.add(Direction.SOUTH);
                }else {
                    directions.add(Direction.NORTH);
                }
            }

            else if (nextPoint.getY() == previousPoint.getY()){
                if (nextPoint.getX() > previousPoint.getX()){
                    directions.add(Direction.EAST);
                } else {
                    directions.add(Direction.WEST);
                }
            }

            //Update next and previous
            previousPoint = new Point(path.getStep(i).getY(), path.getStep(i).getX());
            if (i < path.getLength()-1){
                nextPoint = new Point(path.getStep(i+1).getY(), path.getStep(i+1).getX());
            }
        }
        path.getSteps().remove(0);
        return directions;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
