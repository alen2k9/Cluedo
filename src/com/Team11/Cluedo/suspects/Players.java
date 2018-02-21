/**
 * Code to handle the players in the game.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */


package com.team11.cluedo.suspects;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

public class Players extends JComponent {
    private int numPlayers;
    private int remainingPlayers;

    private final Point[] SUSPECT_SPAWN = new Point[]
            {new Point(10,1), new Point(15,1), new Point(24,7),
             new Point(24,20), new Point(8,25), new Point(1,18)};
    private final String[] SUSPECT_NAME = new String[] {
            "Miss White", "Mr. Green", "Ms. Peacock", "Mr. Plum", "Miss Scarlett", "Colonel Mustard"};
    private Image[] playerTokens, playerCards, selectedCards;
    private Resolution resolution;

    /**
     * Player One - Miss White
     * Player Two - Mr. Green
     * Player Three - Ms. Peacock
     * Player Four - Mr. Plum
     * Player Five - Miss Scarlett
     * Player Six - Colonel Mustard
     *
     * */
    private Suspect[] players;

    public Players(int numPlayers, Assets gameAssets, Resolution resolution){
        this.numPlayers = numPlayers;
        this.remainingPlayers = numPlayers;
        this.players = new Suspect[this.numPlayers];
        this.resolution = resolution;

        this.playerTokens = new Image[] {gameAssets.getWhiteToken(), gameAssets.getGreenToken(), gameAssets.getPeacockToken(),
        gameAssets.getPlumToken(), gameAssets.getScarletToken(), gameAssets.getMustardToken()};

        this.playerCards = new Image[] {gameAssets.getWhiteCard(), gameAssets.getGreenCard(), gameAssets.getPeacockCard(),
        gameAssets.getPlumCard(), gameAssets.getScarletCard(), gameAssets.getMustardCard()};

        this.selectedCards = new Image[] {gameAssets.getSelectedWhiteCard(), gameAssets.getSelectedGreenCard(), gameAssets.getSelectedPeacockCard(),
        gameAssets.getSelectedPlumCard(), gameAssets.getSelectedScarletCard(), gameAssets.getSelectedMustardCard()};

        for (int i = 0; i < this.numPlayers; i++){
            players[i] = new Suspect(i, SUSPECT_NAME[i], SUSPECT_NAME[i], SUSPECT_SPAWN[i], playerTokens[i], playerCards[i], selectedCards[i], this.resolution);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        for (Suspect suspect : players) {
            suspect.draw(g);
        }
    }

    public void playerMove(int player, Direction dir, int space) {
        players[player].move(dir,space);
    }

    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setPlayer(int index, int selectedChar, String playerName) {
        players[index] = new Suspect(index, SUSPECT_NAME[selectedChar], playerName, SUSPECT_SPAWN[selectedChar],
                playerTokens[selectedChar], playerCards[selectedChar], selectedCards[selectedChar], resolution);
    }

    public Suspect getPlayer(int index) {
        return players[index];
    }

    public void setRemainingPlayers(int amount) {
        this.remainingPlayers = amount;
    }
}
