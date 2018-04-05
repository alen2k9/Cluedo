package com.team11.cluedo.players;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.ui.Resolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class NotesPanel extends JPanel {
    private NotesTable playerNotes;
    private Resolution resolution;

    private Timer timer;
    private Timer openTimer, closeTimer;

    private int tabHeight, maxY;
    private int targetY, initialY, distance;
    private boolean update = false, alpha = false;

    public NotesPanel(Assets gameAssets, Resolution resolution) {
        super(new BorderLayout());
        this.resolution = resolution;
        setBackground(gameAssets.getDarkerGrey());
        setBorder(new LineBorder(new Color(108,13,13), 3));
        setLocation(0,0);

        openTimer = new Timer(0, e -> {
            if (getY() <= targetY) {
                setLocation(getX(), targetY);
                openTimer.stop();
            }
            setLocation(getX(),getY() - distance);
        });

        closeTimer = new Timer(0, e -> {
            if (getY() >= targetY) {
                setLocation(getX(), targetY);
                if(update)
                    updateNotes();
                closeTimer.stop();
            }
            setLocation(getX(),getY() + distance);
        });
    }

    public void updatePlayerNotes(NotesTable playerNotes) {
        this.playerNotes = playerNotes;
        if (!isAnimating()) {
            if (!isEnabled()) {
                update = true;
                animate();
            } else {
                updateNotes();
            }
        }
    }

    public void updateNotes() {
        int xPadding = (int)(15*resolution.getScalePercentage());
        int yPadding = (int)(10*resolution.getScalePercentage());
        removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0,0,yPadding,0);
        JLabel text = new JLabel("Your Notes");
        text.setFont(new Font("Orange Kid", Font.PLAIN, (int)(30 * resolution.getScalePercentage())));
        text.setForeground(Color.WHITE);
        text.setBackground(Color.BLACK);
        panel.add(text, gbc);

        JScrollPane notes = new JScrollPane(playerNotes);
        notes.getVerticalScrollBar().setUnitIncrement(15);
        super.add(panel, BorderLayout.NORTH);
        super.add(notes, BorderLayout.CENTER);
        setSize(notes.getPreferredSize().width + 2*xPadding, (int)(getPreferredSize().height*0.6));
        tabHeight = panel.getPreferredSize().height;
    }

    public void animate() {
        if (!isEnabled()) {
            initialY = maxY-(getHeight());
            targetY = maxY-tabHeight;
        } else {
            initialY = maxY-tabHeight;
            targetY = maxY-(getHeight());
        }
        distance = 5;
        setEnabled(!isEnabled());
        if (!isEnabled()) {
            openTimer.start();
            alpha = false;
        } else {
            closeTimer.start();
        }
    }

    public boolean isAnimating() {
        return closeTimer.isRunning() || openTimer.isRunning();
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getTabHeight() {
        return tabHeight;
    }
}
