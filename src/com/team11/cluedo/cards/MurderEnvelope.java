package com.team11.cluedo.cards;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.ui.panel.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MurderEnvelope {
    private SuspectCard suspectCard;
    private RoomCard roomCard;
    private WeaponCard weaponCard;

    private JFrame frame;
    private JPanel panel;

    public MurderEnvelope(SuspectCard suspectCard, RoomCard roomCard, WeaponCard weaponCard) {
        this.suspectCard = suspectCard;
        this.roomCard = roomCard;
        this.weaponCard = weaponCard;

        this.panel = new JPanel(new GridBagLayout());
    }

    public void displayMurderEnvelope() {
        this.frame = new JFrame("Murder Envelope");
        this.panel = new BackgroundPanel(new Assets().getBackgroundTile().getImage(), BackgroundPanel.TILED);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel(new ImageIcon(weaponCard.getCardImage())), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(new ImageIcon(suspectCard.getCardImage())), gbc);
        gbc.gridx = 2;
        panel.add(new JLabel(new ImageIcon(roomCard.getCardImage())), gbc);

        frame.setResizable(false);
        frame.getContentPane().add(panel);
        frame.pack();
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(resolution.width/2 - frame.getSize().width/2, resolution.height/2 - (frame.getSize().height/2));
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                new Timer(3500, e1 -> {
                    frame.dispose();
                    panel.removeAll();
                }).start();
            }
        });
    }

    public SuspectCard getSuspectCard() {
        return suspectCard;
    }

    public RoomCard getRoomCard() {
        return roomCard;
    }

    public WeaponCard getWeaponCard() {
        return weaponCard;
    }
}
