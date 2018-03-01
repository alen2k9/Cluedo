package com.team11.cluedo.ui.components;

import com.team11.cluedo.cards.Cards;
import com.team11.cluedo.players.PlayerHand;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PlayerHandLayout extends JPanel {
    private Players gamePlayers;
    private Cards gameCards;
    private PlayerHand playerHand;
    private Resolution resolution;

    private final double scalar = .6;

    public PlayerHandLayout (Cards gameCards, Players gamePlayers, Resolution resolution) {
        this.gameCards = gameCards;
        this.gamePlayers = gamePlayers;
        this.playerHand = gamePlayers.getPlayer(0).getPlayerHand();
        this.resolution = resolution;
        this.setBackground(Color.DARK_GRAY);
        setupLayout();
    }

    private void setupLayout() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        if (gameCards.getPublicEnvelope().size() > 0) {
            this.add(setupPublicCards(), BorderLayout.NORTH);
        }
        JPanel playerCards = new JPanel(new GridBagLayout());
        playerCards.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        ImageIcon card;
        for (int i = 0, x = 0, y = 0 ; i < playerHand.getCardAmount() ; i++, x++) {
            if (i > 0 && x % 2 == 0) {
                x = 0;
                y++;
            }
            gbc.gridx = x; gbc.gridy = y;
            card = new ImageIcon(playerHand.getCard(i).getCardImage().getScaledInstance(
                    (int) ((new ImageIcon(playerHand.getCard(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * scalar),
                    (int) ((new ImageIcon(playerHand.getCard(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * scalar),
                    0));
            playerCards.add(new JLabel(card), gbc);
        }
        TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK,3), "Player Hand");
        border.setTitleFont(new Font("Calibri",Font.BOLD, (int)(20*resolution.getScalePercentage())));
        border.setTitleColor(Color.WHITE);
        playerCards.setBorder(border);
        this.add(playerCards, BorderLayout.CENTER);
    }

    private JPanel setupPublicCards() {
        JPanel publicCards = new JPanel(new GridBagLayout());
        publicCards.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        ImageIcon card;
        for (int i = 0, x = 0, y = 0 ; i < gameCards.getPublicEnvelope().size() ; i++, x++) {
            if (i == 2) {
                x = 0;
                y = 1;
                gbc.gridwidth = 2;
            }
            card = new ImageIcon(gameCards.getPublicEnvelope().get(i).getCardImage().getScaledInstance(
                    (int) ((new ImageIcon(gameCards.getPublicEnvelope().get(i).getCardImage()).getIconWidth() * resolution.getScalePercentage()) * scalar),
                    (int) ((new ImageIcon(gameCards.getPublicEnvelope().get(i).getCardImage()).getIconHeight() * resolution.getScalePercentage()) * scalar),
                    0));

            gbc.gridx = x; gbc.gridy = y;
            gbc.ipady = 5;
            publicCards.add(new JLabel(card), gbc);
        }

        TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK,3), "Public Cards");
        border.setTitleFont(new Font("Calibri",Font.BOLD, (int)(20*resolution.getScalePercentage())));
        border.setTitleColor(Color.WHITE);
        publicCards.setBorder(border);
        return publicCards;
    }

    public void reDraw(int currentPlayer) {
        this.playerHand = gamePlayers.getPlayer(currentPlayer).getPlayerHand();
        setupLayout();
        this.revalidate();
        this.repaint();
    }
}
