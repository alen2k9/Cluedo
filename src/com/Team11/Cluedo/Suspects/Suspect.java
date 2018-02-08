/**
 * Class which contains the behavior for the suspects of the game
 *
 * Authors :    Jack Geraghty - 163884181
 *              Conor Beenham -
 *              Alen Thomas   -
 */

package com.Team11.Cluedo.Suspects;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Suspect extends JComponent {
    private String name;
    private Point location;
    private int suspectID;

    /**
     * Default Constructor
     */
    public Suspect(){
    }

    /**
     * Parametrized Constuctor
     * @param p : The location of the player
     * @param name : The name of the player
     * @param ID : The ID associated with the player
     */
    public Suspect(Point p, String name, int ID){
        this.name = name;
        this.location = p;
        this.suspectID = ID;
    }

    /**
     * Method to set the name of the suspect
     * @param n : The name to give them
     */
    public void setName(String n){
        this.name = n;
    }

    /**
     * Accessor Method to return the name of the suspect
     * @return : The name of the suspect
     */
    public String getName(){
        return this.name;
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

        System.out.println("Draw Called");
        /**
         * Depending on the suspectID of each suspect they will be drawn as different coloured ellipses
         */

        /**
         * First player is red
         */
        if (this.getSuspectID() == 0){
            g2.setColor(Color.RED);
            System.out.println("Drawing a red ellipse");
        }

        /**
         * Second player is blue
         */
        else if (this.getSuspectID() == 1){
            g2.setColor(Color.BLUE);
        }

        /**
         * Third player is cyan
         */
        else if (this.getSuspectID() == 2){
            g2.setColor(Color.orange);
        }

        /**
         * Fourth player is yellow
         */
        else if (this.getSuspectID() == 3){
            g2.setColor(Color.YELLOW);
        }

        /**
         * Fifth player is white
         */
        else if (this.getSuspectID() == 4){
            g2.setColor(Color.WHITE);
        }

        /**
         * Sixth player is black
         */
        else if (this.getSuspectID() == 5){
            g2.setColor(Color.BLACK);
        }

        /**
         * Otherwise set the colour to green
         */
        else{
            g2.setColor(Color.GREEN);
        }

        /**
         * Draw the ellipse at an offset of the suspects location and the size of each tile
         */
        g2.fill(new Ellipse2D.Double(this.location.getX()*25, this.location.getY()*25, 20,20));
    }

    /**
     * Method for handling the movement of the suspects
     * Very much in a test state for the first sprint of the assignment
     * Only has very basic movement with no restriction on where the suspect can move to
     */
    public void move(Direction dir, int numMove){
        /**
         * Moving up
         */
        if (dir == Direction.NORTH){
            this.location.setLocation(this.location.getX(), this.location.getY() - numMove);
        }

        /**
         * Moving down
         */
        else if (dir == Direction.SOUTH){
            this.location.setLocation(this.location.getX(), this.location.getY() + numMove);
        }

        /**
         * Moving right
         */
        else if (dir == Direction.EAST) {
            this.location.setLocation(this.location.getX() + numMove, this.location.getY());
        }

        /**
         * Moving left
         */
        else if (dir == Direction.WEST){
            this.location.setLocation(this.location.getX() - numMove, this.location.getY());
        }

        else{
            System.out.println("Unknown Direction");
        }
    }
}
