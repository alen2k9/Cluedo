/*
  Code to handle the creation of the Menu screen, character selection and UI.

  Authors Team11:  Jack Geraghty - 16384181
                   Conor Beenham - 16350851
                   Alen Thomas   - 16333003
 */

package com.team11.cluedo.ui;

import com.team11.cluedo.assets.Assets;
import com.team11.cluedo.board.Board;
import com.team11.cluedo.players.Player;
import com.team11.cluedo.players.Players;
import com.team11.cluedo.suspects.SuspectData;
import com.team11.cluedo.suspects.Suspects;
import com.team11.cluedo.components.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.io.IOException;

import com.team11.cluedo.ui.panel.*;
import com.team11.cluedo.weapons.Weapons;

public class MenuScreen implements Screen {
    private JFrame frame;
    private JPanel menuPanel;
    private BackgroundPanel selectionPanel;
    private JLayeredPane mainPanel;

    private int numPlayers;
    private int currentPlayer;
    private JTextField nameInput;
    private JButton enterButton;

    private Resolution resolution;
    private GameScreen gameScreen;
    private CommandInput gameInput;

    private Assets gameAssets;
    private boolean doSelection = false;
    private Timer timer;

    public MenuScreen() throws IOException {

        //  Used to handle the assets
        this.gameAssets = new Assets();

        //  Used to scale on resolutions lower than 1080p
        this.resolution = new Resolution();

        //  Game logic components
        Board gameBoard = new Board(resolution, gameAssets);
        Suspects gameSuspects = new Suspects(resolution);
        Weapons gameWeapons = new Weapons(gameBoard, resolution);
        Players gamePlayers = new Players();

        this.gameScreen = new GameScreen(gameBoard, gameSuspects, gameWeapons, gamePlayers, gameAssets, resolution, "Cluedo");
        this.gameInput = new CommandInput(gameScreen);

        /*
        SuspectData suspectData = new SuspectData();
        for (int i = 0; i < 4 ; i++)
            gamePlayers.addPlayer(suspectData.getSuspectName(i), gameSuspects.getSuspect(i), resolution);


        startGame();
        //*/

        //*
        //  Calling functions to create screen
        this.setupScreen(0);
        this.createScreen("Cluedo - Title Screen");
        this.displayScreen();
        //*/
    }

    @Override
    public void createScreen(String name) {
        this.frame = new JFrame(name);
        this.frame.setIconImage(gameAssets.getIcon());
        this.frame.setResizable(false);
        this.frame.getContentPane().add(this.mainPanel);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.frame.pack();
    }

    @Override
    public void setupScreen(int state) {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(getMenuContent(), BorderLayout.CENTER);
        selectionPanel = getPlayerSelection();

        mainPanel = new JLayeredPane();
        menuPanel.setSize(menuPanel.getPreferredSize());
        mainPanel.setSize(menuPanel.getPreferredSize());
        selectionPanel.setSize(menuPanel.getPreferredSize());
        mainPanel.add(menuPanel,0);
        mainPanel.add(selectionPanel,1);
        menuPanel.setOpaque(false);
        selectionPanel.setVisible(false);
    }

    @Override
    public void displayScreen() {
        this.frame.setSize(frame.getPreferredSize());
        this.frame.setLocation(resolution.getScreenSize().width/2 - frame.getSize().width/2, resolution.getScreenSize().height/2 - (frame.getSize().height/2));
        this.frame.setVisible(true);
    }

    @Override
    public void closeScreen() {
        this.mainPanel.removeAll();
        this.frame.removeAll();
        this.frame.dispose();
    }

    @Override
    public void reDraw(int currentPlayer) {

    }

    public class MenuPanel extends JPanel {
        MenuPanel(LayoutManager layout) {
            super.setLayout(layout);
        }

        @Override
        public void paintComponent(Graphics g) {
            Image splashImage = gameAssets.getSplashImage();
            ImageIcon splash = new ImageIcon(splashImage);
            g.drawImage(splashImage, 0, 0, (int)(splash.getIconWidth() * resolution.getScalePercentage()),(int)(splash.getIconHeight() * resolution.getScalePercentage()),this);
        }
    }

