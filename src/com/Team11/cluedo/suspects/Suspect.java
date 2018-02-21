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
import com.team11.cluedo.weapons.Weapons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Suspect extends JComponent {
    private int suspectID;
    private String suspectName;
    private String playerName;
    private Point location;
    private Image playerTokenImage;
    private Image playerCardImage;
    private PlayerPoints roomSpawnPoints = new PlayerPoints();

    public final String[] PLAYER_NAMES = new String[] {"Player One", "Player Two", "Player Three",
            "Player Four", "Player Five", "Player Six"};

    /*
     * @param location : The location of the player
     * @param suspectName : The suspectName of the player
     * @param suspectID : The ID associated with the player
     */
    public Suspect(int suspectID, String suspectName, String playerName, Point location, Image playerTokenImage, Image playerCardImage){
        this.suspectID = suspectID;
        this.suspectName = suspectName;
        this.playerName = playerName;
        this.location = location;
        this.playerTokenImage = playerTokenImage;
        this.playerCardImage = playerCardImage;
    }

    /*
     * Method to set the suspectName of the suspect
     * @param n : The suspectName to give them
     */
    public void setName(String n){
        this.suspectName = n;
    }

    /*
     * Accessor Method to return the suspectName of the suspect
     * @return : The suspectName of the suspect
     */
    @Override
    public String getName(){
        return this.suspectName;
    }

    public String getPlayerName() {
        return this.playerName;
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

    private Image getPlayerTokenImage() {
        return this.playerTokenImage;
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

        //System.out.println("Draw Called");
        /*
         * Depending on the suspectID of each suspect they will be drawn as different coloured ellipses
         */

        /*
         * Player One - Miss White
         * Player Two - Mr. Plum
         * Player Three - Ms. Peacock
         * Player Four - Colonel Mustard
         * Player Five - Mr. Green
         * Player Six - Miss. Scarlett
         */

        /*
         * Draw the ellipse at an offset of the suspects location and the size of each tile
         */
        g2.drawImage(this.playerTokenImage, (int)(this.location.getX() * 25), (int)(this.location.getY() * 25), 25, 25,null);
    }

    /*
     * Method for handling the playerMovement of the suspects
     * Very much in a test state for the first sprint of the assignment
     * Only has very basic playerMovement with no restriction on where the suspect can playerMove to
     * Returns 1 if the move can be performed. If not then zero
     */
    public void move(Board gameBoard, Direction dir, int numMove){

        //Use arrayList of directions which will be passed in from the command input.
        //Run a check move method to see if any of the moves will result in being invalid
        //Check to see if the tile that we land on is a door, then see if they want to move to the room
        //If there's a player on the desired path, check to see if the current player will have enough moves to move over them
        //NumMoves will be equal to the length of the arrayList



        int doMoveToRoom = 0;

        if (dir == Direction.NORTH){
            if (gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isTraversable() &&
                    !gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isOccupied()){




                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                this.location.setLocation((int)this.getLoc().getX(),(int)this.getLoc().getY()-1);
                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);


                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()) + " is now occupied");
                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()) + " is now unoccupied");

                doMoveToRoom = checkRoomMove(gameBoard);
                //Player is moving to room
                if (doMoveToRoom == 1){

                }

                //Player is not moving to the room so move them back one space to outside the room
                else{

                }

            }
            else{
                if (!gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isTraversable()){
                    System.out.println("Cannot move up tile is not traversable");
                }

                else if (gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isOccupied()){
                    System.out.println("Cannot move up as the tile is occupied");
                }
            }
        }

        //Moving Right
        else if (dir == Direction.EAST){
            if (gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()+1).isTraversable() &&
                    !gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()+1).isOccupied()){

                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                this.location.setLocation((int)this.getLoc().getX()+1,(int)this.getLoc().getY());
                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);

                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()) + " is now occupied");
                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()) + " is now unoccupied");

                doMoveToRoom = checkRoomMove(gameBoard);
                //Player is moving to room
                if (doMoveToRoom == 1){

                }

                //Player is not moving to the room so move them back one space to outside the room
                else{

                }
            }
            else{
                if (!gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isTraversable()){
                    System.out.println("Cannot move right tile is not traversable");
                }

                else if (gameBoard.getBoard((int)this.getLoc().getY()-1, (int)this.getLoc().getX()).isOccupied()) {
                    System.out.println("Cannot move right as the tile is occupied");
                }
            }
        }

        //Moving down
        else if (dir == Direction.SOUTH){
            if (gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()).isTraversable() &&
                    !gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()).isOccupied()){

                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                this.location.setLocation((int)this.getLoc().getX(),(int)this.getLoc().getY()+1);
                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);

                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()) + "is now occupied");
                System.out.println("Position : " + gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()) + "is now unoccupied");

                doMoveToRoom = checkRoomMove(gameBoard);
                if (doMoveToRoom == 1){

                }

                //Player is not moving to the room so move them back one space to outside the room
                else{

                }
            }

            else{
                if (!gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()).isTraversable()){
                    System.out.println("Cannot move down tile is not traversable");
                }

                else if (gameBoard.getBoard((int)this.getLoc().getY()+1, (int)this.getLoc().getX()).isOccupied()){
                    System.out.println("Cannot move down as the tile is occupied");
                }
            }
        }

        //Moving left
        else if (dir == Direction.WEST){
            if (gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()-1).isTraversable() &&
                    !gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()-1).isOccupied()){

                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(false);
                this.location.setLocation((int)this.getLoc().getX()-1,(int)this.getLoc().getY());
                gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).setOccupied(true);


                doMoveToRoom = checkRoomMove(gameBoard);
                if (doMoveToRoom == 1){

                }

                //Player is not moving to the room so move them back one space to outside the room
                else{

                }
            }

            else{
                if (!gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()-1).isTraversable()){
                    System.out.println("Cannot move up tile is not traversable");
                }

                else if (gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()-1).isOccupied()){
                    System.out.println("Cannot move up as the tile is occupied");
                }
            }
        }
    }


    private int checkRoomMove(Board gameBoard){
        int doMove;
        if (gameBoard.getBoard((int)this.getLoc().getY(), (int)this.getLoc().getX()).getType() == TileType.DOOR){
            doMove = JOptionPane.showConfirmDialog(null, "Do you want to move into this room?","Move to WeaponPoints", JOptionPane.YES_NO_OPTION);
            //Yes
            if (doMove == 0){
                doMove = 1;
            }

            //No
            else{
                doMove = -1;
            }
        }

        else{
            doMove = 0;
        }
        return doMove;
    }


}
