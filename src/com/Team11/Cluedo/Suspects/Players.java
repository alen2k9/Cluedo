/**
 * Code to handle the creation of the suspects into an array
 *
 * Authors :    Jack Geraghty - 16384181
 *              Conor Beenham -
 *              Alen Thomas   -
 */


package com.Team11.Cluedo.Suspects;

import javax.swing.*;
import java.awt.*;

public class Players extends JPanel {
    private int numPlayers;
    private int remainingPlayers;

    private final Point[] SUSPECT_SPAWN = new Point[]
            {new Point(10,1), new Point(15,1), new Point(24,7),
             new Point(1,18), new Point(24,20), new Point(8,25)};
    private final String[] PLAYER_NAME = new String[] {
            "Player One", "Player Two", "Player Three", "Player Four", "Player Five", "Player Six"};

    private Suspect[] players;

    public Players(int numPlayers){
        this.numPlayers = numPlayers;
        this.remainingPlayers = numPlayers;
        this.players = new Suspect[this.numPlayers];

        for (int i = 0; i < this.numPlayers; i++){
            players[i] = new Suspect(SUSPECT_SPAWN[i], PLAYER_NAME[i], i);
        }
    }

    public void paintComponent(Graphics g){
        for (Suspect suspect : players
             ) {
            suspect.draw(g);
        }
    }

    public void playerMove(int player, Direction dir, int space) {
        players[player].move(dir,space);
    }

    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    public void setRemainingPlayers(int amount) {
        this.remainingPlayers = amount;
    }
}
