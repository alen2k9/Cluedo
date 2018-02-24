package com.team11.cluedo.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void setRoomPoints(ArrayList<Point> pointList){
        this.roomPoints.addAll(pointList);
    }

    public ArrayList<Point> getRoomPoints() {
        return this.roomPoints;
    }

    public void setWeaponPositions(ArrayList<Point> pointsList){
        this.weaponPositions.addAll(pointsList);
    }

    public ArrayList<Point> getWeaponPositions() {
        return this.weaponPositions;
    }

    public void setPlayerPositions(ArrayList<Point> pointsList){
        this.playerPositions.addAll(pointsList);
    }

    public ArrayList<Point> getPlayerPositions() {
        return this.playerPositions;
    }

    public void setExitPoints(ArrayList<Point> pointsList){
        this.exitPoints.addAll(pointsList);
    }

    public ArrayList<Point> getExitPoints(){
        return this.exitPoints;
    }

    public void addExitPoint(Point point){
        this.getExitPoints().add(point);
    }

    public void removeExitPoint(Point point){
        this.getExitPoints().remove(point);
    }

    public void setEntryPoints(ArrayList<Point> pointList){
        this.entryPoints.addAll(pointList);
    }

    public ArrayList<Point> getEntryPoints() {
        return this.entryPoints;
    }

    public void addEntryPoint(Point point){
        this.getEntryPoints().add(point);
    }

    public void removeEntryPoint(Point point){
        this.getEntryPoints().remove(point);
    }

    public void setSecretPassageIn(Point point){
        this.secretPassageIn = point;
    }

    public Point getSecretPassageIn(){
        return this.secretPassageIn;
    }

    public void setSecretPassageOut(Point point){
        this.secretPassageOut = point;
    }

    public Point getSecretPassageOut(){
        return this.secretPassageOut;
    }

    public void setRoomType(TileType type){
        this.roomType = type;
    }

    public TileType getRoomType() {
        return this.roomType;
    }

    public void setHasSecretPassage(boolean hasSecretPassage) {
        this.hasSecretPassage = hasSecretPassage;
    }

    public boolean hasSecretPassage(){
        return this.hasSecretPassage;
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
    public String toString(){
        return this.getRoomType().toString();
    }
}
