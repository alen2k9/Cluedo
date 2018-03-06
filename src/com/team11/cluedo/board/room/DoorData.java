package com.team11.cluedo.board.room;

import java.awt.*;
import java.util.ArrayList;

public class DoorData {
    private final ArrayList<ArrayList<Point>> entryPoints = new ArrayList<>();
    private final ArrayList<ArrayList<Point>> exitPoints = new ArrayList<>();

    private final ArrayList<Point> kitchenEntryPoints = new ArrayList<>();
    private final ArrayList<Point> kitchenExitPoints = new ArrayList<>();

    private final ArrayList<Point> ballroomEntryPoints = new ArrayList<>();
    private final ArrayList<Point> ballroomExitPoints = new ArrayList<>();

    private final ArrayList<Point> conservatoryEntryPoints = new ArrayList<>();
    private final ArrayList<Point> conservatoryExitPoints = new ArrayList<>();

    private final ArrayList<Point> diningRoomEntryPoints = new ArrayList<>();
    private final ArrayList<Point> diningRoomExitPoints = new ArrayList<>();

    private final ArrayList<Point> billiardRoomEntryPoints = new ArrayList<>();
    private final ArrayList<Point> billiardRoomExitPoints = new ArrayList<>();

    private final ArrayList<Point> libraryEntryPoints = new ArrayList<>();
    private final ArrayList<Point> libraryExitPoints = new ArrayList<>();

    private final ArrayList<Point> loungeEntryPoints = new ArrayList<>();
    private final ArrayList<Point> loungeExitPoints = new ArrayList<>();

    private final ArrayList<Point> hallEntryPoints = new ArrayList<>();
    private final ArrayList<Point> hallExitPoints = new ArrayList<>();

    private final ArrayList<Point> studyEntryPoints = new ArrayList<>();
    private final ArrayList<Point> studyExitPoints = new ArrayList<>();

    private final ArrayList<Point> cellarEntryPoints = new ArrayList<>();
    private final ArrayList<Point> cellarExitPoints = new ArrayList<>();

    public DoorData(){
        fillEntryData();
        fillExitData();
        addLists();
    }

    private void fillEntryData(){
        //  Kitchen
        kitchenEntryPoints.add(new Point(7,5));
        //  Ballroom
        ballroomEntryPoints.add(new Point(6,9));
        ballroomEntryPoints.add(new Point(8,10));
        ballroomEntryPoints.add(new Point(8,15));
        ballroomEntryPoints.add(new Point(6,16));
        //  Conservatory
        conservatoryEntryPoints.add(new Point(5,19));
        //  Dining Room
        diningRoomEntryPoints.add(new Point(13,8));
        diningRoomEntryPoints.add(new Point(16,7));
        //  Billiard Room
        billiardRoomEntryPoints.add(new Point(10,19));
        billiardRoomEntryPoints.add(new Point(13,23));
        //  Library
        libraryEntryPoints.add(new Point(15,20));
        libraryEntryPoints.add(new Point(17,18));
        //  Lounge
        loungeEntryPoints.add(new Point(20,7));
        //  Hall
        hallEntryPoints.add(new Point(19,12));
        hallEntryPoints.add(new Point(19,13));
        hallEntryPoints.add(new Point(21,15));
        //  Study
        studyEntryPoints.add(new Point(22,18));
        //  Cellar
        cellarEntryPoints.add(new Point(17,13));
    }

    private void fillExitData(){
//  Kitchen
        kitchenExitPoints.add(new Point(5,8));
        //  Ballroom
        ballroomExitPoints.add(new Point(8,6));
        ballroomExitPoints.add(new Point(10,9));
        ballroomExitPoints.add(new Point(15,9));
        ballroomExitPoints.add(new Point(17,6));
        //  Conservatory
        conservatoryExitPoints.add(new Point(19,6));
        //  Dining Room
        diningRoomExitPoints.add(new Point(9,13));
        diningRoomExitPoints.add(new Point(7,17));
        //  Billiard Room
        billiardRoomExitPoints.add(new Point(18,10));
        billiardRoomExitPoints.add(new Point(23,14));
        //  Library
        libraryExitPoints.add(new Point(20,14));
        libraryExitPoints.add(new Point(17,17));
        //  Lounge
        loungeExitPoints.add(new Point(7,19));
        //  Hall
        hallExitPoints.add(new Point(12,18));
        hallExitPoints.add(new Point(13,18));
        hallExitPoints.add(new Point(16,21));
        //  Study
        studyExitPoints.add(new Point(18,21));
        //  Cellar
        cellarExitPoints.add(new Point(13,18));
    }

    private void addLists(){
        entryPoints.add(kitchenEntryPoints);
        entryPoints.add(ballroomEntryPoints);
        entryPoints.add(conservatoryEntryPoints);
        entryPoints.add(diningRoomEntryPoints);
        entryPoints.add(billiardRoomEntryPoints);
        entryPoints.add(libraryEntryPoints);
        entryPoints.add(loungeEntryPoints);
        entryPoints.add(hallEntryPoints);
        entryPoints.add(studyEntryPoints);
        entryPoints.add(cellarEntryPoints);

        exitPoints.add(kitchenExitPoints);
        exitPoints.add(ballroomExitPoints);
        exitPoints.add(conservatoryExitPoints);
        exitPoints.add(diningRoomExitPoints);
        exitPoints.add(billiardRoomExitPoints);
        exitPoints.add(libraryExitPoints);
        exitPoints.add(loungeExitPoints);
        exitPoints.add(hallExitPoints);
        exitPoints.add(studyExitPoints);
        exitPoints.add(cellarExitPoints);
    }

    public ArrayList<Point> getEntryData(int index){
        return entryPoints.get(index);
    }

    public ArrayList<Point> getExitData(int index){
        return exitPoints.get(index);
    }

    public ArrayList<ArrayList<Point>> getAllEntryData(){
        return this.entryPoints;
    }
}
