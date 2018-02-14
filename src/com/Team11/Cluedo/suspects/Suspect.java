/**
 * Code to handle the behaviour of the suspects.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.Team11.Cluedo.suspects;

import javax.swing.*;
import java.awt.*;

public class Suspect extends JComponent {
    private int suspectID;
    private String suspectName;
    private String playerName;
    private Point location;
    private Image playerTokenImage;
    private Image playerCardImage;

    public final String[] PLAYER_NAMES = new String[] {"Player One", "Player Two", "Player Three",
            "Player Four", "Player Five", "Player Six"};

    /**
     * Parametrized Constuctor
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

    /**
     * Method to set the suspectName of the suspect
     * @param n : The suspectName to give them
     */
    public void setName(String n){
        this.suspectName = n;
    }

    /**
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

    /**
     * Method to set the location of the player
     * @param p : The point to assign the player to
     */
    public void setLoc(Point p){
        this.location = p;
    }

    /**
     * Accessor Method to return the current position of the suspect
     * @return
     */
    public Point getLoc(){
        return this.location;
    }

    /**
     * Method to set the ID of the suspect
     * @param i : The id to assign to the suspect
     */
    public void setSuspectID(int i){
        this.suspectID = i;
    }

    private Image getPlayerTokenImage() {
        return this.playerTokenImage;
    }

    /**
     * Accessor Method to return the id of the suspect
     * @return
     */
    public int getSuspectID(){
        return this.suspectID;
    }

    /**
     * Draw method for drawing each of the suspects on the screen
     * @param g : The graphic to draw
     */
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        //System.out.println("Draw Called");
        /**
         * Depending on the suspectID of each suspect they will be drawn as different coloured ellipses
         */

        /**
         * Player One - Miss White
         * Player Two - Mr. Plum
         * Player Three - Ms. Peacock
         * Player Four - Colonel Mustard
         * Player Five - Mr. Green
         * Player Six - Miss. Scarlett
         */

        /**
         * Draw the ellipse at an offset of the suspects location and the size of each tile
         */
        g2.drawImage(this.playerTokenImage, (int)(this.location.getX() * 25), (int)(this.location.getY() * 25), 25, 25,null);
    }

    /**
     * Method for handling the playerMovement of the suspects
     * Very much in a test state for the first sprint of the assignment
     * Only has very basic playerMovement with no restriction on where the suspect can playerMove to
     */
    public void move(Direction dir, int numMove){
        /**
         * Moving up
         */
        if (dir == Direction.NORTH){
            if (this.getLoc().getY() != 0.0) {
                this.location.setLocation(this.location.getX(), this.location.getY() - numMove);
            }

            else{
                System.out.println("Y : " + this.getY());
                System.out.println("Cannot move up");
            }
        }

        /*
          Moving down
         */
        else if (dir == Direction.SOUTH){
            if (this.getLoc().getY() != 26.0) {
                this.location.setLocation(this.location.getX(), this.location.getY() + numMove);
            }

            else{
                System.out.println("Y : " + this.getY());
                System.out.println("Cannot move down");
            }
        }

        /**
         * Moving right
         */
        else if (dir == Direction.EAST) {
            if (this.getLoc().getX() != 25.0) {
                this.location.setLocation(this.location.getX() + numMove, this.location.getY());
            }

            else{
                System.out.println("X : " + this.getX());
                System.out.println("Cannot move right");
            }
        }

        /**
         * Moving left
         */
        else if (dir == Direction.WEST){
            if (this.getLoc().getX() != 0.0){
                this.location.setLocation(this.location.getX() - numMove, this.location.getY());
            }

            else{
                System.out.println("X : " + this.getX());
                System.out.println("Cannot move left");
            }

        }

        else{
            System.out.println("Unknown Direction");
        }
    }
}
