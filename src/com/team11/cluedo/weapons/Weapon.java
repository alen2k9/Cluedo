/*
 * Code to handle the weapon objects and tokens
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */
package com.team11.cluedo.weapons;

import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

public class Weapon extends JComponent {
    private int weaponID;
    private String name;
    private int currentRoom;
    private Point location;
    private Image tokenImage;
    private Resolution resolution;

    public Weapon(int weaponID, String name, Image tokenImage, Resolution resolution){
        this.weaponID = weaponID;
        this.name = name;
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

    public void setCurrentRoom(int c){
        this.currentRoom = c;
    }

    public int getCurrentRoom(){
        return this.currentRoom;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.tokenImage, (int)(this.location.getX() * ((int)(30 * resolution.getScalePercentage()))),
                (int)(this.location.getY() * ((int)(30 * resolution.getScalePercentage()))),
                ((int)(30 * resolution.getScalePercentage())),  ((int)(30 * resolution.getScalePercentage())),null);
    }
}

