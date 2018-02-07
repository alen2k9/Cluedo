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
            GameScreen gameScreen = new GameScreen();
            Command com = new Command(gameScreen);
        }
    }

}
