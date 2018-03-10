package com.team11.cluedo.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

    public class TokenComponent extends JComponent {
    protected int tokenID;
    protected int currentRoom;
    protected String tokenName;
    protected Point tokenLocation;
    protected int drawX, drawY;

    private Image tokenImage;
    private int resolutionScalar;

    public TokenComponent(int tokenID, String tokenName, Point tokenLocation, Image tokenImage, Resolution resolution){
        this.tokenID = tokenID;
        this.tokenName = tokenName;
        this.tokenLocation = tokenLocation;
        this.tokenImage = tokenImage;
        this.resolutionScalar = (int)(resolution.getScalePercentage()* Board.TILE_SIZE);
        this.currentRoom = -1;
        this.drawX = (int)(tokenLocation.getX() * resolutionScalar);
        this.drawY = (int)(tokenLocation.getY() * resolutionScalar);
        setBounds();
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
        setBounds();
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
        setBounds();
    }

    @Override
    public void setLocation(Point tokenLocation){
        this.tokenLocation = tokenLocation;
    }

    @Override
    public Point getLocation(){
        return this.tokenLocation;
    }

    public void setCurrentRoom(int currentRoom){
        this.currentRoom = currentRoom;
    }

    public int getCurrentRoom(){
        return currentRoom;
    }

    @Override
    public int getX(){
        return (int)tokenLocation.getX();
    }

    @Override
    public int getY(){
        return (int)tokenLocation.getY();
    }

    private void setBounds() {
        setBounds(drawX, drawY, resolutionScalar, resolutionScalar);
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(tokenImage, drawX, drawY, resolutionScalar, resolutionScalar,null);
    }
}
