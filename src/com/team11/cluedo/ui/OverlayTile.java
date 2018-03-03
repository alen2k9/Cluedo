/*
    Code to handle the individual tiles of the overlay

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OverlayTile extends JComponent implements MouseListener{
    private Point location;

    public OverlayTile(Point point){
        this.location = point;
        addMouseListener(this);
    }

    public OverlayTile(int x, int y){
        this.location = new Point(x,y);
    }

    public Point getLocation(){
        return this.location;
    }

    public void setLocation(Point point){
        this.location = point;
    }

    @Override
    public String toString(){
        return this.location.toString();
    }

    @Override
    public void mouseClicked(MouseEvent e){
        System.out.println(e.getPoint());
    }

    @Override
    public  void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
}
