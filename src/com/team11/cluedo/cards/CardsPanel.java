package com.team11.cluedo.cards;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.players.PlayerHand;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class CardsPanel extends JPanel {
    private PlayerHand playerHand;
    private Resolution resolution;

    private int cardAmount;
    private String tabName;

    private boolean alpha, update = false;
    private Timer timer;
    private int targetX, initialX, distance;
    private int posY;

    public CardsPanel(Assets gameAssets, Resolution resolution, String tabName) {
        super(new GridBagLayout());
        this.tabName = tabName;
        this.resolution = resolution;
        setBackground(gameAssets.getDarkerGrey());
        setBorder(new LineBorder(new Color(108,13,13), 3));
        setOpaque(true);

        timer = new Timer(0, e -> {
            if (getX() > targetX) {
                setLocation(getX() - distance, getY());
            } else {
                setLocation(getX() + distance, getY());
            }

            if (targetX - initialX > 0 && getX() >= targetX) {
                setLocation(targetX, getY());
                timer.stop();
            }
            if (targetX - initialX < 0 && getX() <= targetX) {
                setLocation(targetX, getY());
                if (update)
                    updateCards();
                timer.stop();
            }
        });
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void updatePlayerHand(PlayerHand playerHand) {
        this.playerHand = playerHand;
        if (!isAnimating()) {
            if (!isEnabled()) {
                update = true;
                animate();
            } else {
                updateCards();
            }
        }
    }

    private void updateCards() {
        update = false;
        removeAll();
        cardAmount = playerHand.getCardAmount();
        GridBagConstraints gbc = new GridBagConstraints();
        ImageIcon icon = new ImageIcon(playerHand.getCard(0).getCardImage());
        int width = (int)(icon.getIconWidth()*.33*resolution.getScalePercentage());
        int height = (int)(icon.getIconHeight()*.33*resolution.getScalePercentage());

        gbc.gridy = 0; gbc.insets = new Insets(10,2,10,2);
        if (playerHand.getPublicHand().size() > 0) {
            JPanel publicCards = new JPanel(new GridBagLayout());
            TitledBorder titledBorder = new TitledBorder(new EmptyBorder(0,0,0,0),
                    "Public Cards",0,0,new Font("Orange Kid",Font.BOLD,20),Color.WHITE);
            publicCards.setBorder(titledBorder);
            publicCards.setOpaque(false);
            for (int i = 0 ; i < playerHand.getPublicHand().size() ; i++) {
                gbc.gridx = i;
                JLabel card = new JLabel(new ImageIcon(playerHand.getPublicHand().get(i).getCardImage().getScaledInstance(
                        width, height, 0
                )));
                publicCards.add(card, gbc);
            }
            gbc.gridx = 0;
            gbc.gridwidth = playerHand.getPublicHand().size();
            super.add(publicCards, gbc);
            gbc.gridy++;
            gbc.gridwidth = 1;
        }
        for (int i = 0 ; i < cardAmount ; i++) {
            JLabel card = new JLabel(new ImageIcon(playerHand.getCard(i).getCardImage().getScaledInstance(
                    width, height, 0
            )));
            gbc.gridx = i;
            if (i == cardAmount-1) {
                gbc.insets = new Insets(10,2,10,(int)(50*resolution.getScalePercentage()));
            }
            super.add(card, gbc);
        }
        setSize(getPreferredSize());
        setLocation(-getPreferredSize().width+(int)(50*resolution.getScalePercentage()),posY);
        setAlpha(false);
    }

    public void setAlpha(boolean aFlag) {
        this.alpha = aFlag;
    }

    public void animate() {
        if (!isEnabled()) {
            initialX = 0;
            targetX = -(getPreferredSize().width - (int)(50*resolution.getScalePercentage()));
        } else {
            initialX = -(getPreferredSize().width - (int)(50*resolution.getScalePercentage()));
            targetX = 0;
        }

        distance = 2+cardAmount;
        timer.start();
        setEnabled(!isEnabled());
        if (isEnabled())
            alpha = false;
    }

    public boolean isAnimating() {
        return timer.isRunning();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (alpha)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        super.paint(g2);
        g2.setColor(Color.WHITE);
        Font font = new Font("Orange Kid", Font.PLAIN, (int)(30*resolution.getScalePercentage()));
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2.setFont(rotatedFont);
        g2.drawString(tabName,getWidth()-(int)(10*resolution.getScalePercentage()),getHeight()-(int)(15*resolution.getScalePercentage()));
        g2.dispose();
    }
}
