/*
    Code to handle the individual tiles of the overlay

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.ui.components;

import com.team11.cluedo.board.room.DoorData;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.ui.Resolution;

import java.awt.*;
import java.util.ArrayList;

public class OverlayTile {
    private final int TILE_SIZE = 30;

    private Point location;
    private DoorData doorData = new DoorData();

    private ArrayList<Point> exitList = new ArrayList<>();

    private ArrayList<OverlayTile> kitchenStudy = new ArrayList<>();
    private ArrayList<OverlayTile> conservatoryLounge = new ArrayList<>();

    private ArrayList<Point> doorList = new ArrayList<>();

    public OverlayTile(Point point){
        this.location = point;
        fillPassageways();
        fillDoorList();
        fillExits();
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

    private void fillPassageways(){
        this.kitchenStudy.add(new OverlayTile(2,6));
        this.kitchenStudy.add(new OverlayTile(22,24));
        this.conservatoryLounge.add(new OverlayTile(20,1));
        this.conservatoryLounge.add(new OverlayTile(2,24));
    }

    private void fillExits(){
        for (int i = 0; i < doorData.getAllEntryData().size(); i++){
            exitList.addAll(doorData.getEntryData(i));
        }
    }

    private void fillDoorList(){
        for (int i = 0; i < doorData.getAllEntryData().size(); i++){
            doorList.addAll(doorData.getEntryData(i));
        }
    }

    public void draw(Graphics g, Player currentPlayer, Resolution resolution,int exitNum){

        Graphics2D g2 = (Graphics2D) g;
        int resolutionScalar = (int)(TILE_SIZE * resolution.getScalePercentage());
        int fontSize = (int)(25 * resolution.getScalePercentage());

        //Player is in a hallway
        if (!currentPlayer.getSuspectToken().isInRoom()){

            //Check to see if we need to draw a tile for a door
            if (doorList.contains(new Point((int)this.getLocation().getY(), (int)this.getLocation().getX()))){
                g2.setColor(new Color(0, 133, 255, 90));
            }

            else {
                //Switch the colour of the overlay for each of the players
                switch (currentPlayer.getSuspectToken().getSuspectID()){
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
                        g2.setColor(new Color(204, 110, 8, 100));
                        break;
                    default :
                        g2.setColor(new Color(0, 0, 0, 90));
                        break;
                }
            }

            g2.fillRect((int)(this.getLocation().getX() * (resolutionScalar)), (int)this.getLocation().getY() * (resolutionScalar),
                    resolutionScalar, resolutionScalar);
            g2.drawRect((int)(this.getLocation().getX() * (resolutionScalar)), (int)this.getLocation().getY() * (resolutionScalar),
                    resolutionScalar, resolutionScalar);
        }

        //Player is in a room
        else{
            //Draw the doors
            if (currentPlayer.getSuspectToken().isInRoom()){

                g2.setColor(new Color(0, 133, 255, 90));

                g2.fillRect((int)(this.getLocation().getY() * (resolutionScalar)), (int)this.getLocation().getX() * (resolutionScalar),
                        resolutionScalar, resolutionScalar);

                g2.drawRect((int)(this.getLocation().getY() * (resolutionScalar)), (int)this.getLocation().getX() * (resolutionScalar),
                        resolutionScalar, resolutionScalar);

                g2.setFont(new Font("Orange Kid",Font.BOLD, fontSize));
                g2.setColor(Color.BLACK);
                g2.drawString(Integer.toString(exitNum),(int)(this.getLocation().getY() * (resolutionScalar) + 10), (int)(this.getLocation().getX() * (resolutionScalar) + 22) );
            }

            //Check to see if the player is in a room with a secret passageway
            //If so draw the secret passageway and where it goes to
            if ((currentPlayer.getSuspectToken().getCurrentRoom()) == 0 || (currentPlayer.getSuspectToken().getCurrentRoom()) == 2 || (currentPlayer.getSuspectToken().getCurrentRoom()) == 6 || (currentPlayer.getSuspectToken().getCurrentRoom()) == 8){

                g2.setColor(new Color(255,0,0, 90));

                if (currentPlayer.getSuspectToken().getCurrentRoom() == 0 || currentPlayer.getSuspectToken().getCurrentRoom() == 8){
                    for (OverlayTile ov : kitchenStudy){
                        g2.fillRect((int)ov.getLocation().getY() * (resolutionScalar), (int)ov.getLocation().getX() * (resolutionScalar), resolutionScalar, resolutionScalar);
                        g2.drawRect((int)ov.getLocation().getY() * (resolutionScalar), (int)ov.getLocation().getX() * (resolutionScalar), resolutionScalar, resolutionScalar);
                    }
                }
                else{
                    for (OverlayTile ov : conservatoryLounge){
                        g2.fillRect((int)ov.getLocation().getY() * (resolutionScalar), (int)ov.getLocation().getX() * (resolutionScalar), resolutionScalar, resolutionScalar);
                        g2.drawRect((int)ov.getLocation().getY() * (resolutionScalar), (int)ov.getLocation().getX() * (resolutionScalar), resolutionScalar, resolutionScalar);
                    }
                }
            }
        }
    }

    @Override
    public String toString(){
        return this.location.toString();
    }
}
