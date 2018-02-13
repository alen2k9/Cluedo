/**
 * Code to handle the creation of the menuscreen, character selection and UI.
 *
 * Authors Team11:  Jack Geraghty - 16384181
 *                  Conor Beenham - 16350851
 *                  Alen Thomas   - 16333003
 */

package com.Team11.Cluedo.UI;

import com.Team11.Cluedo.Assets.Assets;

import javax.swing.*;
import java.awt.*;

public class MenuScreen implements Screen {
    private JFrame frame;
    private JPanel mainPanel;

    private int numPlayers;

    private Assets gameAssets;

    public MenuScreen(Assets gameAssets){
        this.gameAssets = gameAssets;
        this.setupScreen();
        this.createScreen();
        this.displayScreen();
    }

    @Override
    public void createScreen() {
        this.frame = new JFrame("Cluedo");
        this.frame.setResizable(true);
        this.frame.getContentPane().add(this.mainPanel);
        this.frame.pack();
    }

    @Override
    public void setupScreen() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        MenuPanel contentPanel = getMenuContent();
        contentPanel.repaint();
        mainPanel.add(contentPanel);
    }

    @Override
    public void displayScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation(dimension.width/2 - frame.getSize().width/2, dimension.height/2 - (frame.getSize().height/2));
        this.frame.setVisible(true);
    }

    @Override
    public void closeScreen() {
        this.frame.removeAll();
        this.frame.dispose();
    }

    @Override
    public void reDraw() {

    }

    public class MenuPanel extends JPanel {
        MenuPanel(LayoutManager layout) {
            super.setLayout(layout);
        }
        @Override
        public void paintComponent(Graphics g) {
            Image splashImage = gameAssets.getSplashImage();
            g.drawImage(splashImage, 0, 0, this);
        }
    }

    private MenuPanel getMenuContent() {
        MenuPanel menuPanel = new MenuPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel playerAmount = new JLabel("Select amount of players: ");
        playerAmount.setForeground(Color.WHITE);
        playerAmount.setBackground(Color.BLACK);
        String[] amountChoice = new String[] {"Two", "Three", "Four", "Five", "Six"};
        JComboBox amountList = new JComboBox(amountChoice);
        amountList.setSelectedIndex(0);

        JButton playButton = new JButton("Play");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5, 5, 5);
        menuPanel.add(playerAmount, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        menuPanel.add(amountList, gbc);
        gbc.gridwidth = 3;
        gbc.gridx = 0; gbc.gridy = 1;
        menuPanel.add(playButton, gbc);

        playButton.addActionListener(e -> {
            numPlayers = amountList.getSelectedIndex() + 2;
            System.out.println(numPlayers);
            closeScreen();
        });

        Dimension imageSize = new Dimension(600, 400);
        menuPanel.setPreferredSize(imageSize);
        return menuPanel;
    }
}
