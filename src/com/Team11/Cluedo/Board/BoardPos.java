/**
 * Class which handles the concept of each tile on the board
 *
 * Authors :     Jack Geraghty - 16384181
 *               Conor Beenham -
 *               Alen Thomas   -
 */

package com.Team11.Cluedo.Board;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardPos extends JComponent {
    /**
     * @Param location : The x,y location of the boardPos
     * @Param isSecret : Boolean used to indicate whether or not the tile is a secrete passageway
     * @Param isTraversable : Boolean used to indicate whether or not the suspects can playerMove on it
     * @Param isOccupied : Boolean used to indicate whether or not there is a suspect on the tile or not
     * @Param type : Enum used to identify what kind of tile the tile is
     * @Param tileSize : Integer which contains the size of each tile
     * @Param neighbours : An arrayList which will be used as part of the full suspect playerMovement
     */
    private Point location;
    private boolean isSecret;
    private boolean isTraversable;
    private boolean isOccupied;
    private TileType type;
    private int tileSize;
    private ArrayList<BoardPos> neighbours = new ArrayList<>();

    /**
     * Default Constructor
     */
    public BoardPos(){
        this.location.setLocation(0,0);
        this.isSecret = false;
        this.isTraversable = false;
        this.isOccupied = false;
        this.type = TileType.BLANK;
        this.tileSize = 0;
    }

    /**
     * Parametrized Constructor
     * @param loc : The x,y location of the tile
     * @param secret : Is it a secret
     * @param traverse : Is it traversable
     * @param occ : Is it occupied
     * @param t : What type of tile is it
     * @param size : The size of the tile
     */
    public BoardPos(Point loc, boolean secret, boolean traverse, boolean occ, TileType t, int size){
        this.location = loc;
        this.isSecret = secret;
        this.isTraversable = traverse;
        this.isOccupied = occ;
        this.type = t;
        this.tileSize = size;
    }

    /**
     * Method to set the location of the tile
     * @param p
     */
    public void setLoc(Point p){
        this.location.setLocation(p);
    }

    /**
     * Method to return the location of the tile
     * @return
     */
    public Point getLocation(){
        return this.location;
    }

    /**
     * Method to set whether or not the tile is a secret passageway
     * @param s
     */
    public void setSecret(boolean s){
        this.isSecret = s;
    }

    /**
     * Method to return if the location is a secret
     * @return
     */
    public boolean isSecret() {
        return this.isSecret;
    }

    /**
     * Method to set whether or not the tile is traversable
     * @param t
     */
    public void setTraversable(boolean t){
        this.isTraversable = t;
    }

    /**
     * Method to return if the tile is traversable or not
     * @return
     */
    public boolean isTraversable(){
        return this.isTraversable;
    }

    /**
     * Method to see if the tile is occupuied or not
     * @param o
     */
    public void setOccupied(boolean o){
        this.isOccupied = o;
    }

    /**
     * Method to return whether or not the tile is occupied
     * @return
     */
    public boolean isOccupied(){
        return this.isOccupied;
    }

    /**
     * Method to set the type of tile it is
     * @param t
     */
    public void setType(TileType t){
        this.type = t;
    }

    /**
     * Method to return what type of tile it is
     * @return
     */
    public TileType getType(){
        return this.type;
    }

    /**
     * Method to handle the drawing of boardPos object
     * @param g : The graphic to draw
     * @param p : Where to draw the boardPos
     */
    public void draw(Graphics g, Point p){
        Graphics2D g2 = (Graphics2D) g;

        /**
         * Blank Tile
         */
        if (this.getType() == TileType.BLANK){
            g2.setColor(Color.BLACK);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        /**
         * Hallway Tile
         */
        else if(this.getType() == TileType.HALLWAY){
            g2.setColor(Color.GREEN);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        /**
         * DOOR Tile
         */
        else if(this.getType() == TileType.DOOR){
            g2.setColor(Color.CYAN);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        /**
         * Spawn Tile
         */
        else if(this.getType() == TileType.SPAWN){
            g2.setColor(Color.RED);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        /**
         * Secret Tile
         */
        else if(this.getType() == TileType.SECRETPASSAGE){
            g2.setColor(Color.BLUE);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        /**
         * All Other Tiles
         */
        else{
            g2.setColor(Color.GRAY);
            g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
            g2.setColor(Color.WHITE);
        }

        //g2.drawString(this.getLocation().getX() + "," + this.getLocation().getY(), (int)this.getLocation().getX() , (int)this.getLocation().getY() + 15);


    }


    /**
     * Mutator Methods for Class
     */

    public void findNeighbours(BoardPos bp){

    }
}
