package com.team11.cluedo.ui.components;

import com.team11.cluedo.components.T11Label;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import java.awt.*;

public class PlayerChange extends JPanel {
    private JButton doneButton;
    private Resolution resolution;

    public PlayerChange(Resolution resolution) {
        this.resolution = resolution;

        setupButton();

        setLayout(new GridBagLayout());
        setBackground(new Color(0,0,0, 168));
        setVisible(false);
    }

    private void setupButton() {
        doneButton = new JButton("DONE");
        doneButton.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(30 * resolution.getScalePercentage())));
        doneButton.setBorderPainted(false);
        doneButton.setContentAreaFilled(false);
        doneButton.setFocusPainted(false);
        doneButton.setOpaque(false);
        doneButton.setForeground(Color.WHITE);
    }

    public void setPlayerCard(Player player){
        this.removeAll();

        JLabel text = new JLabel(("Pass to player: " + player.getPlayerName()));
        text.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(20 * resolution.getScalePercentage())));
        text.setForeground(Color.WHITE);
        text.setBackground(Color.BLACK);

        ImageIcon icon = new ImageIcon(player.getCardImage().getScaledInstance(
                (int)(player.getCardImage().getWidth(this) * resolution.getScalePercentage()),
                (int)(player.getCardImage().getHeight(this) * resolution.getScalePercentage()),
                0
        ));
        JLabel playerCard = new JLabel(icon);

        System.out.println("Card: " + playerCard.getSize() + " Panel: " + getSize());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0;
        super.add(text, gbc);
        gbc.gridy = 1;
        super.add(playerCard, gbc);
        gbc.gridy = 2;
        super.add(doneButton, gbc);
    }

    public JButton getDoneButton() {
        return doneButton;
    }
}
