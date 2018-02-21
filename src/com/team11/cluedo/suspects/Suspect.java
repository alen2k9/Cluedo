/**
 * Code to handle the behaviour of the suspects.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.suspects;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.board.TileType;
import com.team11.cluedo.ui.Resolution;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Suspect extends JComponent{
    private int suspectID;
    private String suspectName;
    private String playerName;
    private Point location;
    private Image tokenImage;
    private Image cardImage;
    private Image selectedCardImage;
    private Resolution resolution;
    private int numMoves;

    public final String[] PLAYER_NAMES = new String[] {"Player One", "Player Two", "Player Three",
            "Player Four", "Player Five", "Player Six"};

    /*
     * @param location : The location of the player
     * @param suspectName : The suspectName of the player
     * @param suspectID : The ID associated with the player
     */
    public Suspect(int suspectID, String suspectName, String playerName, Point location, Image tokenImage, Image cardImage, Image selectedCardImage, Resolution resolution){
        this.suspectID = suspectID;
        this.suspectName = suspectName;
        this.playerName = playerName;
        this.location = location;
        this.tokenImage = tokenImage;
        this.cardImage = cardImage;
        this.selectedCardImage = selectedCardImage;
        this.resolution = resolution;
        this.numMoves = 0;
    }

    public void setName(String n){
        this.suspectName = n;
    }

    @Override
    public String getName(){
        return this.suspectName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void setNumMoves(int num){
        this.numMoves = num;
    }

    /*
         * Method to set the location of the player
         * @param p : The point to assign the player to
         */
    public void setLoc(Point p){
        this.location = p;
    }

    /*
     * Accessor Method to return the current position of the suspect
     * @return
     */
    public Point getLoc(){
        return this.location;
    }

    /*
     * Method to set the ID of the suspect
     * @param i : The id to assign to the suspect
     */
    public void setSuspectID(int i){
        this.suspectID = i;
    }

    public Image getCardImage() {
        return this.cardImage;
    }

    public Image getSelectedCardImage() {
        return this.selectedCardImage;
    }

    /*
     * Accessor Method to return the id of the suspect
     * @return
     */
    public int getSuspectID(){
        return this.suspectID;
    }

    /*
     * Draw method for drawing each of the suspects on the screen
     * @param g : The graphic to draw
     */
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        /*
         * Draw the ellipse at an offset of the suspects location and the size of each tile
         */
        g2.drawImage(this.tokenImage, (int)(this.location.getX() * ((int)(30 * resolution.getScalePercentage()))),
                (int)(this.location.getY() * ((int)(30 * resolution.getScalePercentage()))),
                ((int)(30 * resolution.getScalePercentage())),  ((int)(30 * resolution.getScalePercentage())),null);
    }


    private boolean checkMove(ArrayList<Direction> moveList, Board gameBoard) {

        Point testerPoint = this.getLoc();

        boolean isValid = false;
        while(!moveList.isEmpty()){
            if (moveList.get(0) == Direction.NORTH){
                if (gameBoard.getBoard((int)testerPoint.getLocation().getY()-1, (int)testerPoint.getLocation().getX()).isTraversable()){
                    testerPoint.setLocation((int)testerPoint.getLocation().getX(),(int)testerPoint.getLocation().getY()-1);
                    isValid = true;
                }
            }

            else if (moveList.get(0) == Direction.EAST){
                if (gameBoard.getBoard((int)testerPoint.getLocation().getY(), (int)testerPoint.getLocation().getX()+1).isTraversable()){
                    testerPoint.setLocation((int)testerPoint.getLocation().getX()+1,(int)testerPoint.getLocation().getY());
                    isValid = true;
                }
            }

            else if (moveList.get(0) == Direction.SOUTH){
                if (gameBoard.getBoard((int)testerPoint.getLocation().getY()+1, (int)testerPoint.getLocation().getX()).isTraversable()){
                    testerPoint.setLocation((int)testerPoint.getLocation().getX(),(int)testerPoint.getLocation().getY()+1);
                    isValid = true;
                }
            }

            else if (moveList.get(0) == Direction.WEST){
                if (gameBoard.getBoard((int)testerPoint.getLocation().getY(), (int)testerPoint.getLocation().getX()-1).isTraversable()){
                    testerPoint.setLocation((int)testerPoint.getLocation().getX()-1,(int)testerPoint.getLocation().getY());
                    isValid = true;
                }
            }
            moveList.remove(0);
        }

        //Have checked all of the moves to see if they go to any non traversal
        //Now check to see of testerPoint is on a tile that is already occupied

        if (gameBoard.getBoard((int)testerPoint.getLocation().getY(), (int) testerPoint.getLocation().getX()).isOccupied()){
            isValid = false;
            System.out.println("Move will result in the current player landing on another player");
        }
        testerPoint = null;
        return isValid;
    }

    /*
     * Method for handling the playerMovement of the suspects
     */
    public int move(Board gameBoard, ArrayList<Direction> dirList){
        boolean isValid = checkMove(dirList, gameBoard);
        if (isValid){
            //Continue with move
            System.out.println("Is valid");
            while(!dirList.isEmpty()){
                System.out.println("nadhol kew");
                if (dirList.get(0) == Direction.NORTH){
                    System.out.println("nadhol kew");
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                    this.getLoc().setLocation((int)this.getLoc().getX(), (int)this.getLoc().getY()-1);
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);
                    System.out.println("Current Pos = " + (this.getLoc()));

                }
                else if (dirList.get(0) == Direction.EAST){
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                    this.getLoc().setLocation((int)this.getLoc().getX()+1, (int)this.getLoc().getY());
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);
                    System.out.println("Current Pos = " + (this.getLoc()));

                }
                else if (dirList.get(0) == Direction.SOUTH){
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                    this.getLoc().setLocation((int)this.getLoc().getX(), (int)this.getLoc().getY()+1);
                    gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);
                    System.out.println("Current Pos = " + (this.getLoc()));

                }
                else if (dirList.get(0) == Direction.WEST) {
                    gameBoard.getBoard((int) this.getLoc().getY(), (int) this.getLoc().getX()).setOccupied(false);
                    this.getLoc().setLocation((int) this.getLoc().getX() - 1, (int) this.getLoc().getY());
                    gameBoard.getBoard((int) this.getLoc().getY(), (int) this.getLoc().getX()).setOccupied(true);
                    System.out.println("Current Pos = " + (this.getLoc()));
                }
                dirList.remove(0);
            }

            if (gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).getType() == TileType.DOOR){
                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                moveToRoom(gameBoard);
            }

            return 1;
        }

        else{
            return -1;
        }
    }


    private void moveToRoom(Board gameBoard){
        System.out.println("Moving to room");
        int currentRoom = findParentRoom(this.getLoc(), gameBoard);
        System.out.println(currentRoom);
        Point nextPoint = gameBoard.getRoom(currentRoom).getRandomPoint(gameBoard.getRoom(currentRoom).getPlayerPositions());

        this.setLoc(nextPoint);
        gameBoard.getRoom(currentRoom).getPlayerPositions().remove(nextPoint);
    }


    private void moveOutOfRoom(Board gameBoard, int exitNum){
        Point currentPoint = this.getLoc();
        int currentRoom = determineRoom(gameBoard);

        gameBoard.getRoom(currentRoom).getPlayerPositions().add(currentPoint);

        if (gameBoard.getRoom(currentRoom).getEntryPoints().size() == 1){
            this.setLoc(gameBoard.getRoom(currentRoom).getEntryPoints().get(0));
        }
        else{
            this.setLoc(gameBoard.getRoom(currentRoom).getEntryPoints().get(exitNum));
        }

    }
    private int findParentRoom(Point point, Board gameBoard){
        int parentRoom = 0;

        return parentRoom;
    }

    private int determineRoom(Board gameBoard){
        switch (gameBoard.getBoard((int)this.getLoc().getX(), (int)this.getLoc().getY()).getType()){
            case KITCHEN:
                return 0;
            case BALLROOM:
                return 1;
            case CONSERVATORY:
                return 2;
            case DININGROOM:
                return 3;
            case BILLIARDROOM:
                return 4;
            case LIBRARY:
                return 5;
            case LOUNGE:
                return 6;
            case HALL:
                return 7;
            case STUDY:
                return 8;
            case CELLAR:
                return 9;
            default:
                return -1;
        }
    }
}
