package com.team11.cluedo.players;

import com.team11.cluedo.board.Board;
import com.team11.cluedo.suspects.Direction;
import com.team11.cluedo.suspects.Suspect;
import com.team11.cluedo.suspects.SuspectData;

import java.util.ArrayList;
import java.util.HashMap;

public class Players {
    private HashMap<Integer, Player> players;
    private int playerCount;

    public Players() {
        playerCount = 0;
        players = new HashMap<>();
    }

    public void addPlayer(String playerName, Suspect suspectToken) {
        SuspectData data = new SuspectData();
        players.put(playerCount, new Player(playerName, suspectToken, data.getSuspectCard(suspectToken.getSuspectID()), data.getSelectedSuspectCard(suspectToken.getSuspectID())));
        System.out.println("Player " + playerCount + " | " + playerName + " | " + suspectToken.getSuspectID() + " " + suspectToken.getSuspectName());
        playerCount++;
    }

    public Player getPlayer(int index) {
        return players.getOrDefault(index, null);
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int playerMove(Board gameBoard, int player, ArrayList<Direction> directions) {
        return getPlayer(player).getSuspectToken().move(gameBoard, directions);
    }
}
