/**
 * Contains the functionality of the board positions as well as the information for drawing them on the screen
 * Jack Geraghty - 16384181
 * Conor Beenham -
 * Alen Thomas   -
 */
package Board;

import javax.swing.*;
import java.awt.*;

public class BoardPos extends JComponent{
    /**
     * Int X :  The x coordinate of the tile
     * Int y :  The y coordinate of the tile
     * Boolean Traversable : Can the tile be moved on
     * TileType tileType : What kind of tile is it?
     * Boolean Occupied : Does it currently have a player on it
     */
    private int x;
    private int y;
    private Boolean traverseable;
    private Boolean isSecret;
    private TileType tileType;
    private Boolean occupied;

    /**
     * Default Constructor for the Class
     */
    public BoardPos(){
        x = 0;
        y = 0;
        traverseable = false;
        isSecret = false;
        tileType = TileType.HALLWAY;
        occupied = false;
    }

    /**
     * Parametrized Constructor for the class
     */
    public BoardPos(int a, int b, Boolean trav, Boolean secret, TileType type, Boolean occ){
        x = a;
        y = b;
        traverseable = trav;
        isSecret = secret;
        tileType = type;
        occupied = occ;
    }


    /**
     * Method which handles the drawing of each of the various types of Tile
     * @param g : Graphics Component
     * @param x : X Coordinate
     * @param y : Y Cooridinate
     */
    public void draw(Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D) g;

        /**
         * Blank Tiles
         */
        if (this.getTileType() == TileType.BLANK){
            g2.setColor(Color.BLACK);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }

        /**
         * Hallway Tiles
         */
        else if (this.getTileType() == TileType.HALLWAY){
            g2.setColor(Color.GREEN);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }

        /**
         * Spawn Tiles
         */
        else if (this.getTileType() == TileType.SPAWN){
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }

        /**
         * Door Tiles
         */
        else if (this.getTileType() == TileType.DOOR){
            g2.setColor(Color.CYAN);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }

        /**
         * Secrete Passageway Tiles
         */
        else if (this.getTileType() == TileType.SECRETPASSAGE){
            g2.setColor(Color.RED);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }

        /**
         * All other tiles
         */
        else {
            g2.setColor(Color.GRAY);
            g2.fillRect(x, y, 30, 30);
            g2.drawRect(x,y, 30,30);
        }


    }


    /**
     * Mutator Methods for Class
     */

    public void setX(int a){
        x = a;
    }

    public void setY(int a){
        y = a;
    }

    public void setTraverseable(Boolean trav){
        traverseable = trav;
    }

    public void setIsSecret(Boolean secret){
        isSecret = secret;
    }

    public void setTileType(TileType type){
        tileType = type;
    }

    public void setOccupied(Boolean occ){
        occupied = occ;
    }


    /**
     *Accessor Methods for Class
     */
    public int getXCoord() {
        return x;
    }

    public int getYCoord(){
        return y;
    }

    public boolean isTraversable(){
        return traverseable;
    }

    public boolean isSecretPassage(){
        return isSecret;
    }

    public boolean isOccupied(){
        return occupied;
    }

    public TileType getTileType() {
        return tileType;
    }

}
