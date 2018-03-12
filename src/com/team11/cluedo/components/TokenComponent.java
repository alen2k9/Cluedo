package com.team11.cluedo.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

    public class TokenComponent extends JLabel {
    protected int tokenID;
    protected int currentRoom;
    protected String tokenName;
    protected Point tokenLocation;
    protected int drawX, drawY;

    private Image tokenImage;
    private int resolutionScalar;

    public TokenComponent(int tokenID, String tokenName, Point tokenLocation, Image tokenImage, Resolution resolution){
        super(new ImageIcon(tokenImage.getScaledInstance((int)(resolution.getScalePercentage()* Board.TILE_SIZE),
                (int)(resolution.getScalePercentage()* Board.TILE_SIZE),0)));

        this.tokenID = tokenID;
        this.tokenName = tokenName;
        this.tokenLocation = tokenLocation;
        this.tokenImage = tokenImage;
        this.resolutionScalar = (int)(resolution.getScalePercentage()* Board.TILE_SIZE);
        this.currentRoom = -1;
        this.drawX = (int)(tokenLocation.getX() * resolutionScalar);
        this.drawY = (int)(tokenLocation.getY() * resolutionScalar);
        //super.setBorder(new LineBorder(Color.MAGENTA, 3));
        setBounds(drawX, drawY, resolutionScalar, resolutionScalar);
        setLocation();
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
        setLocation();
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
        setLocation();
    }

    public void setBoardLocation(Point tokenLocation){
        this.tokenLocation = tokenLocation;
        System.out.println(tokenLocation);
        this.drawX = (int)(tokenLocation.getX() * resolutionScalar);
        this.drawY = (int)(tokenLocation.getY() * resolutionScalar);
        setLocation();
    }

    public Point getBoardLocation(){
        return this.tokenLocation;
    }

    public void setCurrentRoom(int currentRoom){
        this.currentRoom = currentRoom;
    }

    public int getCurrentRoom(){
        return currentRoom;
    }

    public int getBoardX(){
        return (int)tokenLocation.getX();
    }

    public int getBoardY(){
        return (int)tokenLocation.getY();
    }

    private void setLocation() {
        super.setLocation(drawX,drawY);
        System.out.println("location: " + getLocation());
    }
}
