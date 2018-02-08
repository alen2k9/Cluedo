package com.Team11.Cluedo;

import com.Team11.Cluedo.Board.Board;
import com.Team11.Cluedo.Controls.Command;
import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.UI.GameScreen;

import javax.swing.*;
import java.io.IOException;

public class Cluedo {

    public static void main(String[] args) throws IOException{

        {
<<<<<<< HEAD
            Board gameBoard = new Board(25);
            Players gamePlayers = new Players(6);

            GameScreen gameScreen = new GameScreen(gamePlayers);
=======
            GameScreen gameScreen = new GameScreen();
            Command com = new Command(gameScreen);
>>>>>>> adf0b6d7d8ac1a0cf2b87acbef460a79ca7ca2d6
        }
    }

}
