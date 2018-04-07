/*
 * Class which handles the concept of each tile on the board
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.board;

import com.team11.cluedo.board.room.TileType;

import javax.swing.*;
import java.awt.*;

public class BoardPos extends JComponent {
    private Point location;
    private boolean isTraversable;
    private boolean isOccupied;
    private TileType tileType, roomType;

    public BoardPos(Point loc, boolean traverse, boolean occ, TileType tileType, TileType roomType, int size){
        this.location = loc;
        this.isTraversable = traverse;
        this.isOccupied = occ;
        this.tileType = tileType;
        this.roomType = roomType;

        Dimension buttonSize = new Dimension(size, size);
        super.setPreferredSize(buttonSize);
    }

    public Point getLocation(){
        return this.location;
    }

    public boolean isTraversable(){
        return this.isTraversable;
    }

    public void setOccupied(boolean o){
        this.isOccupied = o;
    }

    public boolean isOccupied(){
        return this.isOccupied;
    }

    public TileType getTileType(){
        return this.tileType;
    }

    public void setRoomType(TileType roomType) {
        this.roomType = roomType;
    }

    public TileType getRoomType(){
        return this.roomType;
    }

    public String toString(){
        return "\nLocation: " + this.getLocation() +
                "\nIsTraverseable:" + this.isTraversable +
                "\nIsOccupied: " + this.isOccupied() +
                "\nTileType: " + this.getTileType() + "\n";
    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color((int)(Math.random()*254),(int)(Math.random()*254),(int)(Math.random()*254),60));
        g.fillRect(0,0,Board.TILE_SIZE, Board.TILE_SIZE);
    }
    //*/
}
