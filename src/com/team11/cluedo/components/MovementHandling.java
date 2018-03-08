package com.team11.cluedo.components;

import com.team11.cluedo.pathfinder.AStarFinder;
import com.team11.cluedo.pathfinder.Path;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.components.OverlayTile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
        int steps = moves.size();
        if(remainingMoves > 0){
            if(currentPlayer.getSuspectToken().checkMove(gameScreen.getGameBoard(), moves)){
                move(moves);
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
                    gameScreen.getInfoOutput().append(currentPlayer.getPlayerName() + " is now in the " + roomName + ", and has 0 moves remaining.\n");
                }
                if (commandInput.isMoveEnabled()) {
                    gameScreen.getMoveOverlay().setValidMoves(findValidMoves(remainingMoves), currentPlayer);
                }
            } else {
                gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + remainingMoves + " moves remaining.\n");
            }
        } else {
            gameScreen.getInfoOutput().append("This path isn't valid.\nYou have " + remainingMoves + " moves remaining.\n");
        }

        if (remainingMoves == 0 && moveEnabled) {
            commandInput.setMoveEnabled(disableMove());
        }
        commandInput.setRemainingMoves(remainingMoves);
    }

    private void move(ArrayList<Direction> moves) {
        while (!moves.isEmpty()) {
            currentPlayer.getSuspectToken().move(gameScreen.getGameBoard(), moves.remove(0));
            move(moves);
        }
    }

    public ArrayList<OverlayTile> findValidMoves(int remainingMoves) {
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

    public void mouseClickMove(Point target, int remainingMoves, boolean moveEnabled){
        Suspect suspectToken = currentPlayer.getSuspectToken();
        AStarFinder finder = new AStarFinder(gameScreen.getGameBoard(), 12, false);
        Path path = finder.findPath(suspectToken, (int)suspectToken.getLoc().getY(), (int)suspectToken.getLoc().getX(), (int)target.getY(), (int)target.getX());
        ArrayList<Direction> moveList = pathToDirections(path);
        playerMovement(moveList, remainingMoves, moveEnabled);
    }

    public boolean enableMove(JTextArea infoOutput) {
        ArrayList<OverlayTile> overlayTiles = new ArrayList<>();

        if (currentPlayer.getSuspectToken().isInRoom()){
            System.out.println("Is in room");
            for (Point point : this.gameScreen.getGameBoard().getRoom(currentPlayer.getSuspectToken().getCurrentRoom()).getEntryPoints()){
                overlayTiles.add(new OverlayTile(point));
            }
            this.gameScreen.getDoorOverlay().setExits(overlayTiles, currentPlayer);
        } else {
            this.gameScreen.getMoveOverlay().setValidMoves(findValidMoves(commandInput.getRemainingMoves()), currentPlayer);
        }

        infoOutput.append("Enter 'U', 'R', 'D', or 'L' to move.\n" +
                "Click on a highlighted square to move.\n" +
                "Use the arrow keys to move.\n" +
                "Close 'move' by typing 'move' or 'finished'\n");
        return true;
    }

    public boolean disableMove() {
        gameScreen.getInfoOutput().append("Moves finished. Enter another command.\n");
        gameScreen.getMoveOverlay().setValidMoves(new ArrayList<>(), currentPlayer);
        gameScreen.getDoorOverlay().setExits(new ArrayList<>(), currentPlayer);
        return false;
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

    public ArrayList<Direction> inputToDirection(String moves, int remainingMoves){
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

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
