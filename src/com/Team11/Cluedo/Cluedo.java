package com.Team11.Cluedo;

import com.Team11.Cluedo.Assets.Assets;
import com.Team11.Cluedo.Board.Board;
import com.Team11.Cluedo.Controls.CommandInput;
import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.UI.GameScreen;

import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) throws IOException{
        {
            Assets gameAssets = new Assets();

            Board gameBoard = new Board();
            Players gamePlayers = new Players(6, gameAssets);
            Weapons gameWeapons = new Weapons();

            GameScreen gameScreen = new GameScreen(gamePlayers, gameBoard, gameWeapons, gameAssets);
            gameScreen.reDraw();
            CommandInput gameInput = new CommandInput(gameScreen);
        }
    }

}
