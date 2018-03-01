package com.team11.cluedo.ui.components;

import com.team11.cluedo.players.Notes;
import com.team11.cluedo.players.Players;

import javax.swing.*;
import java.awt.*;

public class NotesPanel extends JPanel {
    private Players gamePlayers;
    private Notes playerNotes;

    public NotesPanel (Players gamePlayers) {
        this.gamePlayers = gamePlayers;
        this.playerNotes = gamePlayers.getPlayer(0).getPlayerNotes();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        setupPanel();
    }

    private void setupPanel() {
        this.removeAll();
        this.add(playerNotes.getTableHeader(), BorderLayout.NORTH);
        this.add(playerNotes, BorderLayout.CENTER);
    }

    public void reDraw(int currentPlayer) {
        playerNotes = gamePlayers.getPlayer(currentPlayer).getPlayerNotes();
        setupPanel();
        this.validate();
    }
}
