/**
 * Class which handles the concept of each tile on the board
 *
 * Authors :     Jack Geraghty - 16384181
 *               Conor Beenham - 16350851
 *               Alen Thomas   -
 */

package com.Team11.Cluedo.board;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardPos extends JComponent {
    /**
     * location : The x,y location of the boardPos
     * isSecret : Boolean used to indicate whether or not the tile is a secrete passageway
     * isTraversable : Boolean used to indicate whether or not the suspects can playerMove on it
     * isOccupied : Boolean used to indicate whether or not there is a suspect on the tile or not
     * type : Enum used to identify what kind of tile the tile is
     * tileSize : Integer which contains the size of each tile
     * neighbours : An arrayList which will be used as part of the full suspect playerMovement
     */
    private Point location;
    private boolean isSecret;
    private boolean isTraversable;
    private boolean isOccupied;
    private TileType type;
    private int tileSize;
    private ArrayList<BoardPos> neighbours = new ArrayList<>();


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
     * @param p : Point to set as location
     */
    public void setLoc(Point p){
        this.location.setLocation(p);
    }

    /**
     * Method to return the location of the tile
     * @return : Returns the boardPos location as a point
     */
    public Point getLoc(){
        return this.location;
    }

    /**
     * Method to set whether or not the tile is a secret passageway
     * @param s : Boolean whether the tile is secrete or not
     */
    public void setSecret(boolean s){
        this.isSecret = s;
    }

    /**
     * Method to return if the location is a secret
     * @return : Is the position a secret passage
     */
    public boolean isSecret() {
        return this.isSecret;
    }

    /**
     * Method to set whether or not the tile is traversable
     * @param t : Boolean whether the player can move on the tile
     */
    public void setTraversable(boolean t){
        this.isTraversable = t;
    }

    /**
     * Method to return if the tile is traversable or not
     * @return : If the tile is traversable or not
     */
    public boolean isTraversable(){
        return this.isTraversable;
    }

    /**
     * Method to see if the tile is occupuied or not
     * @param o : Boolean whether it is occupied dor not
     */
    public void setOccupied(boolean o){
        this.isOccupied = o;
    }

    /**
     * Method to return whether or not the tile is occupied
     * @return : Returns whether or not it is occupied or not
     */
    public boolean isOccupied(){
        return this.isOccupied;
    }

    /**
     * Method to set the type of tile it is
     * @param t : Set the tile type to type t
     */
    public void setType(TileType t){
        this.type = t;
    }

    /**
     * Method to return what type of tile it is
     * @return : Returns the type of tile it is
     */
    public TileType getType(){
        return this.type;
    }

    public void addNeighbour(BoardPos position){
        this.neighbours.add(position);
    }

    public ArrayList<BoardPos> getNeighbours(){
        return this.neighbours;
    }

    /**
     * Method to handle the drawing of boardPos object
     * @param g : The graphic to draw
     * @param p : Where to draw the boardPos
     */
    public void draw(Graphics g, Point p){
        Graphics2D g2 = (Graphics2D) g;


        /*switch (this.type) {
            case BLANK:
                g2.setColor(Color.BLACK);
                break;
            case HALLWAY:
                g2.setColor(Color.GREEN);
                break;
            case DOOR:
                g2.setColor(Color.cyan);
                break;
            case SPAWN:
                g2.setColor(Color.RED);
                break;
            case SECRETPASSAGE:
                g2.setColor(Color.blue);
                break;
            default:
                g2.setColor(Color.gray);
                break;
        }*/
        g2.setColor(Color.BLACK);
        g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
        g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
    }

}
