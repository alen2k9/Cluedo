package com.Team11.Cluedo.Suspects;

import com.Team11.Cluedo.Board.Board;
import javafx.scene.shape.Ellipse;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Suspect extends JComponent{
    private int x;
    private int y;
    private String name;

    public Suspect(){

    }

    public Suspect(int a, int b, String n){
        this.x = a;
        this.y = b;
        this.name = n;
    }


    public void setXCoord(int a){
        this.x = a;
    }

    public void setYCoord(int a ){
        this.y = a;
    }

    public void setSuspectName(String n){
        this.name = n;
    }

    public int getXCoord(){
        return this.x;
    }

    public int getYCoord(){
        return this.y;
    }

    public String getPos(){
        return ("(" + this.x + ", " + this.y + ") ");
    }

    public String getSuspectName(){
        return this.name;
    }

    /**
     *
     * @param g : The Graphic to Draw
     * @param x : The X coordinate to draw at
     * @param y : THe Y coordinate to draw at
     */
    public void draw(Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D) g;
        System.out.println("Draw Called");
        g2.setColor(Color.RED);

        g2.fill(new Ellipse2D.Double((x*25)+7, (y*25)+7, 20, 20));
        //g2.fillRect(x*25, y*25, 20, 20);
        //g2.drawRect(x*25,y*25, 20, 20);
    }

    /**
     *
     * @param dir : The direction to move in
     * @param board: The main game board
     * @param spaces: Number of spaces to move
     */
    public void move(Directions dir, Board board, int spaces){
        /**
         * Moving up
         */
        if (dir == Directions.NORTH){
            //Check to see
        }

        /**
         * Moving down
         */
        else if (dir == Directions.SOUTH){

        }

        /**
         * Moving right
         */
        else if (dir == Directions.EAST){

        }

        /**
         * Moving left
         */
        else if (dir == Directions.WEST){

        }
    }
}
