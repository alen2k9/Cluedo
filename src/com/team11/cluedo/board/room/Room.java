/*
  Code to handle the Rooms objects.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.board.room;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Room {
    private ArrayList<Point> roomPoints = new ArrayList<>();
    private ArrayList<Point> weaponPositions = new ArrayList<>();
    private ArrayList<Point> playerPositions = new ArrayList<>();

    private ArrayList<Point> exitPoints = new ArrayList<>();
    private ArrayList<Point> entryPoints = new ArrayList<>();

    private TileType roomType;
    private boolean hasSecretPassage;
    private Point passagePoint;

    public Room(){
        this.roomType = TileType.BLANK;
        this.hasSecretPassage = false;
    }
    public Point getRandomPoint(ArrayList<Point> pointList) {
        Random random = new Random();
        int randomInt = random.nextInt(pointList.size());

        Point retPoint = pointList.get(randomInt);
        pointList.remove(randomInt);
        return retPoint;
    }

    public void addPositions(ArrayList<Point> listToAddTo, ArrayList<Point> listToAddFrom){
        listToAddTo.addAll(listToAddFrom);
    }

    // Setters
    public void setExitPoints(ArrayList<Point> exits){
        this.exitPoints = exits;
    }

    public void setEntryPoints(ArrayList<Point> entries){
        this.entryPoints = entries;
    }

    public void setRoomType(TileType type){
        this.roomType = type;
    }

    public void setHasSecretPassage(boolean hasSecretPassage) {
        this.hasSecretPassage = hasSecretPassage;
    }

    public void setPassagePoint(Point passagePoint) { this.passagePoint = passagePoint;}
    // Getters

    public ArrayList<Point> getRoomPoints() {
        return this.roomPoints;
    }

    public ArrayList<Point> getWeaponPositions() {
        return this.weaponPositions;
    }

    public ArrayList<Point> getPlayerPositions() {
        return this.playerPositions;
    }

    public ArrayList<Point> getExitPoints(){
        return this.exitPoints;
    }

    public ArrayList<Point> getEntryPoints() {
        return this.entryPoints;
    }

    public TileType getRoomType() {
        return this.roomType;
    }

    public boolean hasSecretPassage(){
        return this.hasSecretPassage;
    }

    @Override
    public String toString(){
        return this.getRoomType().toString();
    }
}
