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

    public final int NUM_PLAYERS = 6;
    public final Point S1SPAWN = new Point(1,10);
    public final Point S2SPAWN = new Point(1,15);
    public final Point S3SPAWN = new Point(7,24);
    public final Point S4SPAWN = new Point(18,1);
    public final Point S5SPAWN = new Point(20,24);
    public final Point S6SPAWN = new Point(25,8);

    private Suspect[] players = new Suspect[NUM_PLAYERS];


    public Players(){
        for (int i = 0; i < NUM_PLAYERS; i++){
            if (i == 0){
                players[i] = new Suspect(S1SPAWN, "Player One", i);
            }

            else if (i == 1){
                players[i] = new Suspect(S2SPAWN, "Player Two", i);
            }
            else if (i == 2){
                players[i] = new Suspect(S3SPAWN, "Player Three", i);
            }

            else if (i == 3){
                players[i] = new Suspect(S4SPAWN, "Player Four", i);
            }
            else if (i == 4){
                players[i] = new Suspect(S5SPAWN, "Player Five", i);
            }
            else if (i == 5){
                players[i] = new Suspect(S6SPAWN, "Player Six", i);
            }

            //System.out.println("Created new suspect at location : " + players[i].getLoc());

        }

    }

    public void paintComponent(Graphics g){

        for (int i  = 0; i < players.length; i++){
            System.out.println("attempting to paint");
            players[i].draw(g);
        }
    }

}
