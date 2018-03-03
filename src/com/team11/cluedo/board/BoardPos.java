/**
 * Class which handles the concept of each tile on the board
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.board;

import com.team11.cluedo.suspects.Direction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardPos extends JComponent {
    /**
     *  location : The x,y location of the boardPos
     *  isSecret : Boolean used to indicate whether or not the tile is a secrete passageway
     *  isTraversable : Boolean used to indicate whether or not the suspects can playerMove on it
     *  isOccupied : Boolean used to indicate whether or not there is a suspect on the tile or not
     *  type : Enum used to identify what kind of tile the tile is
     *  tileSize : Integer which contains the size of each tile
     *  neighbours : An arrayList which will be used as part of the full suspect playerMovement
     */
    private Point location;
    private boolean isSecret;
    private boolean isTraversable;
    private boolean isOccupied;
    private TileType type;
    private int tileSize;
    //private ArrayList<BoardPos> neighbours = new ArrayList<>();
    private ArrayList<Neighbour> neighbours = new ArrayList<>();

    private class Neighbour{
        Point location;
        Direction direction;
        private Neighbour(Point point, Direction dir){
            this.location = point;
            this.direction = dir;
        }
        private void setLocation(Point point){
            this.location = point;
        }
        private Point getLocation(){
            return this.location;
        }
        private void setDirection(Direction dir){
            this.direction = dir;
        }
        private Direction getDirection() {
            return direction;
        }
    }

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
     *  loc : The x,y location of the tile
     *  secret : Is it a secret
     *  traverse : Is it traversable
     *  occ : Is it occupied
     *  t : What type of tile is it
     *  size : The size of the tile
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
     *  p
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
     *  s
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
     *  t
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
     *  o
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
     *  t
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

    public void addNeighbour(Point position, Direction dir){
        this.neighbours.add(new Neighbour(position, dir));
    }

    public ArrayList<Neighbour> getNeighbours(){
        return this.neighbours;
    }

    public Neighbour getNeighbour(Direction dir){
        int i = 0;
        Neighbour tmp = this.neighbours.get(i);
        while(tmp.getDirection() == dir){
            tmp = this.neighbours.get(i);
        }
        return tmp;
    }

    /**
     * Method to handle the drawing of boardPos object
     *  g : The graphic to draw
     *  p : Where to draw the boardPos
     */
    public void draw(Graphics g, Point p){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
        g2.drawRect((int)p.getX(), (int)p.getY(), this.tileSize, this.tileSize);
    }

    public String toString(){
        StringBuilder s = new StringBuilder();

        s.append("\nLocation: " + this.getLocation());
        s.append("\nIsTraverseable:" + this.isTraversable);
        s.append("\nIsOccupied: " + this.isOccupied());
        s.append("\nTileType: " + this.getType() + "\n");
        return s.toString();
    }


}
