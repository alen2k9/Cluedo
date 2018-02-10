/**
 * Code to handle the weapons of the cluedo game and where they should be placed on to the board and how they are drawn
 *
 * Authors :    Jack Geraghty - 16384181
 *              Conor Beenham -
 *              Alen Thomas   -
 */

package com.Team11.Cluedo;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Weapons extends JComponent{

    public final int NUM_WEAPONS = 6;

    /**
     * NOTES on RoomID for moveWeaponToRoom Method
     * RoomID 0 - kitchen
     * RoomID 1 - ballroom
     * RoomID 2 - dining room
     * RoomID 3 - billiard room
     * RoomID 4 - library
     * RoomID 5 - lounge
     * RoomID 6 - hall
     * RoomID 7 - study
     * RoomID 8 - Conservatory
     */

    /**
     * List of all of the names of the weapons
     */
    private String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};

    /**
     * ArrayLists containing all of the spawn points in the for each of the rooms
     */
    private ArrayList<ArrayList<Point>> spawnLists = new ArrayList<>();


    private ArrayList<Point> kitchenSpawns = new ArrayList<>();
    private ArrayList<Point> ballroomSpawns = new ArrayList<>();
    private ArrayList<Point> conservatorySpawns = new ArrayList<>();
    private ArrayList<Point> diningroomSpawns = new ArrayList<>();
    private ArrayList<Point> billiardroomSpawns = new ArrayList<>();
    private ArrayList<Point> librarySpawns = new ArrayList<>();
    private ArrayList<Point> loungeSpawns = new ArrayList<>();
    private ArrayList<Point> studySpawns = new ArrayList<>();
    private ArrayList<Point> hallSpawns = new ArrayList<>();


    /**
     * Methods to add all of the spawn points to each of the spawning lists
     * Each room has six different positions in which a weapon can be as it is possible for all of the weapons to be in a single room
     */
    private void fillKitchen(){
        kitchenSpawns.add(new Point(1, 3));
        kitchenSpawns.add(new Point(2, 3));
        kitchenSpawns.add(new Point(3, 3));
        kitchenSpawns.add(new Point(4, 3));
        kitchenSpawns.add(new Point(5, 3));
        kitchenSpawns.add(new Point(6, 3));
    }

    private void fillBallroom(){
        ballroomSpawns.add(new Point(10,4));
        ballroomSpawns.add(new Point(11,4));
        ballroomSpawns.add(new Point(12,4));
        ballroomSpawns.add(new Point(13,4));
        ballroomSpawns.add(new Point(14,4));
        ballroomSpawns.add(new Point(15,4));
    }

    private void fillConservatory(){
        conservatorySpawns.add(new Point(19,3));
        conservatorySpawns.add(new Point(20,3));
        conservatorySpawns.add(new Point(21,3));
        conservatorySpawns.add(new Point(22,3));
        conservatorySpawns.add(new Point(23,3));
        conservatorySpawns.add(new Point(24,3));
    }

    private void fillDiningroom(){
        diningroomSpawns.add(new Point(1,12));
        diningroomSpawns.add(new Point(2,12));
        diningroomSpawns.add(new Point(3,12));
        diningroomSpawns.add(new Point(4,12));
        diningroomSpawns.add(new Point(5,12));
        diningroomSpawns.add(new Point(6,12));

    }

    private void fillBilliardroom(){
        billiardroomSpawns.add(new Point(19, 11 ));
        billiardroomSpawns.add(new Point(20, 11 ));
        billiardroomSpawns.add(new Point(21, 11 ));
        billiardroomSpawns.add(new Point(22, 11 ));
        billiardroomSpawns.add(new Point(23, 11 ));
        billiardroomSpawns.add(new Point(24, 11 ));
    }

    private void fillLibrary(){
        librarySpawns.add(new Point(19, 16));
        librarySpawns.add(new Point(20, 16));
        librarySpawns.add(new Point(21, 16));
        librarySpawns.add(new Point(22, 16));
        librarySpawns.add(new Point(23, 16));
        librarySpawns.add(new Point(24, 16));
    }

    private void fillLounge(){
        loungeSpawns.add(new Point(1,21));
        loungeSpawns.add(new Point(2,21));
        loungeSpawns.add(new Point(3,21));
        loungeSpawns.add(new Point(4,21));
        loungeSpawns.add(new Point(5,21));
        loungeSpawns.add(new Point(6,21));
    }

    private void fillHall(){
        hallSpawns.add(new Point(10, 24));
        hallSpawns.add(new Point(11, 24));
        hallSpawns.add(new Point(12, 24));
        hallSpawns.add(new Point(13, 24));
        hallSpawns.add(new Point(14, 24));
        hallSpawns.add(new Point(15, 24));
    }

    private void fillStudy(){
        studySpawns.add(new Point(19,24));
        studySpawns.add(new Point(20,24));
        studySpawns.add(new Point(21,24));
        studySpawns.add(new Point(22,24));
        studySpawns.add(new Point(23,24));
        studySpawns.add(new Point(24,24));
    }


    /**
     * Private inner class to handle each individual weapon
     * Contains information for each weapon such as its name location and id
     */
    private class Weapon extends JComponent {
        /**
         * @Param weaponID: Used to identify the different weapons
         * @Param name@ The name of the weapon selected from an array of all the names
         */
        private int weaponID;
        private String name;
        private Point location;
        private int currentRoom;

        /**
         * Used until we have actual graphics for them
         */
        private String weaponGraphic;

        public Weapon(int i, String n, String weaponG){
            this.weaponID = i;
            this.name = n;
            this.weaponGraphic = weaponG;
        }

        public void setWeaponID(int i){
            this.weaponID = i;
        }

        public int getWeaponID(){
            return this.weaponID;
        }

        @Override
        public void setName(String n){
            this.name = n;
        }

        @Override
        public String getName(){
            return this.name;
        }

        @Override
        public void setLocation(Point p){
            this.location = p;
        }

        @Override
        public Point getLocation(){
            return this.location;
        }

        public void setWeaponGraphic(String s){
            this.weaponGraphic = s;
        }

        public String getWeaponGraphic(){return this.weaponGraphic;}

        public void setCurrentRoom(int c){
            this.currentRoom = c;
        }

        public int getCurrentRoom(){
            return this.currentRoom;
        }

        public void draw(Graphics g){
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.BLACK);
            g2.fill(new Ellipse2D.Double((int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25, 20,20));
            g2.setColor(Color.WHITE);

            g2.drawString(this.weaponGraphic,(int)this.getLocation().getX()*25 + 6, (int)this.getLocation().getY()*25 + 14);

        }
    }

    /**
     * Array of all of the weapons for the game
     */
    private Weapon[] weapons = new Weapon[NUM_WEAPONS];
    private String[] weaponGraphics = {"C", "LP", "Re", "R", "D", "S"};

    /**
     * Default Constructor
     */
    public Weapons(){
        fillKitchen();
        fillBallroom();
        fillConservatory();
        fillDiningroom();
        fillBilliardroom();
        fillLibrary();
        fillLounge();
        fillHall();
        fillStudy();
        addAllRooms();

        for (int i = 0; i < NUM_WEAPONS; i++){
            weapons[i] = new Weapon(i, weaponName[i],weaponGraphics[i]);
        }

        /**
         * Add all weapons to rooms
         */
        for (int i = 0; i < weapons.length; i++){
            addWeaponToBoard(weapons[i].getWeaponID());
        }

    }

    private void addWeaponToBoard(int weaponID){
        Random random = new Random();
        int randomInt = random.nextInt(spawnLists.size());
        ArrayList<Point> tmpList = this.spawnLists.get(randomInt);

        Point spawnPoint = getRandomPoint(tmpList);
        //System.out.println(weapons[weaponID].getName() + " = " + spawnPoint.getLocation());
        weapons[weaponID].setLocation(spawnPoint);
        weapons[weaponID].setCurrentRoom(randomInt);
        tmpList.remove(spawnPoint);
    }

    public void moveWeaponToRoom(int weaponID, int roomID){

        Point currentPoint = weapons[weaponID].getLocation();
        int currRoom = weapons[weaponID].getCurrentRoom();
        Point nextPoint = getRandomPoint(spawnLists.get(roomID));

        spawnLists.get(currRoom).add(currentPoint);
        weapons[weaponID].setLocation(nextPoint);
        spawnLists.get(roomID).remove(nextPoint);
    }

    private Point getRandomPoint(ArrayList<Point> pointList){
        Random random = new Random();
        int randomInt = random.nextInt(pointList.size());

        Point retPoint = pointList.get(randomInt);
        //System.out.println("Random Num" + randomInt + "Random Point: " + retPoint.getLocation());
        pointList.remove(randomInt);
        return retPoint;
    }

    private void addAllRooms(){
        this.spawnLists.add(kitchenSpawns);
        this.spawnLists.add(ballroomSpawns);
        this.spawnLists.add(diningroomSpawns);
        this.spawnLists.add(billiardroomSpawns);
        this.spawnLists.add(librarySpawns);
        this.spawnLists.add(loungeSpawns);
        this.spawnLists.add(hallSpawns);
        this.spawnLists.add(studySpawns);
        this.spawnLists.add(conservatorySpawns);
    }



    /**
     * Method to get a weapon at a specific index in the weapons array
     * @param i : The index of the weapon
     * @return
     */
    public Weapon getWeaponAtIndex(int i){
        return weapons[i];
    }


    public void paintComponent(Graphics g){
        for(int i = 0; i < weapons.length; i++){
            weapons[i].draw(g);
        }
    }
}