    private MenuPanel getMenuContent() {
        MenuPanel menuPanel = new MenuPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel playerAmount = new JLabel("SELECT AMOUNT OF PLAYERS: ");
        playerAmount.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(20 * resolution.getScalePercentage())));
        playerAmount.setForeground(Color.WHITE);
        playerAmount.setBackground(Color.BLACK);

        JButton playButton = new JButton("PLAY");
        playButton.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(30 * resolution.getScalePercentage())));
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);
        playButton.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5,5, 5, 5);
        menuPanel.add(playerAmount, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 2;
        JComboBox amountList = setupComboBox();
        menuPanel.add(amountList, gbc);
        gbc.gridwidth = 3;
        gbc.gridx = 0; gbc.gridy = 1;
        menuPanel.add(playButton, gbc);

        playButton.addActionListener(e -> {
            if (!doSelection) {
                selectionPanel.setVisible(true);
                menuPanel.setEnabled(false);
                playButton.setEnabled(false);
                doSelection = true;
                this.numPlayers = amountList.getSelectedIndex() + 2;
                this.currentPlayer = 0;
                timer = new Timer(2, e1 -> {
                    if (menuPanel.getY() <= -frame.getHeight()) {
                        menuPanel.setVisible(false);
                        menuPanel.removeAll();
                        timer.stop();
                    }
                    menuPanel.setLocation(menuPanel.getX(), menuPanel.getY() - 20);
                });
                timer.start();
            }
        });

        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButton.setForeground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButton.setForeground(Color.WHITE);
            }
        });

        ImageIcon splash = new ImageIcon(gameAssets.getSplashImage());
        Dimension imageSize = new Dimension((int)(splash.getIconWidth() * this.resolution.getScalePercentage()), (int)(splash.getIconHeight() * this.resolution.getScalePercentage()));
        menuPanel.setPreferredSize(imageSize);
        return menuPanel;
    }

    private BackgroundPanel getPlayerSelection() {
        ImageIcon backgroundTile = this.gameAssets.getBackgroundTile();
        Image backgroundImage = backgroundTile.getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage, BackgroundPanel.TILED);
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton[] characterButtons = new JButton[] {
            new JButton(new ImageIcon(this.gameAssets.getWhiteCard())),
            new JButton(new ImageIcon(this.gameAssets.getGreenCard())),
            new JButton(new ImageIcon(this.gameAssets.getPeacockCard())),
            new JButton(new ImageIcon(this.gameAssets.getPlumCard())),
            new JButton(new ImageIcon(this.gameAssets.getScarletCard())),
            new JButton(new ImageIcon(this.gameAssets.getMustardCard()))
        };

        ImageIcon[] selectedCharacters = new ImageIcon[] {
            new ImageIcon(this.gameAssets.getSelectedWhiteCard()),
            new ImageIcon(this.gameAssets.getSelectedGreenCard()),
            new ImageIcon(this.gameAssets.getSelectedPeacockCard()),
            new ImageIcon(this.gameAssets.getSelectedPlumCard()),
            new ImageIcon(this.gameAssets.getSelectedScarletCard()),
            new ImageIcon(this.gameAssets.getSelectedMustardCard())
        };

        for (int i = 0, x = 0, y = 1 ; i < characterButtons.length ; i++, x++) {
            setButtonDisplay(i, characterButtons, selectedCharacters[i]);
            if(i == 3) {
                x = 0;
                y++;
            }
            gbc.gridx = x; gbc.gridy = y;
            backgroundPanel.add(characterButtons[i], gbc);
        }

        gbc.gridx = 1; gbc.gridy = 3;
        this.nameInput = new JTextField(20);

        Font f = new Font("Orange Kid",Font.BOLD, (int)(25*resolution.getScalePercentage()));

        nameInput.setFont(f);
        nameInput.setBackground(Color.WHITE);
        nameInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        nameInput.setForeground(gameAssets.getDarkerGrey());
        nameInput.setBorder(new LineBorder(Color.DARK_GRAY, 5));
        nameInput.setHorizontalAlignment(JTextField.CENTER);
        backgroundPanel.add(nameInput, gbc);
        this.enterButton = new JButton("ENTER");

        enterButton.setFont(new Font("Orange Kid", Font.BOLD, (int)(35 * resolution.getScalePercentage())));
        enterButton.setBorderPainted(false);
        enterButton.setContentAreaFilled(false);
        enterButton.setFocusPainted(false);
        enterButton.setOpaque(false);
        enterButton.setForeground(Color.WHITE);

        //  Action listener for players
        this.enterButton.addActionListener(e -> {
            if(currentPlayer < numPlayers) {
                if(!nameInput.getText().equals("")) {
                    Boolean doContinue = true;
                    String playerName = nameInput.getText();
                    for (int i = 0 ; i < currentPlayer ; i++) {
                        if (playerName.equals(gameScreen.getGamePlayers().getPlayer(i).getPlayerName())) {
                            doContinue = false;
                        }
                    }
                    if(doContinue) {
                        doContinue = false;
                        int index = 0;
                        for (int i = 0; i < characterButtons.length; i++) {
                            if (characterButtons[i].isSelected()) {
                                index = i;
                                characterButtons[i].setSelected(false);
                                characterButtons[i].setEnabled(false);
                                doContinue = true;
                            }
                        }
                        if (doContinue) {
                            gameScreen.getGamePlayers().addPlayer(playerName, gameScreen.getGameSuspects().getSuspect(index), this.resolution);
                            nameInput.setText("");
                            currentPlayer++;

                            for (JButton charButton : characterButtons) {
                                charButton.setSelected(false);
                                charButton.setBorderPainted(false);
                            }

                            if (currentPlayer == numPlayers) {
                                closeScreen();
                                startGame();
                            }
                        } else {
                        JOptionPane.showMessageDialog(null,"No character selected. Please select a character.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,"Name same as previous players. Please enter a new name.");
                        nameInput.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"No name entered. Please enter a name.");
                }
            }
        });

        enterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                enterButton.setForeground(Color.RED);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                enterButton.setForeground(Color.WHITE);
            }
        });

        gbc.gridy += 1;
        backgroundPanel.add(this.enterButton, gbc);
        return backgroundPanel;
    }

    private void setButtonDisplay(int index, JButton[] button, ImageIcon selectedIcon) {
        ImageIcon scaledDefaultIcon = new ImageIcon(((ImageIcon)button[index].getIcon()).getImage().getScaledInstance(
                (int)(selectedIcon.getIconWidth()*resolution.getScalePercentage()),(int)(selectedIcon.getIconHeight()*resolution.getScalePercentage()), 0));
        ImageIcon scaledSelectedIcon = new ImageIcon(selectedIcon.getImage().getScaledInstance(
                (int)(selectedIcon.getIconWidth()*resolution.getScalePercentage()),(int)(selectedIcon.getIconHeight()*resolution.getScalePercentage()), 0));

        int borderThickness = 8;
        SuspectData data = new SuspectData();
        button[index].setBorder(BorderFactory.createLineBorder(data.getSuspectColor(index), borderThickness));

        button[index].setBorderPainted(false);
        button[index].setContentAreaFilled(false);
        button[index].setFocusPainted(false);
        button[index].setOpaque(false);
        button[index].setIcon(scaledDefaultIcon);
        button[index].setSelectedIcon(scaledSelectedIcon);
        button[index].setDisabledIcon(scaledSelectedIcon);

        button[index].addActionListener(e -> {
            if (button[index].isSelected()) {
                button[index].setSelected(false);
            } else {
                for (JButton charButton : button) {
                    charButton.setSelected(false);
                    charButton.setBorderPainted(false);
                }
                button[index].setSelected(true);
                button[index].setBorderPainted(true);
            }

            nameInput.requestFocus();
            frame.getRootPane().setDefaultButton(enterButton);
        });

        button[index].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button[index].isEnabled() && doSelection)
                    button[index].setBorderPainted(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button[index].isSelected() && doSelection)
                    button[index].setBorderPainted(false);
            }
        });
    }

    private JComboBox setupComboBox() {
        UIManager.put("ComboBox.background", Color.LIGHT_GRAY);
        UIManager.put("ComboBox.foreground", gameAssets.getDarkerGrey());
        UIManager.put("ComboBox.buttonBackground", gameAssets.getDarkerGrey());
        UIManager.put("ComboBox.buttonDarkShadow", Color.LIGHT_GRAY);
        UIManager.put("ComboBox.buttonHighlight", gameAssets.getDarkerGrey());
        UIManager.put("ComboBox.buttonShadow", gameAssets.getDarkerGrey());
        UIManager.put("ComboBox.selectionBackground", gameAssets.getDarkerGrey());
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.border", BorderFactory.createEmptyBorder());

        String[] amountChoice = new String[] {"TWO", "THREE", "FOUR", "FIVE", "SIX"};
        JComboBox amountList = new JComboBox<>(amountChoice);
        amountList.setUI(new BasicComboBoxUI(){
            @Override
            protected void installDefaults() {
                super.installDefaults();
            }
        });
        amountList.setFont(new Font("Bulky Pixels", Font.BOLD, (int)(20 * resolution.getScalePercentage())));
        amountList.setSelectedIndex(0);
        return amountList;
    }

    private void startGame() {
        gameScreen.createScreen("Cluedo");
        gameScreen.setupScreen(1);
        gameScreen.getGameCards().dealCards(gameScreen.getGamePlayers());
        gameScreen.getGameSuspects().setSpawnsOccupied(gameScreen.getGameBoard());
        SwingUtilities.invokeLater(() -> {
            gameScreen.displayScreen();
            gameInput.initialSetup();
        });
    }
}