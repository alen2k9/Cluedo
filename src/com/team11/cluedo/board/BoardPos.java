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
    private int tileSize;

    public BoardPos(Point loc, boolean traverse, boolean occ, TileType tileType, TileType roomType, int size){
        this.location = loc;
        this.isTraversable = traverse;
        this.isOccupied = occ;
        this.tileType = tileType;
        this.roomType = roomType;
        this.tileSize = size;

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
        StringBuilder s = new StringBuilder();

        s.append("\nLocation: " + this.getLocation());
        s.append("\nIsTraverseable:" + this.isTraversable);
        s.append("\nIsOccupied: " + this.isOccupied());
        s.append("\nTileType: " + this.getTileType() + "\n");
        return s.toString();
    }


}
