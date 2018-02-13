/**
 * The Main Class of the game.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.Team11.Cluedo;

import com.Team11.Cluedo.Assets.Assets;
import com.Team11.Cluedo.Board.Board;
import com.Team11.Cluedo.Controls.CommandInput;
import com.Team11.Cluedo.Suspects.Players;
import com.Team11.Cluedo.UI.GameScreen;
import com.Team11.Cluedo.UI.MenuScreen;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) throws IOException{
        {
            Assets gameAssets = new Assets();

            Board gameBoard = new Board();
            Players gamePlayers = new Players(6, gameAssets);
            Weapons gameWeapons = new Weapons();

            MenuScreen menuScreen = new MenuScreen(gameAssets);

            GameScreen gameScreen = new GameScreen(gamePlayers, gameBoard, gameWeapons, gameAssets);
            CommandInput gameInput = new CommandInput(gameScreen, gameWeapons);
        }
    }

}
