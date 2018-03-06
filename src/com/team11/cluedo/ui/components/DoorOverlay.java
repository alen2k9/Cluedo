/*
 *   Code to handle the overlaying of exit numbers when a player is in a room
 *
 *   Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
*/

package com.team11.cluedo.ui.components;

import com.team11.cluedo.board.room.DoorData;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DoorOverlay extends JComponent{
    private ArrayList<OverlayTile> validExits = new ArrayList<>();

    private Player currentPlayer;
    private Resolution resolution;

    private DoorData doorData = new DoorData();

    public DoorOverlay(Player currentPlayer, Resolution resolution){
        this.validExits = new ArrayList<>();
        this.currentPlayer = currentPlayer;
        this.resolution = resolution;
    }



    public void setExits(ArrayList<OverlayTile> exits,Player currentPlayer){
        this.validExits = exits;
        this.currentPlayer = currentPlayer;
    }


    @Override
    public void paintComponent(Graphics g){
        int i = 1;

        for (OverlayTile overlayTile : validExits){
            overlayTile.draw(g, currentPlayer, resolution, i);
            i++;
        }
    }

}
