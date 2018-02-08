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
            Board gameBoard = new Board();
            Players gamePlayers = new Players();

            GameScreen gameScreen = new GameScreen(gamePlayers);
            Command com = new Command(gameScreen);
        }
    }

}
