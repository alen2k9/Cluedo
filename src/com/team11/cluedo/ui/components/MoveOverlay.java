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


import com.team11.cluedo.players.Player;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class MoveOverlay extends JComponent{

    private ArrayList<OverlayTile> validMoves = new ArrayList<>();

    private Player currentPlayer;
    private Resolution resolution;

    public MoveOverlay(Player currentPlayer, Resolution resolution) {
        this.validMoves = new ArrayList<>();
        this.currentPlayer = currentPlayer;
        this.resolution = resolution;
    }

    public ArrayList<OverlayTile> getValidMoves() {
        return validMoves;
    }

    public void setValidMoves(ArrayList<OverlayTile> validMoves, Player currentPlayer) {
        this.validMoves = validMoves;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (OverlayTile overlayTile : validMoves) {
            overlayTile.draw(g, currentPlayer, resolution, 0);
        }
    }

    @Override
    public String toString(){
        return validMoves.toString();
    }
}
