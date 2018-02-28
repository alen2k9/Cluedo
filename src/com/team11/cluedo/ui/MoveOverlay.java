package com.team11.cluedo.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MoveOverlay extends JComponent{

    private ArrayList<Point> validMoves = new ArrayList<>();

    public MoveOverlay(ArrayList<Point> validMoves){
        this.validMoves = validMoves;
    }

    public ArrayList<Point> getValidMoves() {
        return validMoves;
    }
    @Override
    public void paintComponent(Graphics g){
        System.out.println("Valid Moves: " + validMoves.size());
        for (Point point : validMoves){
            this.draw(g, point);
        }
    }

    public void draw(Graphics g, Point point){
        Graphics2D g2 = (Graphics2D) g;
        int alpha = 127;
        //System.out.println("Drawing");
        g2.setColor(new Color(255, 25, 17, alpha));
        System.out.println(point);
        g2.fillRect((int)point.getY()*30, (int)point.getX()*30, 30, 30);
        g2.drawRect((int)point.getY()*30, (int)point.getX()*30, 30,30);
    }
}
