package com.team11.cluedo.ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MoveOverlay extends JComponent{

    private ArrayList<Point> validMoves;

    public MoveOverlay() {
        this.validMoves = new ArrayList<>();
    }

    public ArrayList<Point> getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(ArrayList<Point> validMoves) {
        this.validMoves = validMoves;
        System.out.println(validMoves.size() + " : size : moves : " + validMoves);
    }

    @Override
    public void paintComponent(Graphics g){
        for (Point point : validMoves){
            this.draw(g, point);
        }
    }

    public void draw(Graphics g, Point point){
        Graphics2D g2 = (Graphics2D) g;
        int alpha = 127;
        g2.setColor(new Color(255, 25, 17, alpha));
        g2.fillRect((int)point.getY()*30, (int)point.getX()*30, 30, 30);
        g2.drawRect((int)point.getY()*30, (int)point.getX()*30, 30,30);
    }
}
