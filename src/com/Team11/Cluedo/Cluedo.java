package com.Team11.Cluedo;

import com.Team11.Cluedo.Board.Board;
import com.Team11.Cluedo.Controls.Command;
import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.Suspects.Suspect;
import com.Team11.Cluedo.UI.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Cluedo {

    public static void main(String[] args) throws IOException{

        {
            JFrame testFrame = new JFrame("Test");
            testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            testFrame.setSize(800, 800);
            Board bPanel = new Board(25);
            Players players = new Players(1);
            System.out.println("Players Created At: " + players.getPos(0) + " and " + players.getPos(0));
            JLayeredPane test = new JLayeredPane();
            test.add(bPanel) ;
            test.add(players);
            testFrame.add(players);
            testFrame.add(bPanel);
            testFrame.pack();
            testFrame.setVisible(true);
            GameScreen gameScreen = new GameScreen();
            Command com = new Command(gameScreen);
        }
    }

}
