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
import java.util.ArrayList;
import java.util.Random;

public class Weapons extends JComponent{

    public final int NUM_WEAPONS = 6;

    /**
     * List of all of the names of the weapons
     */
    private String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};

    /**
     * ArrayLists containing all of the spawn points in the for each of the rooms
     */
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
    public void fillKitchen(){
        kitchenSpawns.add(new Point(2,1));
        kitchenSpawns.add(new Point(2,2));
        kitchenSpawns.add(new Point(3,1));
        kitchenSpawns.add(new Point(4,1));
        kitchenSpawns.add(new Point(5,1));
        kitchenSpawns.add(new Point(6,1));
    }

    public void fillBallroom(){
        ballroomSpawns.add(new Point(5, 10));
        ballroomSpawns.add(new Point(5, 11));
        ballroomSpawns.add(new Point(5, 12));
        ballroomSpawns.add(new Point(5, 13));
        ballroomSpawns.add(new Point(5, 14));
        ballroomSpawns.add(new Point(5, 15));
    }

    public void fillConservatory(){
        conservatorySpawns.add(new Point(3,19));
        conservatorySpawns.add(new Point(3,20));
        conservatorySpawns.add(new Point(3,21));
        conservatorySpawns.add(new Point(3,22));
        conservatorySpawns.add(new Point(3,23));
        conservatorySpawns.add(new Point(3,24));
    }

    public void fillDiningroom(){
        diningroomSpawns.add(new Point(12,1));
        diningroomSpawns.add(new Point(12,2));
        diningroomSpawns.add(new Point(12,3));
        diningroomSpawns.add(new Point(12,4));
        diningroomSpawns.add(new Point(12,5));
        diningroomSpawns.add(new Point(12,6));
    }

    public void fillBilliardroom(){
        billiardroomSpawns.add(new Point(9, 19));
        billiardroomSpawns.add(new Point(9, 20));
        billiardroomSpawns.add(new Point(9, 21));
        billiardroomSpawns.add(new Point(9, 22));
        billiardroomSpawns.add(new Point(9, 23));
        billiardroomSpawns.add(new Point(9, 24));
    }

    public void fillLibrary(){
        librarySpawns.add(new Point(24, 18));
        librarySpawns.add(new Point(24, 17));
        librarySpawns.add(new Point(24, 16));
        librarySpawns.add(new Point(24, 15));
        librarySpawns.add(new Point(24, 14));
        librarySpawns.add(new Point(24, 13));
    }

    public void fillLounge(){
        loungeSpawns.add(new Point(1 ,22));
        loungeSpawns.add(new Point(2 ,22));
        loungeSpawns.add(new Point(3 ,22));
        loungeSpawns.add(new Point(4 ,22));
        loungeSpawns.add(new Point(5 ,22));
        loungeSpawns.add(new Point(6 ,22));
    }

    public void fillHall(){
        hallSpawns.add(new Point(10,24));
        hallSpawns.add(new Point(11,24));
        hallSpawns.add(new Point(12,24));
        hallSpawns.add(new Point(13,24));
        hallSpawns.add(new Point(14,24));
        hallSpawns.add(new Point(15,24));
    }

    public void fillStudy(){
        studySpawns.add(new Point(18,24));
        studySpawns.add(new Point(19,24));
        studySpawns.add(new Point(20,24));
        studySpawns.add(new Point(21,24));
        studySpawns.add(new Point(22,24));
        studySpawns.add(new Point(23,24));
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

        public Weapon(){}

        public Weapon(int i, String n){
            this.weaponID = i;
            this.name = n;
        }

        public void setWeaponID(int i){
            this.weaponID = i;
        }

        public int getWeaponID(){
            return this.weaponID;
        }

        public void setName(String n){
            this.name = n;
        }

        public String getName(){
            return this.name;
        }

        public void setLocation(Point p){
            this.location = p;
        }

        public Point getLocation(){
            return this.location;
        }

        public void draw(Graphics g){
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.BLACK);
            System.out.println("Draw Called");
            /**
             * Candlestick
             */
            if (this.getWeaponID() == 0){
                g2.drawString("C", (int)this.getLocation().getY()*25, (int)this.getLocation().getX()*25);
            }

            /**
             * Dagger
             */

            else if (this.getWeaponID() == 1){
                g2.drawString("D", (int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25);
            }

            /**
             * Lead Pipe
             */

            else if (this.getWeaponID() == 2){
                g2.drawString("LP", (int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25);
            }

            /**
             * Revolver
             */

            else if (this.getWeaponID() == 3){
                g2.drawString("Re", (int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25);
            }

            /**
             * Rope
             */

            else if (this.getWeaponID() == 4){
                g2.drawString("R", (int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25);
            }

            /**
             * Spanner
             */

            else if (this.getWeaponID() == 5){
                g2.drawString("S", (int)this.getLocation().getX()*25, (int)this.getLocation().getY()*25);
            }

            else{
                System.out.println("Unknown weapon");
            }
        }
    }

    /**
     * Array of all of the weapons for the game
     */
    private Weapon[] weapons = new Weapon[NUM_WEAPONS];

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

        for (int i = 0; i < weapons.length; i++){
            Random rn = new Random();
            int rand = rn.nextInt(10);
            addWeaponToRoom(i, rand);
        }

        for (int i = 0; i < NUM_WEAPONS; i++){
            weapons[i] = new Weapon(i, weaponName[i]);
        }

    }

    /**
     * Method to get a weapon at a specific index in the weapons array
     * @param i : The index of the weapon
     * @return
     */
    public Weapon getWeaponAtIndex(int i){
        return weapons[i];
    }

    /**
     * Method to add a weapon to a room once an accusation has been made
     * Weapons are placed into the room at a random location from the rooms spawn list
     * @param weaponID : The ID of the weapon to place into the room
     * @param roomID : The room to put the weapon in
     */
    public void addWeaponToRoom(int weaponID, int roomID){

        Random rand = new Random();
        int ranN;

        /**
         * Kitchen
         */
        if (roomID == 0){
            ranN = rand.nextInt(kitchenSpawns.size() + 1);
            weapons[weaponID].setLocation(kitchenSpawns.get(ranN));
            kitchenSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to kitchen at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Ball Room
         */
        else if (roomID == 1){
            ranN = rand.nextInt(ballroomSpawns.size() + 1);
            weapons[weaponID].setLocation(ballroomSpawns.get(ranN));
            ballroomSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Ball Room at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Conservatory
         */
        else if (roomID == 2){
            ranN = rand.nextInt(conservatorySpawns.size() + 1);
            weapons[weaponID].setLocation(conservatorySpawns.get(ranN));
            conservatorySpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Conservatory at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Dining Room
         */
        else if (roomID == 3){
            ranN = rand.nextInt(diningroomSpawns.size() + 1);
            weapons[weaponID].setLocation(diningroomSpawns.get(ranN));
            diningroomSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Dinging Room at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Billiard Room
         */
        else if (roomID == 4){
            ranN = rand.nextInt(billiardroomSpawns.size() + 1);
            weapons[weaponID].setLocation(billiardroomSpawns.get(ranN));
            billiardroomSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Billiard at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Library
         */
        else if (roomID == 5){
            ranN = rand.nextInt(librarySpawns.size() + 1);
            weapons[weaponID].setLocation(librarySpawns.get(ranN));
            librarySpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Library at position " + weapons[weaponID].getLocation().toString());
        }


        /**
         * Lounge
         */
        else if (roomID == 6){
            ranN = rand.nextInt(loungeSpawns.size() + 1);
            weapons[weaponID].setLocation(loungeSpawns.get(ranN));
            loungeSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Lounge at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Hall
         */
        else if (roomID == 7){
            ranN = rand.nextInt(hallSpawns.size() + 1);
            weapons[weaponID].setLocation(hallSpawns.get(ranN));
            hallSpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Hall at position " + weapons[weaponID].getLocation().toString());
        }

        /**
         * Study
         */
        else if (roomID == 8){
            ranN = rand.nextInt(studySpawns.size() + 1);
            weapons[weaponID].setLocation(studySpawns.get(ranN));
            studySpawns.remove(ranN);
            System.out.println(weapons[weaponID].getName() + " added to Study at position " + weapons[weaponID].getLocation().toString());
        }
    }

    public void paintComponent(Graphics g){
        for(int i = 0; i < weapons.length; i++){
            weapons[i].draw(g);
        }
    }
}
