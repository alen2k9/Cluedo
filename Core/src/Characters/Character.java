package Characters;

import Board.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Character extends JComponent{
    private int x;
    private int y;
    private String name;

    public Character(){

    }

    public Character(int a, int b, String n){
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

    public void setName(String n){
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

    public String getName(){
        return this.name;
    }

    public void draw(Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D) g;
        //Ellipse ellipse = new Ellipse(x, y, 25, 25);
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(x,y, 25,25));
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
