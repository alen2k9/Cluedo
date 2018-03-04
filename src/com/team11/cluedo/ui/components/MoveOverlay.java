/*
<<<<<<< HEAD:src/com/team11/cluedo/ui/MoveOverlay.java
    Code to handle the overlaying of possible moves

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.ui.components;

/*
 * Code to handle the overlay of possible moves.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */


import com.team11.cluedo.board.room.DoorData;
import com.team11.cluedo.ui.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MoveOverlay extends JComponent{

    final private int TILESIZE = 30;

    private ArrayList<OverlayTile> validMoves = new ArrayList<>();

    private GameScreen gameScreen;
    private int currentPlayer;

    private DoorData doorData = new DoorData();

    private ArrayList<Point> doorList = new ArrayList<>();

    private int resolutionScaler;

    public MoveOverlay(GameScreen gameScreen) {
        this.validMoves = new ArrayList<>();
        this.gameScreen = gameScreen;
        this.resolutionScaler = (int)(TILESIZE*gameScreen.getResolution().getScalePercentage());
        fillDoorSet();
    }
    public ArrayList<OverlayTile> getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(ArrayList<OverlayTile> validMoves, GameScreen gameScreen, int currentPlayer) {
        this.validMoves = validMoves;
        this.gameScreen = gameScreen;
        this.currentPlayer = currentPlayer;
    }

    private void fillDoorSet(){

        for (int i = 0; i < doorData.getAllEntryData().size(); i++){
            doorList.addAll(doorData.getEntryData(i));
        }

        /*
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
       */
    }

    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("Valid Moves: " + validMoves.size());
        for (OverlayTile overlayTile : validMoves) {
            this.draw(g, overlayTile);
        }
    }

    public void draw(Graphics g, OverlayTile overlayTile){
        Graphics2D g2 = (Graphics2D) g;

        if (!this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().isInRoom()){
            if (doorList.contains(new Point((int)overlayTile.getLocation().getY(), (int)overlayTile.getLocation().getX()))){
                g2.setColor(new Color(25, 255, 43, 90));
            }

            else {

                switch (this.gameScreen.getGamePlayers().getPlayer(currentPlayer).getSuspectToken().getSuspectID()){
                    //White
                    case (0):
                        g2.setColor(new Color(255,255,255, 90));
                        break;
                    //Green
                    case (1):
                        g2.setColor(new Color(0,128,0, 90));
                        break;
                    //Peacock
                    case (2):
                        g2.setColor(new Color(0, 0, 255, 90));
                        break;
                    //Plum
                    case (3):
                        g2.setColor(new Color(128,0,128, 90));
                        break;
                    //Scarlet
                    case (4):
                        g2.setColor(new Color(255, 0, 0, 90));
                        break;
                    //Mustard
                    case (5):
                        g2.setColor(new Color(204,204,0, 90));
                        break;
                    default :
                        g2.setColor(new Color(0, 0, 0, 90));
                        break;
                }
            }

            g2.fillRect((int)(overlayTile.getLocation().getX() * (this.resolutionScaler)), (int)overlayTile.getLocation().getY() * (this.resolutionScaler),
                    this.resolutionScaler, this.resolutionScaler);
            g2.drawRect((int)(overlayTile.getLocation().getX() * (this.resolutionScaler)), (int)overlayTile.getLocation().getY() * (this.resolutionScaler),
                    this.resolutionScaler, this.resolutionScaler);
        }



    }

    @Override
    public String toString(){
        return validMoves.toString();
    }
}
