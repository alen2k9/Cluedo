package com.Team11.Cluedo;

import com.Team11.Cluedo.assets.Assets;
import com.Team11.Cluedo.board.Board;
import com.Team11.Cluedo.controls.CommandInput;
import com.Team11.Cluedo.suspects.Players;
import com.Team11.Cluedo.ui.GameScreen;
import com.Team11.Cluedo.weapons.Weapons;

import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) throws IOException{
        {
            Assets gameAssets = new Assets();

            Board gameBoard = new Board();
            Players gamePlayers = new Players(6, gameAssets, gameBoard);
            Weapons gameWeapons = new Weapons();

            GameScreen gameScreen = new GameScreen(gamePlayers, gameBoard, gameWeapons, gameAssets);
            gameScreen.reDraw();
            CommandInput gameInput = new CommandInput(gameScreen, gameWeapons);
        }
    }

}
