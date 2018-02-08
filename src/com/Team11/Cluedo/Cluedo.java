package com.Team11.Cluedo;

import com.Team11.Cluedo.Board.Board;
import com.Team11.Cluedo.Controls.CommandInput;
import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.UI.GameScreen;

import javax.swing.*;
import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) throws IOException{
        {
            Board gameBoard = new Board();

            Players gamePlayers = new Players(4);
            GameScreen gameScreen = new GameScreen(gamePlayers);
            gameScreen.reDraw();
            CommandInput gameInput = new CommandInput(gameScreen);

            while(gamePlayers.getRemainingPlayers() > 1) {
                for(int i = 0 ; i < gamePlayers.getRemainingPlayers() ; i++) {
                    gameInput.playerTurn(i);
                }
            }
        }
    }

}
