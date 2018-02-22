package com.team11.cluedo.weapons;

import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Weapon extends JComponent {
    /*
     * weaponID: Used to identify the different weapons
     * name : The name of the weapon selected from an array of all the names
     */
    private int weaponID;
    private String name;
    private int currentRoom;
    private Point location;
    private Image tokenImage;
    private Resolution resolution;

    /*
     * Used until we have actual graphics for them
     */
    private String weaponGraphic;

    public Weapon(int i, String n, Image tokenImage, Resolution resolution){
        this.weaponID = i;
        this.name = n;
        this.tokenImage = tokenImage;
        this.resolution = resolution;
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
        /*
         * Draw the ellipse at an offset of the suspects location and the size of each tile
         */
        g2.drawImage(this.tokenImage, (int)(this.location.getX() * ((int)(30 * resolution.getScalePercentage()))),
                (int)(this.location.getY() * ((int)(30 * resolution.getScalePercentage()))),
                ((int)(30 * resolution.getScalePercentage())),  ((int)(30 * resolution.getScalePercentage())),null);
    }
}

