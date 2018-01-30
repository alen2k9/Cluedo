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
    private int tileSize;
    private Boolean traverseable;
    private Boolean isSecret;
    private TileType tileType;
    private Boolean occupied;


    /**
     * Default Constructor for the Class
     */
    public BoardPos(){
        this.x = 0;
        this.y = 0;
        this.traverseable = false;
        this.isSecret = false;
        this.tileType = TileType.HALLWAY;
        this.occupied = false;
    }

    /**
     * Parametrized Constructor for the class
     */
    public BoardPos(int a, int b, int tileSize, Boolean trav, Boolean secret, TileType type, Boolean occ){
        this.x = a;
        this.y = b;
        this.tileSize = tileSize;
        this.traverseable = trav;
        this.isSecret = secret;
        this.tileType = type;
        this.occupied = occ;
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
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }

        /**
         * Hallway Tiles
         */
        else if (this.getTileType() == TileType.HALLWAY){
            g2.setColor(Color.GREEN);
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }

        /**
         * Spawn Tiles
         */
        else if (this.getTileType() == TileType.SPAWN){
            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }

        /**
         * Door Tiles
         */
        else if (this.getTileType() == TileType.DOOR){
            g2.setColor(Color.CYAN);
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }

        /**
         * Secrete Passageway Tiles
         */
        else if (this.getTileType() == TileType.SECRETPASSAGE){
            g2.setColor(Color.RED);
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }

        /**
         * All other tiles
         */
        else {
            g2.setColor(Color.GRAY);
            g2.fillRect(x, y, this.tileSize, this.tileSize);
            g2.drawRect(x,y, this.tileSize,this.tileSize);
            g2.setColor(Color.WHITE);
            g2.drawString("(" + this.x + ", " + this.y + ") ", x , y + 15);
        }


    }


    /**
     * Mutator Methods for Class
     */

    public void setX(int a){
        this.x = a;
    }

    public void setY(int a){
        this.y = a;
    }

    public void setTraverseable(Boolean trav){
        this.traverseable = trav;
    }

    public void setIsSecret(Boolean secret){
        this.isSecret = secret;
    }

    public void setTileType(TileType type){
        this.tileType = type;
    }

    public void setOccupied(Boolean occ){
        this.occupied = occ;
    }


    /**
     *Accessor Methods for Class
     */
    public int getXCoord() {
        return this.x;
    }

    public int getYCoord(){
        return this.y;
    }

    public boolean isTraversable(){
        return this.traverseable;
    }

    public boolean isSecretPassage(){
        return this.isSecret;
    }

    public boolean isOccupied(){
        return this.occupied;
    }

    public TileType getTileType() {
        return this.tileType;
    }

}
