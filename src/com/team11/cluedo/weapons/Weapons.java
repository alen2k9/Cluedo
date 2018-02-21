/**
 * Code to handle the weapons of the cluedo game and where they should be placed on to the gameBoard and how they are drawn
 *
 * Authors :    Jack Geraghty - 16384181
 *              Conor Beenham -
 *              Alen Thomas   -
 */

package com.team11.cluedo.weapons;


import com.team11.cluedo.board.Board;
import com.team11.cluedo.board.WeaponPoints;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

public class Weapons extends JComponent{

    private final int NUM_WEAPONS = 6;

    /*
     * NOTES on RoomID for moveWeaponToRoom Method
     * RoomID 0 - kitchen
     * RoomID 1 - ballroom
     * RoomID 2 - Conservatory
     * RoomID 3 - dining room
     * RoomID 4 - billiard room
     * RoomID 5 - library
     * RoomID 6 - lounge
     * RoomID 7 - hall
     * RoomID 8 - study
     */

    /*
     * List of all of the names of the weapons
     */
    private WeaponPoints spawnPoints = new WeaponPoints();

    /*
     * Private inner class to handle each individual weapon
     * Contains information for each weapon such as its name location and id
     */
    private class Weapon extends JComponent {
        /*
         * weaponID: Used to identify the different weapons
         * name : The name of the weapon selected from an array of all the names
         */
        private int weaponID;
        private String name;
        private Point location;
        private int currentRoom;

        /*
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
            g2.fill(new Ellipse2D.Double((int)this.getLocation().getX()*((int)(30 * resolution.getScalePercentage())), (int)this.getLocation().getY()*((int)(30 * resolution.getScalePercentage())), 20,20));
            g2.setColor(Color.WHITE);

            g2.drawString(this.weaponGraphic,(int)this.getLocation().getX()*25 + 6, (int)this.getLocation().getY()*25 + 14);

        }
    }

    /*
     * Array of all of the weapons for the game
     */
    private Weapon[] weapons = new Weapon[NUM_WEAPONS];
    private Board gameBoard;
    private Resolution resolution;

    public Weapons(Board gameBoard, Resolution resolution){
        String[] weaponGraphics = {"C","D", "LP", "Re", "R", "S"};
        String[] weaponName = {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
        this.gameBoard = gameBoard;
        this.resolution = resolution;
        for (int i = 0; i < NUM_WEAPONS; i++){
            weapons[i] = new Weapon(i, weaponName[i],weaponGraphics[i]);
        }
        /*
         * Add all weapons to rooms
         */
        for (int i = 0; i < weapons.length; i++){
            addWeaponToBoard(weapons[i].getWeaponID());
        }
    }

    private void addWeaponToBoard(int weaponID){
        Random random = new Random();
        int randomInt = random.nextInt(gameBoard.getRooms().size()-1);

        //ArrayList<Point> tmpList = gameBoard.getRoom(randomInt).getWeaponPositions();

        Point spawnPoint = gameBoard.getRoom(randomInt).getRandomPoint(gameBoard.getRoom(randomInt).getWeaponPositions());
        weapons[weaponID].setLocation(spawnPoint);
        weapons[weaponID].setCurrentRoom(randomInt);
        gameBoard.getRoom(randomInt).getWeaponPositions().remove(spawnPoint);

    }

    public void moveWeaponToRoom(int weaponID, int roomID){
        Point currentPoint = weapons[weaponID].getLocation();
        int currRoom = weapons[weaponID].getCurrentRoom();
        Point nextPoint = (gameBoard.getRoom(roomID).getRandomPoint(gameBoard.getRoom(roomID).getWeaponPositions()));

        gameBoard.getRoom(currRoom).getWeaponPositions().add(currentPoint);

        weapons[weaponID].setLocation(nextPoint);
        gameBoard.getRoom(roomID).getWeaponPositions().remove(nextPoint);
    }



    /*
     * Method to get a weapon at a specific index in the weapons array
     * @param i : The index of the weapon
     * @return : Reference to weapon at index i
     */
    private Weapon getWeaponAtIndex(int i){
        return weapons[i];
    }

    public void paintComponent(Graphics g){
        for(int i = 0; i < weapons.length; i++){
            weapons[i].draw(g);
        }
    }
}
