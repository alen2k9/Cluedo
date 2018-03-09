/*
 * Code to handle the display of players and current player
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui.components;

import com.team11.cluedo.players.Players;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

public class PlayerLayout extends JPanel {
    private Players gamePlayers;
    private Resolution resolution;
    private int currentPlayer;

    public PlayerLayout(Players gamePlayers, Resolution resolution, int currentPlayer) {
        this.gamePlayers = gamePlayers;
        this.resolution = resolution;
        this.currentPlayer = currentPlayer;
        getCardLayout();
    }

    private void getCardLayout() {
        this.setLayout(new GridBagLayout());
        switch (gamePlayers.getPlayerCount()) {
            case 2: {
                getColumnCardsLayout();
                break;
            }
            case 3: {
                getColumnCardsLayout();
                break;
            }
            case 4: {
                getFourCardsLayout();
                break;
            }
            case 5: {
                getFiveCardsLayout();
                break;
            }
            case 6: {
                getSixCardsLayout();
                break;
            }
        }
    }

    private void getColumnCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0  ; i < gamePlayers.getPlayerCount() ; i++) {
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridy = i; gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFourCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
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
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getFiveCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();

        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
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
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc);
        }
    }

    private void getSixCardsLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon card;
        for(int i = 0, x = 0, y = 0 ; i < gamePlayers.getPlayerCount() ; i++, x++) {
            if (i % 2 == 0 && i > 0) {
                x = 0;
                y++;
            }
            if (i == currentPlayer) {
                card = new ImageIcon(gamePlayers.getPlayer(i).getCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            } else {
                card = new ImageIcon(gamePlayers.getPlayer(i).getSelectedCardImage().getScaledInstance(
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * .66),
                        (int) ((new ImageIcon(gamePlayers.getPlayer(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * .66),
                        0));
            }

            JLabel playerCard = new JLabel(card);
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            this.add(playerCard, gbc, i);
        }
    }

    public void reDraw(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.removeAll();
        getCardLayout();
        this.revalidate();
        repaint();
        paintComponent(getGraphics());
    }
}
