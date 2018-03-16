/*
 * Code to handle the tokens.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.components;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TokenComponent extends JLabel {
    protected int tokenID;
    protected int currentRoom;
    protected String tokenName;
    protected Point tokenLocation;
    protected int drawX, drawY;

    private int resolutionScalar;

    public TokenComponent(int tokenID, String tokenName, Point tokenLocation, Image tokenImage, Resolution resolution){
        super(new ImageIcon(tokenImage.getScaledInstance((int)(resolution.getScalePercentage()* Board.TILE_SIZE),
                (int)(resolution.getScalePercentage()* Board.TILE_SIZE),0)));

        this.tokenID = tokenID;
        this.tokenName = tokenName;
        this.tokenLocation = tokenLocation;
        this.resolutionScalar = (int)(resolution.getScalePercentage()* Board.TILE_SIZE);
        this.currentRoom = -1;
        this.drawX = (int)(tokenLocation.getX() * resolutionScalar);
        this.drawY = (int)(tokenLocation.getY() * resolutionScalar);

        this.createToolTip();
        setToolTipText(tokenName);

        setBounds(drawX, drawY, resolutionScalar, resolutionScalar);
        setLocation();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("ID: " + tokenID + "\nName: " + tokenName + "\nLocation: " + tokenLocation);
            }
        });
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
    }
}
