package com.Team11.Cluedo.Suspects;

import javax.swing.*;
import java.awt.*;

public class Players extends JPanel{

    Suspect suspects[];

    public Players(int numPlayers){
        suspects = createSuspects(numPlayers);
    }

    public String getPos(int i){
        return suspects[i].getPos();
    }


    public Suspect[] createSuspects(int numPlayers){
        String charSetOne[] = {"Jack", "Alen"};
        String charsetOne[] = {"Conor", "Eoghan"};

        int[] spawn_x = {1,1};
        int[] spawn_y = {10, 15};

        Suspect[] suspects = new Suspect[1];

        for (int i = 0; i < numPlayers; i++){
            suspects[i] = new Suspect(spawn_y[i], spawn_x[i], charSetOne[i]);
        }

        return suspects;
    }

    public void paintComponent(Graphics g){

        for (int i  = 0; i < suspects.length; i++){
            System.out.println("attempting to paint");
            suspects[i].draw(g, suspects[i].getXCoord(), suspects[i].getYCoord());
        }
    }
}
