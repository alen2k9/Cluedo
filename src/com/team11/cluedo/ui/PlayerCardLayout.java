package com.team11.cluedo.ui;

import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.Suspects;

import javax.swing.*;
import java.awt.*;

public class PlayerCardLayout extends JPanel {
    private Players gamePlayers;
    private Resolution resolution;
    private int numPlayers;

    public PlayerCardLayout (int numPlayers, Players gamePlayers, Resolution resolution) {
        System.out.println("Num players " + numPlayers);
        this.numPlayers = numPlayers;
        this.gamePlayers = gamePlayers;
        this.resolution = resolution;

        this.setLayout(new GridBagLayout());

        switch (numPlayers) {
            case 2: {
                getColumnCardsLayout(numPlayers);
                break;
            }
            case 3: {
                getColumnCardsLayout(numPlayers);
                break;
            }
            case 4: {
                getFourCardsLayout(numPlayers);
                break;
            }
            case 5: {
                getFiveCardsLayout(numPlayers);
                break;
            }
            case 6: {
                getSixCardsLayout(numPlayers);
                break;
            }
        }
    }

    private void getColumnCardsLayout(int numPlayers) {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0  ; i < numPlayers ; i++) {
            System.out.println(i + " Players");
            card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                    0
            ));

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridy = i; gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFourCardsLayout(int numPlayers) {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < numPlayers ; i++, x++) {
            if (i == 0 || i == 3) {
                x = 0;
                y++;
                gbc.gridwidth = 2;
            }
            if (i == 1) {
                x = 0;
                y++;
                gbc.gridwidth = 1;
            }
            card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                    0
            ));

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFiveCardsLayout(int numPlayers) {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < numPlayers ; i++, x++) {
            if (i == 2) {
                x = 0;
                y++;
                gbc.gridwidth = 2;
            }
            if (i == 3) {
                x = 0;
                y++;
                gbc.gridwidth = 1;
            }
            card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                    0
            ));

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getSixCardsLayout(int numPlayers) {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < numPlayers ; i++, x++) {
            if (i % 2 == 0 && i > 0) {
                x = 0;
                y++;
            }
            card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                    (int)((new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                    0
            ));

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
    }
}
