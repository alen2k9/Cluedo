package com.team11.cluedo.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Room {

    //Has list of all points in the room
    //Room has weapon spawn points
    //Room has player spawn points
    //Room has exit points
    //Room can have secret passage way

    //Exit points and secretPassage way stuff will have to be done manually

    private ArrayList<Point> roomPoints = new ArrayList<>();
    private ArrayList<Point> weaponPositions = new ArrayList<>();
    private ArrayList<Point> playerPositions = new ArrayList<>();
    private ArrayList<Point> exitPoints = new ArrayList<>();
    private ArrayList<Point> entryPoints = new ArrayList<>();
    private Point secretPassageIn;
    private Point secretPassageOut;
    private TileType roomType;
    private boolean hasSecretPassage;

    public Room(){
        this.secretPassageIn = null;
        this.secretPassageOut = null;
        this.roomType = TileType.BLANK;
        this.hasSecretPassage = false;
    }

    public void setRoomType(TileType type){
        this.roomType = type;
    }

    public TileType getRoomType(){
        return this.roomType;
    }

    public Point getSecretPassageOut() {
        return this.secretPassageOut;
    }

    public void setSecretPassageOut(Point point){
        this.secretPassageOut = point;
    }

    public Point getSecretPassageIn(){
        return this.secretPassageIn;
    }

    public void setSecretPassageIn(Point point){
        this.secretPassageIn = point;
    }

    public ArrayList<Point> getExitPoints() {
        return exitPoints;
    }

    public ArrayList<Point> getPlayerPositions() {
        return playerPositions;
    }

    public ArrayList<Point> getRoomPoints() {
        return roomPoints;
    }

    public void addPositions(ArrayList<Point> listToAddTo, ArrayList<Point> listToAddFrom){
        listToAddTo.addAll(listToAddFrom);
    }

    public ArrayList<Point> getWeaponPositions() {
        return weaponPositions;
    }

    public boolean hasSecretPassage(){
        return this.hasSecretPassage;
    }

    public void setHasSecretPassage(boolean bool){
        this.hasSecretPassage = bool;
    }

    public ArrayList<Point> getEntryPoints() {
        return entryPoints;
    }

    public Point getRandomPoint(ArrayList<Point> pointList) {
        Random random = new Random();
        int randomInt = random.nextInt(pointList.size());

        Point retPoint = pointList.get(randomInt);
        pointList.remove(randomInt);
        return retPoint;
    }

    public String toString(){
        return this.getRoomType().toString();
    }
}
