/*
    Code to handle the overlaying of possible moves

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MoveOverlay extends JComponent{

    private ArrayList<OverlayTile> validMoves = new ArrayList<>();
    private Set<Point> doorSet = new HashSet<>();

    public MoveOverlay() {
        this.validMoves = new ArrayList<>();
        fillDoorSet();
    }
    public ArrayList<OverlayTile> getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(ArrayList<OverlayTile> validMoves){
        this.validMoves = validMoves;
    }

    private void fillDoorSet(){
        //Kitchen
        this.doorSet.add(new Point(5, 7));
        //Ballroom
        this.doorSet.add(new Point(9, 6));
        this.doorSet.add(new Point(10, 8));
        this.doorSet.add(new Point(15,8));
        this.doorSet.add(new Point(16,6));
        //Conservatory
        this.doorSet.add(new Point(19,5));
        //Dining Room
        this.doorSet.add(new Point(8,13));
        this.doorSet.add(new Point(7,16));
        //Billiard Room
        this.doorSet.add(new Point(19,10));
        this.doorSet.add(new Point(23,13));
        //Library
        this.doorSet.add(new Point(20,15));
        this.doorSet.add(new Point(18,17));
        //Lounge
        this.doorSet.add(new Point(7,20));
        //Hall
        this.doorSet.add(new Point(12,19));
        this.doorSet.add(new Point(13,19));
        this.doorSet.add(new Point(15,21));
        //Study
        this.doorSet.add(new Point(18,22));
        //Cellar
        this.doorSet.add(new Point(13,17));
    }

    @Override
    public void paintComponent(Graphics g){
        //System.out.println("Valid Moves: " + validMoves.size());
        for (OverlayTile overlayTile : validMoves){
            this.draw(g, overlayTile);
        }
    }

    public void draw(Graphics g, OverlayTile overlayTile){
        Graphics2D g2 = (Graphics2D) g;
        int alpha = 90;

        if (doorSet.contains(new Point(overlayTile.getLocation()))){
            g2.setColor(new Color(243, 247, 2, alpha));
        }

        else {
            g2.setColor(new Color(255, 25, 17, alpha));
        }

        g2.fillRect((int)overlayTile.getLocation().getX()*30, (int)overlayTile.getLocation().getY()*30, 30, 30);
        g2.drawRect((int)overlayTile.getLocation().getX()*30, (int)overlayTile.getLocation().getY()*30, 30,30);
    }

    @Override
    public String toString(){
        return validMoves.toString();
    }
}
