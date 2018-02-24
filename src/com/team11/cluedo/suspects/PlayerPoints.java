package com.team11.cluedo.suspects;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerPoints {
    
    ArrayList<ArrayList<Point>> playerSpawnLists = new ArrayList<>();
    
    ArrayList<Point> kitchenPlayerSpawns = new ArrayList<>();
    ArrayList<Point> ballroomPlayerSpawns = new ArrayList<>();
    ArrayList<Point> conservatoryPlayerSpawns = new ArrayList<>();
    ArrayList<Point> diningRoomPlayerSpawns = new ArrayList<>();
    ArrayList<Point> billiardRoomPlayerSpawns = new ArrayList<>();
    ArrayList<Point> libraryPlayerSpawns = new ArrayList<>();
    ArrayList<Point> loungePlayerSpawns = new ArrayList<>();
    ArrayList<Point> hallPlayerSpawns = new ArrayList<>();
    ArrayList<Point> studyPlayerSpawns = new ArrayList<>();
    ArrayList<Point> cellarPlayerSpawns = new ArrayList<>();

    
    public PlayerPoints(){
        fillPlayerPoints();
    }
    
    private void fillPlayerPoints(){
        Point[] kitchenPoints = {new Point(1,4), new Point(2,4), new Point(3,4), new Point(4,4), new Point(5,4), new Point(6,4)};
        Point[] ballroomPoints = {new Point(10,5), new Point(11,5), new Point(12,5), new Point(13,5), new Point(14,5), new Point(15,5)};
        Point[] conservatoryPoints = {new Point(19,4), new Point(20,4), new Point(21,4), new Point(22,4), new Point(23,4), new Point(24,4)};
        Point[] diningRoomPoints = {new Point(1,14), new Point(2,14), new Point(3,14), new Point(4,14), new Point(5,14), new Point(6,14)};
        Point[] billiardRoomPoints = {new Point(19,12), new Point(20,12), new Point(21,12), new Point(22,12), new Point(23,12), new Point(24,12)};
        Point[] libraryPoints = {new Point(19,17), new Point(20,17), new Point(21,17), new Point(22,17), new Point(23,17), new Point(24,17)};
        Point[] loungePoints = {new Point(1,22), new Point(2,22), new Point(3,22), new Point(4,22), new Point(5,22), new Point(6,22)};
        Point[] hallPoints = {new Point(10,24), new Point(11,24), new Point(12,24), new Point(13,24), new Point(14,24), new Point(15,24)};
        Point[] studyPoints = {new Point(19,25), new Point(20,25), new Point(21,25), new Point(22,25), new Point(23,25), new Point(24,25)};
        Point[] cellarPoints = {new Point(15,16), new Point(15,15), new Point(15,14), new Point(15,13), new Point(15,12), new Point(15, 11), new Point(14,11)};


        addPointToList(kitchenPlayerSpawns, kitchenPoints);
        addPointToList(ballroomPlayerSpawns, ballroomPoints);
        addPointToList(conservatoryPlayerSpawns, conservatoryPoints);
        addPointToList(diningRoomPlayerSpawns, diningRoomPoints);
        addPointToList(billiardRoomPlayerSpawns, billiardRoomPoints);
        addPointToList(libraryPlayerSpawns, libraryPoints);
        addPointToList(loungePlayerSpawns, loungePoints);
        addPointToList(hallPlayerSpawns, hallPoints);
        addPointToList(studyPlayerSpawns, studyPoints);
        addPointToList(cellarPlayerSpawns, cellarPoints);


        playerSpawnLists.add(kitchenPlayerSpawns);
        playerSpawnLists.add(ballroomPlayerSpawns);
        playerSpawnLists.add(conservatoryPlayerSpawns);
        playerSpawnLists.add(diningRoomPlayerSpawns);
        playerSpawnLists.add(billiardRoomPlayerSpawns);
        playerSpawnLists.add(libraryPlayerSpawns);
        playerSpawnLists.add(loungePlayerSpawns);
        playerSpawnLists.add(hallPlayerSpawns);
        playerSpawnLists.add(studyPlayerSpawns);
        playerSpawnLists.add(cellarPlayerSpawns);
    }

    private void addPointToList(ArrayList<Point> roomList, Point[] p){
        for (Point point : p ){
            roomList.add(point);
        }
    }

    public ArrayList<ArrayList<Point>> getPlayerSpawnList() {
        return playerSpawnLists;
    }
}
