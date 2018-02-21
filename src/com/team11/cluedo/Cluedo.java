/**
 * The Main Class of the game.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.controls.CommandInput;
import com.team11.cluedo.ui.GameScreen;
import com.team11.cluedo.ui.MenuScreen;
import com.team11.cluedo.ui.Resolution;
import com.team11.cluedo.weapons.Weapons;

import java.io.IOException;

public class Cluedo {
    public static void main(String[] args) throws IOException{
        {
            //  Used to handle the assets
            Assets gameAssets = new Assets();

            //  Used to scale on resolutions lower than 1080p
            Resolution resolution = new Resolution();

            //  Game logic components
            Board gameBoard = new Board(resolution);
            Weapons gameWeapons = new Weapons(gameBoard, resolution);

            GameScreen gameScreen = new GameScreen(gameWeapons, gameAssets, resolution);
            CommandInput gameInput = new CommandInput(gameScreen, gameWeapons, gameBoard);

            new MenuScreen(gameAssets, gameScreen, gameInput, gameBoard, resolution);
        }
    }

}
