package com.team11.cluedo.board;


import java.awt.*;
import java.util.ArrayList;

//Class to handle adding both players and weapons to rooms
public class WeaponPoints {

    private ArrayList<ArrayList<Point>> weaponSpawnList = new ArrayList<>();

    private ArrayList<Point> kitchenWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> ballroomWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> conservatoryWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> diningRoomWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> billiardRoomWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> libraryWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> loungeWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> hallWeaponSpawns = new ArrayList<>();
    private ArrayList<Point> studyWeaponSpawns = new ArrayList<>();

    public WeaponPoints(){
        fillWeaponPoints();
    }


    private void fillWeaponPoints(){
        Point[] kitchenPoints = {new Point(1,3), new Point(2,3), new Point(3,3), new Point(4,3), new Point(5,3), new Point(6,3)};
        Point[] ballroomPoints = {new Point(10,4), new Point(11,4), new Point(12,4), new Point(13,4), new Point(14,4), new Point(15,4)};
        Point[] conservatoryPoints = {new Point(19,3), new Point(20,3), new Point(21,3), new Point(22,3), new Point(23,3), new Point(24,3)};
        Point[] diningRoomPoints = {new Point(1,12), new Point(2,12), new Point(3,12), new Point(4,12), new Point(5,12), new Point(6,12)};
        Point[] billiardRoomPoints = {new Point(19,11), new Point(20,11), new Point(21,11), new Point(22,11), new Point(23,11), new Point(24,11)};
        Point[] libraryPoints = {new Point(19,16), new Point(20,16), new Point(21,16), new Point(22,16), new Point(23,16), new Point(24,16)};
        Point[] loungePoints = {new Point(1,21), new Point(2,21), new Point(3,21), new Point(4,21), new Point(5,21), new Point(6,21)};
        Point[] hallPoints = {new Point(10,24), new Point(11,24), new Point(12,24), new Point(13,24), new Point(14,24), new Point(15,24)};
        Point[] studyPoints = {new Point(19,24), new Point(20,24), new Point(21,24), new Point(22,24), new Point(23,24), new Point(24,24)};


        addPointToList(kitchenWeaponSpawns, kitchenPoints);
        addPointToList(ballroomWeaponSpawns, ballroomPoints);
        addPointToList(conservatoryWeaponSpawns, conservatoryPoints);
        addPointToList(diningRoomWeaponSpawns, diningRoomPoints);
        addPointToList(billiardRoomWeaponSpawns, billiardRoomPoints);
        addPointToList(libraryWeaponSpawns, libraryPoints);
        addPointToList(loungeWeaponSpawns, loungePoints);
        addPointToList(hallWeaponSpawns, hallPoints);
        addPointToList(studyWeaponSpawns, studyPoints);

        weaponSpawnList.add(kitchenWeaponSpawns);
        weaponSpawnList.add(ballroomWeaponSpawns);
        weaponSpawnList.add(conservatoryWeaponSpawns);
        weaponSpawnList.add(diningRoomWeaponSpawns);
        weaponSpawnList.add(billiardRoomWeaponSpawns);
        weaponSpawnList.add(libraryWeaponSpawns);
        weaponSpawnList.add(loungeWeaponSpawns);
        weaponSpawnList.add(hallWeaponSpawns);
        weaponSpawnList.add(studyWeaponSpawns);

    }

    private void addPointToList(ArrayList<Point> roomList, Point[] p){
        for (Point point : p ){
            roomList.add(point);
        }
    }

    public ArrayList<ArrayList<Point>> getWeaponSpawnList() {
        return weaponSpawnList;
    }
}
